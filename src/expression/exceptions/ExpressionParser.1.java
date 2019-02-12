package expression.exceptions;

import expression.*;
import expression.parser.*;

public class ExpressionParser implements Parser {

	private String subStr;
	private Character currTok;
	private int numberValue;
	private String str;
	private int index;
	private int countBracket;

	private Variable var_x = null;
	private Variable var_y = null;
	private Variable var_z = null;

	public CommonExpression parse(String str) throws ParserFormatException {
		this.str = str;
		index = 0;
		countBracket = 0;
		CommonExpression e = binop();
		return e;
	}

	private String readWord() {
		StringBuilder s = new StringBuilder();
		for (; index < str.length(); index++) {
			char c = str.charAt(index);
			if ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9')) {
				s.append(c);
			} else {
				return s.toString();
			}
		}
		return s.toString();
	}

	private int readInt() throws ParserFormatException {
		StringBuilder s = new StringBuilder();
		int lastIndex = index + 1;
		if (currTok == '-') {
			s.append('-');
		}
		while (index < str.length() && str.charAt(index) == ' ') {
			index++;
		}
		for (; index < str.length(); index++) {
			if (str.charAt(index) >= '0' && str.charAt(index) <= '9') {
				s.append(str.charAt(index));
			} else {
				return Integer.valueOf(s.toString());
			}
		}
		try {
			return Integer.valueOf(s.toString());
		} catch (NumberFormatException e) {
			throw new ParserFormatException("Constant overflow on index " + lastIndex, lastIndex);
		}
	}
	/*
	 * private int readInt() throws ParserFormatException { int lastIndex = index +
	 * 1; String token = readWord(); if (token == null) { throw new
	 * ParserFormatException("No integer found on " + lastIndex, lastIndex); } int
	 * length = token.length(); if (length == 0) { throw new
	 * ParserFormatException("No integer found on " + lastIndex, lastIndex); } int i
	 * = 0; if (token.charAt(0) == '-') { if (length == 1) { throw new
	 * ParserFormatException("Incorrect symbol on index " + lastIndex, lastIndex); }
	 * i = 1; } for (; i < length; i++) { char c = token.charAt(i); if (c < '0' || c
	 * > '9') { throw new ParserFormatException("Incorrect symbol on index " +
	 * lastIndex, lastIndex); } } try { return Integer.valueOf(token.toString()); }
	 * catch (NumberFormatException e) { throw new
	 * ParserFormatException("Constant overflow on index " + lastIndex, lastIndex);
	 * } }
	 */

	private void getToken() throws ParserFormatException {
		currTok = 'e';
		numberValue = -1;
		while (index < str.length() && (str.charAt(index) == ' ' || str.charAt(index) == 9)) {
			index++;
		}
		if (index == str.length()) {
			return;
		}

		switch (str.charAt(index)) {
		case '-':
		case '+':
		case '*':
		case '/':
		case '(':
		case ')':
		case 'x':
		case 'y':
		case 'z':
			currTok = str.charAt(index++);
			return;
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			numberValue = readInt();
			currTok = 'n';
			return;
		default:
			int lastIndex = index;
			subStr = readWord();
			if (subStr.compareTo("abs") == 0 || subStr.compareTo("sqrt") == 0) {
				currTok = 's';
			} else if (subStr.compareTo("min") == 0 || subStr.compareTo("max") == 0) {
				currTok = 'c';
			} else {
				throw new ParserFormatException("Incorrect symbol on index " + (lastIndex + 1), lastIndex + 1);
			}
		}

	}

	private Character nextToken(int ind) {
		while (ind < str.length() && str.charAt(ind) == ' ') {
			ind++;
		}
		if (ind < str.length()) {
			return str.charAt(ind);
		}
		return '\n';
	}

	private CommonExpression prim() throws ParserFormatException {
		getToken();

		switch (currTok) {
		case 'x':
			if (var_x == null) {
				var_x = new Variable("x");
			}
			getToken();
			return var_x;
		case 'y':
			if (var_y == null) {
				var_y = new Variable("y");
			}
			getToken();
			return var_y;
		case 'z':
			if (var_z == null) {
				var_z = new Variable("z");
			}
			getToken();
			return var_z;
		case '-':
			Character next = nextToken(index);
			if (next >= '0' && next <= '9') {
				CommonExpression e = new Const(readInt());
				getToken();
				return e;
			}
			return new CheckedNegate(prim());
		case '(':
			countBracket++;
			CommonExpression e = binop();
			if (currTok != ')') {
				throw new ParserFormatException("No closing parenthesis on index " + index, index);
			}
			countBracket--;
			getToken();
			return e;
		case 'n':
			int v = numberValue;
			getToken();
			return new Const(v);
		case 's':
			if (subStr.compareTo("abs") == 0) {
				return new CheckedAbs(prim());
			} else {
				return new CheckedSqrt(prim());
			}
		default:
			throw new ParserFormatException("No argument on index " + index, index);
		}
	}

	private CommonExpression term() throws ParserFormatException {
		CommonExpression left = prim();
		while (true) {
			switch (currTok) {
			case '*':
				left = new CheckedMultiply(left, prim());
				break;
			case '/':
				left = new CheckedDivide(left, prim());
				break;
			default:
				return left;
			}
		}
	}

	private CommonExpression expr() throws ParserFormatException {
		CommonExpression left = term();
		while (true) {
			switch (currTok) {
			case '+':
				left = new CheckedAdd(left, term());
				break;
			case '-':
				left = new CheckedSubtract(left, term());
				break;
			case ')':
				if (countBracket > 0) {
					return left;
				} else {
					throw new ParserFormatException("No opening parenthesis on index " + index, index);
				}
			case '(':
				throw new ParserFormatException("Start symbol on index " + index, index);
			default:
				return left;
			}
		}
	}

	private CommonExpression binop() throws ParserFormatException {
		CommonExpression left = expr();
		while (true) {
			switch (currTok) {
			case 'c':
				if (subStr.compareTo("min") == 0) {
					left = new Min(left, expr());
				} else {
					left = new Max(left, expr());
				}
				break;
			case ')':
				if (countBracket > 0) {
					return left;
				} else {
					throw new ParserFormatException("No opening parenthesis on index " + index, index);
				}
			case '(':
				throw new ParserFormatException("Start symbol on index " + index, index);
			default:
				return left;
			}
		}
	}
}
