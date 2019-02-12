package expression.bitwise;

import expression.CommonExpression;
import expression.UnaryOperation;

public class BitwiseNot extends UnaryOperation {
	public BitwiseNot(CommonExpression left) {
		super(left);
	}

	protected int calculate(int left) {
		return ~left;
	}

	protected double calculate(double left) {
		return -1;
	}
}
