package expression.exceptions;

import expression.BinaryOperation;
import expression.CommonExpression;

public class Min extends BinaryOperation {
	public Min(CommonExpression a, CommonExpression b) {
		super(a, b);
	}

	protected int calculate(int a, int b) {
		return Integer.min(a, b);
	}

	protected double calculate(double a, double b) {
		return Double.min(a, b);
	}
}