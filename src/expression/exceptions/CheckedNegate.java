package expression.exceptions;

import expression.CommonExpression;

public class CheckedNegate extends CheckedUnaryOperation {

	public CheckedNegate(CommonExpression left) {
		super(left);
	}

	protected int calculate(int left) throws ArithmeticException {
		if (left == Integer.MIN_VALUE) {
			throw new ArithmeticException("overflow");
		}
		return -left;
	}
}