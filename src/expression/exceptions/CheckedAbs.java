package expression.exceptions;

import expression.CommonExpression;

public class CheckedAbs extends CheckedUnaryOperation {

	public CheckedAbs(CommonExpression left) {
		super(left);
	}

	protected int calculate(int left) throws ArithmeticException {
		if (left == Integer.MIN_VALUE) {
			throw new ArithmeticException("overflow");
		}
		return left < 0 ? -left : left;
	}
}