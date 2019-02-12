package expression.bitwise;

import expression.CommonExpression;
import expression.BinaryOperation;

public class BitwiseOr extends BinaryOperation {
	public BitwiseOr(CommonExpression left, CommonExpression right) {
		super(left, right);
	}

	protected int calculate(int left, int right) {
		return left | right;
	}

	protected double calculate(double left, double right) {
		return -1;
	}
}
