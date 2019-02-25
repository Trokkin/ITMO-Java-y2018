package expression.exceptions;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import expression.*;
import expression.parser.*;

public class ExpressionParser implements Parser {

	private enum TOKEN {
		UNDEF, NUM, VAR, UN_FUNC, BIN_FUNC, OPEN_BRACKET, CLOSE_BRACKET, OP_PLUS, OP_MINUS, OP_MULTIPLY, OP_DIVIDE
	}

	static final HashMap<String, Class<? extends CommonExpression>> UNARY_FUNCS = new HashMap<>(
			Map.of("abs", CheckedAbs.class, "sqrt", CheckedSqrt.class, "log2", CheckedLog.class, "pow2",
					CheckedPow.class, "high", CheckedHigh.class, "low", CheckedLow.class));
	static final HashMap<String, Class<? extends CommonExpression>> BINARY_FUNCS = new HashMap<>(
			Map.of("min", Min.class, "max", Max.class));

	static final String[] VAR_NAMES = { "x", "y", "z" };
	private HashMap<String, Variable> variable_ref = new HashMap<>();

	private String tokenStr;
	private TOKEN currentToken;
	private int tokenInt;
	private String str;
	private int strPtr;
	private int bracketCount;

	public CommonExpression parse(String str) throws ParserFormatException {
		this.str = str;
		strPtr = 0;
		bracketCount = 0;
		CommonExpression e = binfunc();
		return e;
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

	private String formatException(int errorIndex) {
		if (errorIndex < 1) {
			errorIndex = 1;
		}
		String s = " on index " + errorIndex + " \t|---> ";
		if (str.length() > 20) {
			int a = errorIndex - 10;
			if (a <= 0) {
				a = 0;
				s += "...";
			}
			s += str.substring(a, errorIndex - 1) + "`" + str.substring(errorIndex - 1, a + 20) + "...";
		} else {
			s += str.substring(0, errorIndex - 1) + "`" + str.substring(errorIndex - 1);
		}
		return s + " |";
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
			throw new ParserFormatException("Constant overflow for '" + s.toString() + "'" + formatException(lastIndex),
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
				throw new ParserFormatException(
						"Undefined symbol '" + str.charAt(strPtr) + "'" + formatException(lastIndex), lastIndex);
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
				throw new ParserFormatException("Undefined token '" + tokenStr + "'" + formatException(lastIndex),
						lastIndex);
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
			return new CheckedNegate(prim());
		case OPEN_BRACKET:
			bracketCount++;
			CommonExpression expr = binfunc();
			if (currentToken != TOKEN.CLOSE_BRACKET) {
				throw new ParserFormatException("No closing parenthesis" + formatException(lastIndex), lastIndex);
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
			throw new ParserFormatException("No argument" + formatException(lastIndex), lastIndex);
		}
	}

	private CommonExpression term() throws ParserFormatException {
		CommonExpression left = prim();
		while (true) {
			switch (currentToken) {
			case OP_MULTIPLY:
				left = new CheckedMultiply(left, prim());
				break;
			case OP_DIVIDE:
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
			switch (currentToken) {
			case OP_PLUS:
				left = new CheckedAdd(left, term());
				break;
			case OP_MINUS:
				left = new CheckedSubtract(left, term());
				break;
			default:
				return left;
			}
		}
	}

	private CommonExpression binfunc() throws ParserFormatException {
		CommonExpression left = expr();
		while (true) {
			switch (currentToken) {
			case BIN_FUNC:
				try {
					left = (CommonExpression) BINARY_FUNCS.get(tokenStr)
							.getConstructor(CommonExpression.class, CommonExpression.class).newInstance(left, expr());
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
					throw new ParserFormatException("No opening parenthesis" + formatException(strPtr), strPtr);
				}
			case OPEN_BRACKET:
				throw new ParserFormatException("Start symbol" + formatException(strPtr), strPtr);
			default:
				return left;
			}
		}
	}
}
