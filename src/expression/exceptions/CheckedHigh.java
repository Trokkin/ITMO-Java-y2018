package expression.exceptions;

import expression.CommonExpression;

public class CheckedHigh extends CheckedUnaryOperation {

	public CheckedHigh(CommonExpression left) {
		super(left);
	}

	protected int calculate(int left) throws ArithmeticException {
		return Integer.highestOneBit(left);
	}
}