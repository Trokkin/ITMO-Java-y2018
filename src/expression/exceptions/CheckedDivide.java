package expression.exceptions;

import expression.exceptions.CheckedBinaryOperation;
import expression.CommonExpression;

public class CheckedDivide extends CheckedBinaryOperation {

	public CheckedDivide(CommonExpression a, CommonExpression b) {
		super(a, b);
	}

	protected int calculate(int a, int b) throws ArithmeticException {
		if (b == 0) {
			throw new ArithmeticException("division by zero");
		} else if (a == Integer.MIN_VALUE && b == -1) {
			throw new ArithmeticException("overflow");
		}
		return a / b;
	}
}