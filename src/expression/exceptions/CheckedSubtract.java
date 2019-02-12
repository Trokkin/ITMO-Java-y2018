package expression.exceptions;

import expression.exceptions.CheckedBinaryOperation;
import expression.CommonExpression;

public class CheckedSubtract extends CheckedBinaryOperation {

	public CheckedSubtract(CommonExpression a, CommonExpression b) {
		super(a, b);
	}

	protected int calculate(int a, int b) throws ArithmeticException {
		if (b <= 0) {
			if (Integer.MAX_VALUE + b < a) {
				throw new ArithmeticException("overflow");
			}
		} else if (a >= 0) {
			if (Integer.MAX_VALUE - a < -b) {
				throw new ArithmeticException("overflow");
			}
		} else {
			if (Integer.MIN_VALUE - a > -b) {
				throw new ArithmeticException("overflow");
			}
		}
		return a - b;
	}
}