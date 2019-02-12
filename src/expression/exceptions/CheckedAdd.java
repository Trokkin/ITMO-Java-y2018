package expression.exceptions;

import expression.exceptions.CheckedBinaryOperation;
import expression.CommonExpression;

public class CheckedAdd extends CheckedBinaryOperation {

	public CheckedAdd(CommonExpression a, CommonExpression b) {
		super(a, b);
	}

	protected int calculate(int a, int b) throws ArithmeticException {
		if (a >= 0) {
			if (Integer.MAX_VALUE - a < b) {
				throw new ArithmeticException("overflow");
			}
		} else if (b >= 0) {
			if (Integer.MAX_VALUE - b < a) {
				throw new ArithmeticException("overflow");
			}
		} else {
			if (Integer.MIN_VALUE - a > b) {
				throw new ArithmeticException("overflow");
			}
		}
		return a + b;
	}
}