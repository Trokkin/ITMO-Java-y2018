package expression.exceptions;

import expression.CommonExpression;

public class CheckedLow extends CheckedUnaryOperation {

	public CheckedLow(CommonExpression left) {
		super(left);
	}

	protected int calculate(int left) throws ArithmeticException {
		return Integer.lowestOneBit(left);
	}
}