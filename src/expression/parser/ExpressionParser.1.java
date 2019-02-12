package expression.parser;

import expression.*;
import expression.bitwise.*;
import expression.parser.ParserFormatException;

public class ExpressionParser implements Parser {

	private String subStr;
	private Character currTok;
	private int numberValue;
	private String str;
	private int index;
	private int countBracket;

	public CommonExpression parse(String str) {
		this.str = str;
		index = 0;
		countBracket = 0;
		try {
			CommonExpression e = bitwiseOr(true);
			return e;
		} catch (ParserFormatException e) {
			e.printStackTrace();
			return null;
		}
	}

	private int readInt() throws ParserFormatException {
		StringBuilder s = new StringBuilder();
		int lastIndex = index;
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
			throw new ParserFormatException("Constant overflow on index " + (lastIndex + 1), lastIndex + 1);
		}
	}

	private String readWord() throws ParserFormatException {
		StringBuilder s = new StringBuilder();
		for (; index < str.length(); index++) {
			if (str.charAt(index) >= 'a' && str.charAt(index) <= 'z') {
				s.append(str.charAt(index));
			} else {
				return s.toString();
			}
		}
		return s.toString();
	}

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
		case '&':
		case '^':
		case '|':
		case '~':
		case '(':
		case ')':
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
		case 'x':
		case 'y':
		case 'z':
			currTok = str.charAt(index++);
			return;
		default:
			int lastIndex = index;
			subStr = readWord();
			if (subStr.compareTo("count") == 0) {
				currTok = 's';
			} else {
				throw new ParserFormatException("Incorrect symbol: index " + (lastIndex + 1), (lastIndex + 1));
			}
		}
	}

	private Character nextToken(int ind) throws ParserFormatException {
		while (ind < str.length() && str.charAt(ind) == ' ') {
			ind++;
		}
		if (ind < str.length()) {
			return str.charAt(ind);
		}
		return '\n';
	}

	private CommonExpression prim(boolean get) throws ParserFormatException {
		if (get) {
			getToken();
		}

		switch (currTok) {
		case 'x':
		case 'y':
		case 'z':
			Character ch = currTok;
			getToken();
			return new Variable(String.valueOf(ch));
		case '-':
			Character next = nextToken(index);
			if (next >= '0' && next <= '9') {
				CommonExpression e = new Const(readInt());
				getToken();
				return e;
			}
			return new Negate(prim(true));
		case '~':
			return new BitwiseNot(prim(true));
		case '(':
			countBracket++;
			CommonExpression e = bitwiseOr(true);
			if (currTok != ')') {
				throw new ParserFormatException("No closing parenthesis: index " + index, index);
			}
			countBracket--;
			getToken();
			return e;
		case 's':
			return new BitwiseCount(prim(true));
		case 'n':
			int v = numberValue;
			getToken();
			return new Const(v);
		default:
			throw new ParserFormatException("No argument: index " + index, index);
		}
	}

	private CommonExpression term(boolean get) throws ParserFormatException {
		CommonExpression left = prim(get);
		while (true) {
			switch (currTok) {
			case '*':
				left = new Multiply(left, prim(true));
				break;
			case '/':
				left = new Divide(left, prim(true));
				break;
			default:
				return left;
			}
		}
	}

	private CommonExpression expr(boolean get) throws ParserFormatException {
		CommonExpression left = term(get);
		while (true) {
			switch (currTok) {
			case '+':
				left = new Add(left, term(true));
				break;
			case '-':
				left = new Subtract(left, term(true));
				break;
			case ')':
				if (countBracket > 0) {
					return left;
				} else {
					throw new ParserFormatException("No opening parenthesis: index " + index, index);
				}
			case '(':
				throw new ParserFormatException("Start symbol: index " + index, index);
			default:
				return left;
			}
		}
	}

	private CommonExpression bitwiseAnd(boolean get) throws ParserFormatException {
		CommonExpression left = expr(get);
		while (true) {
			switch (currTok) {
			case '&':
				left = new BitwiseAnd(left, expr(true));
				break;
			case ')':
				if (countBracket > 0) {
					return left;
				} else {
					throw new ParserFormatException("No opening parenthesis: index " + index, index);
				}
			case '(':
				throw new ParserFormatException("Start symbol: index " + index, index);
			default:
				return left;
			}
		}
	}

	private CommonExpression bitwiseXor(boolean get) throws ParserFormatException {
		CommonExpression left = bitwiseAnd(get);
		while (true) {
			switch (currTok) {
			case '^':
				left = new BitwiseXor(left, bitwiseAnd(true));
				break;
			case ')':
				if (countBracket > 0) {
					return left;
				} else {
					throw new ParserFormatException("No opening parenthesis: index " + index, index);
				}
			case '(':
				throw new ParserFormatException("Start symbol: index " + index, index);
			default:
				return left;
			}
		}
	}

	private CommonExpression bitwiseOr(boolean get) throws ParserFormatException {
		CommonExpression left = bitwiseXor(get);
		while (true) {
			switch (currTok) {
			case '|':
				left = new BitwiseOr(left, bitwiseXor(true));
				break;
			case ')':
				if (countBracket > 0) {
					return left;
				} else {
					throw new ParserFormatException("No opening parenthesis: index " + index, index);
				}
			case '(':
				throw new ParserFormatException("Start symbol: index " + index, index);
			default:
				return left;
			}
		}
	}
}