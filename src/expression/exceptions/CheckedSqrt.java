package expression.exceptions;

import expression.CommonExpression;

public class CheckedSqrt extends CheckedUnaryOperation {

	public CheckedSqrt(CommonExpression left) {
		super(left);
	}

	private int sqrt(int n) {
		if (n <= 0) {
			return 0;
		}
		int x = 1;
		boolean decreased = false;
		for (;;) {
			int nx = (x + n / x) >> 1;
			if (x == nx || nx > x && decreased) {
				break;
			}
			decreased = nx < x;
			x = nx;
		}
		return x;
	}

	protected int calculate(int left) throws ArithmeticException {
		if (left < 0) {
			throw new ArithmeticException("sqrt expr lesser than zero");
		}
		return sqrt(left);
	}
}