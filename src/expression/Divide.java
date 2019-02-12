package expression;

public class Divide extends BinaryOperation {
	public Divide(CommonExpression left, CommonExpression right) {
		super(left, right);
	}

	protected int calculate(int left, int right) {
		return left / right;
	}

	protected double calculate(double left, double right) {
		return left / right;
	}
}
