package expression.exceptions;

import expression.CommonExpression;

public class CheckedPow extends CheckedUnaryOperation {

	public CheckedPow(CommonExpression left) {
		super(left);
	}

	protected int calculate(int left) throws ArithmeticException {
		if (left < 0) {
			throw new ArithmeticException("sqrt expr lesser than zero");
		}
		if (left > 31) {
			throw new ArithmeticException("overflow");
		}
		return 1 << left;
	}
}