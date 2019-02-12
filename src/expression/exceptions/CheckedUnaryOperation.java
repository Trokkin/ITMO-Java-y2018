package expression.exceptions;

import expression.UnaryOperation;
import expression.CommonExpression;

public abstract class CheckedUnaryOperation extends UnaryOperation {
	public CheckedUnaryOperation(CommonExpression left) {
		super(left);
	}

	protected abstract int calculate(int left) throws ArithmeticException;

	protected double calculate(double left) throws ArithmeticException {
		throw new ArithmeticException("Non-integer value found");
	}
}