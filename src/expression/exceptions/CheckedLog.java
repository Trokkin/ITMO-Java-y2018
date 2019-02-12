package expression.exceptions;

import expression.CommonExpression;

public class CheckedLog extends CheckedUnaryOperation {

	public CheckedLog(CommonExpression left) {
		super(left);
	}

	protected int log2(int n) {
		int x = 0;
		while ((n >>= 1) > 0) {
			x++;
		}
		return x;
	}

	protected int calculate(int left) throws ArithmeticException {
		if (left <= 0) {
			throw new ArithmeticException("log2 expr lesser than zero");
		}
		return log2(left);
	}
}