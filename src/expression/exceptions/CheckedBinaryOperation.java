package expression.exceptions;

import expression.BinaryOperation;
import expression.CommonExpression;

public abstract class CheckedBinaryOperation extends BinaryOperation {
	public CheckedBinaryOperation(CommonExpression left, CommonExpression right) {
		super(left, right);
	}

	protected abstract int calculate(int left, int right) throws ArithmeticException;

	protected double calculate(double left, double right) throws ArithmeticException {
		throw new ArithmeticException("Non-integer value found");
	}
}