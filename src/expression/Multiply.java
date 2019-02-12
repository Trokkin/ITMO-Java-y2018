package expression;

public class Multiply extends BinaryOperation {

	public Multiply(CommonExpression left, CommonExpression right) {
		super(left, right);
	}

	protected int calculate(int left, int right) {
		return left * right;
	}

	protected double calculate(double left, double right) {
		return left * right;
	}

}