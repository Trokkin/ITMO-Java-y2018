package expression.exceptions;

import expression.exceptions.CheckedBinaryOperation;
import expression.CommonExpression;

public class CheckedMultiply extends CheckedBinaryOperation {
	public CheckedMultiply(CommonExpression a, CommonExpression b) {
		super(a, b);
	}

	protected int calculate(int a, int b) throws ArithmeticException {
		if (a > 0 && b > 0) {
			if (Integer.MAX_VALUE / a < b) {
				throw new ArithmeticException("overflow");
			}
		} else if (a < 0 && b < 0) {
			if (Integer.MAX_VALUE / a > b) {
				throw new ArithmeticException("overflow");
			}
		} else {
			if ((a > 0 && Integer.MIN_VALUE / a > b) || (b > 0 && Integer.MIN_VALUE / b > a)) {
				throw new ArithmeticException("overflow");
			}
		}
		return a * b;
	}
}