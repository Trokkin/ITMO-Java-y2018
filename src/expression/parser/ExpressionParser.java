package expression.parser;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import expression.*;
import expression.bitwise.*;
import expression.parser.*;

public class ExpressionParser implements Parser {

	private enum TOKEN {
		UNDEF, NUM, VAR, UN_FUNC, BIN_FUNC, OPEN_BRACKET, CLOSE_BRACKET, OP_PLUS, OP_MINUS, OP_MULTIPLY, OP_DIVIDE,
		OP_BIT_OR, OP_BIT_XOR, OP_BIT_AND, OP_BIT_NOT
	}

	static final HashMap<String, Class<? extends CommonExpression>> UNARY_FUNCS = new HashMap<>(
			Map.of("count", BitwiseCount.class));
	static final HashMap<String, Class<? extends CommonExpression>> BINARY_FUNCS = new HashMap<>();

	static final String[] VAR_NAMES = { "x", "y", "z" };
	private HashMap<String, Variable> variable_ref = new HashMap<>();

	private String tokenStr;
	private TOKEN currentToken;
	private int tokenInt;
	private String str;
	private int strPtr;
	private int bracketCount;

	public CommonExpression parse(String str) {
		this.str = str;
		strPtr = 0;
		bracketCount = 0;
		try {
			CommonExpression e = binfunc();
			return e;
		} catch (ParserFormatException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void skipWhitespaces() {
		while (strPtr < str.length() && Character.isWhitespace(str.charAt(strPtr))) {
			strPtr++;
		}
	}

	private String readWord() {
		StringBuilder s = new StringBuilder();
		for (; strPtr < str.length(); strPtr++) {
			char c = str.charAt(strPtr);
			if (!Character.isAlphabetic(c) && !Character.isDigit(c)) {
				break;
			}
			s.append(c);
		}
		return s.toString();
	}

	private int readInt() throws ParserFormatException {
		int lastIndex = strPtr + 1;
		StringBuilder s = new StringBuilder();
		if (currentToken == TOKEN.OP_MINUS) {
			s.append('-');
		}
		skipWhitespaces();
		for (; strPtr < str.length(); strPtr++) {
			char c = str.charAt(strPtr);
			if (!Character.isDigit(c)) {
				break;
			}
			s.append(c);
		}
		try {
			return Integer.valueOf(s.toString());
		} catch (NumberFormatException e) {
			throw new ParserFormatException("Constant overflow for '" + s.toString() + "' on index " + lastIndex,
					lastIndex);
		}
	}

	private void getToken() throws ParserFormatException {
		currentToken = TOKEN.UNDEF;
		tokenInt = -1;
		skipWhitespaces();

		if (strPtr == str.length()) {
			return;
		}

		switch (str.charAt(strPtr++)) {
		case '-':
			currentToken = TOKEN.OP_MINUS;
			return;
		case '+':
			currentToken = TOKEN.OP_PLUS;
			return;
		case '*':
			currentToken = TOKEN.OP_MULTIPLY;
			return;
		case '/':
			currentToken = TOKEN.OP_DIVIDE;
			return;
		case '(':
			currentToken = TOKEN.OPEN_BRACKET;
			return;
		case ')':
			currentToken = TOKEN.CLOSE_BRACKET;
			return;
		case '&':
			currentToken = TOKEN.OP_BIT_AND;
			return;
		case '^':
			currentToken = TOKEN.OP_BIT_XOR;
			return;
		case '|':
			currentToken = TOKEN.OP_BIT_OR;
			return;
		case '~':
			currentToken = TOKEN.OP_BIT_NOT;
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
			currentToken = TOKEN.NUM;
			strPtr--;
			tokenInt = readInt();
			return;
		default:
			int lastIndex = strPtr--;
			if (!Character.isAlphabetic(str.charAt(strPtr))) {
				throw new ParserFormatException("Undefined symbol '" + str.charAt(strPtr) + "' on index " + lastIndex,
						lastIndex);
			}
			tokenStr = readWord();

			if (UNARY_FUNCS.containsKey(tokenStr)) {
				currentToken = TOKEN.UN_FUNC;
			} else if (BINARY_FUNCS.containsKey(tokenStr)) {
				currentToken = TOKEN.BIN_FUNC;
			} else {
				for (int i = VAR_NAMES.length; i-- > 0;) {
					if (tokenStr.compareTo(VAR_NAMES[i]) == 0) {
						currentToken = TOKEN.VAR;
						return;
					}
				}
				throw new ParserFormatException("Undefined token '" + tokenStr + "' on index " + lastIndex, lastIndex);
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
		int lastIndex = strPtr + 1;
		getToken();

		switch (currentToken) {
		case VAR:
			Variable v = variable_ref.get(tokenStr);
			if (v == null) {
				v = new Variable(tokenStr);
				variable_ref.put(tokenStr, v);
			}
			getToken();
			return v;
		case OP_MINUS:
			Character next = nextToken(strPtr);
			// if (next >= '0' && next <= '9') {
			if (Character.isDigit(next)) {
				CommonExpression e = new Const(readInt());
				getToken();
				return e;
			}
			return new Negate(prim());
		case OP_BIT_NOT:
			return new BitwiseNot(prim());
		case OPEN_BRACKET:
			bracketCount++;
			CommonExpression expr = binfunc();
			if (currentToken != TOKEN.CLOSE_BRACKET) {
				throw new ParserFormatException("No closing parenthesis on index " + lastIndex, lastIndex);
			}
			bracketCount--;
			getToken();
			return expr;
		case NUM:
			int t = tokenInt;
			getToken();
			return new Const(t);
		case UN_FUNC:
			try {
				return (CommonExpression) UNARY_FUNCS.get(tokenStr).getConstructor(CommonExpression.class)
						.newInstance(prim());
			} catch (NoSuchMethodException e) {
			} catch (InstantiationException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}
		default:
			throw new ParserFormatException("No argument on index " + lastIndex, lastIndex);
		}
	}

	private CommonExpression term() throws ParserFormatException {
		CommonExpression left = prim();
		while (true) {
			switch (currentToken) {
			case OP_MULTIPLY:
				left = new Multiply(left, prim());
				break;
			case OP_DIVIDE:
				left = new Divide(left, prim());
				break;
			default:
				return left;
			}
		}
	}

	private CommonExpression expr() throws ParserFormatException {
		CommonExpression left = term();
		while (true) {
			switch (currentToken) {
			case OP_PLUS:
				left = new Add(left, term());
				break;
			case OP_MINUS:
				left = new Subtract(left, term());
				break;
			default:
				return left;
			}
		}
	}

	private CommonExpression bitwiseAnd() throws ParserFormatException {
		CommonExpression left = expr();
		while (currentToken == TOKEN.OP_BIT_AND) {
			left = new BitwiseAnd(left, expr());
		}
		return left;
	}

	private CommonExpression bitwiseXor() throws ParserFormatException {
		CommonExpression left = bitwiseAnd();
		while (currentToken == TOKEN.OP_BIT_XOR) {
			left = new BitwiseXor(left, bitwiseAnd());
		}
		return left;
	}

	private CommonExpression bitwiseOr() throws ParserFormatException {
		CommonExpression left = bitwiseXor();
		while (currentToken == TOKEN.OP_BIT_OR) {
			left = new BitwiseOr(left, bitwiseXor());
		}
		return left;
	}

	private CommonExpression binfunc() throws ParserFormatException {
		CommonExpression left = bitwiseOr();
		while (true) {
			switch (currentToken) {
			case BIN_FUNC:
				try {
					left = (CommonExpression) BINARY_FUNCS.get(tokenStr)
							.getConstructor(CommonExpression.class, CommonExpression.class)
							.newInstance(left, bitwiseOr());
				} catch (NoSuchMethodException e) {
				} catch (InstantiationException e) {
				} catch (IllegalAccessException e) {
				} catch (InvocationTargetException e) {
				}
				break;
			case CLOSE_BRACKET:
				if (bracketCount > 0) {
					return left;
				} else {
					throw new ParserFormatException("No opening parenthesis on index " + strPtr, strPtr);
				}
			case OPEN_BRACKET:
				throw new ParserFormatException("Start symbol on index " + strPtr, strPtr);
			default:
				return left;
			}
		}
	}
}
