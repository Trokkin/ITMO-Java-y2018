package expression.bitwise;

import expression.CommonExpression;
import expression.UnaryOperation;

public class BitwiseCount extends UnaryOperation {
	public BitwiseCount(CommonExpression left) {
		super(left);
	}

	protected int calculate(int left) {
		return Integer.bitCount(left);
	}

	protected double calculate(double left) {
		return -1;
	}
}
