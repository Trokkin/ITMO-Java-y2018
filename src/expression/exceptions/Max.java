package expression.exceptions;

import expression.BinaryOperation;
import expression.CommonExpression;

public class Max extends BinaryOperation {
	public Max(CommonExpression a, CommonExpression b) {
		super(a, b);
	}

	protected int calculate(int a, int b) {
		return Integer.max(a, b);
	}

	protected double calculate(double a, double b) {
		return Double.max(a, b);
	}
}