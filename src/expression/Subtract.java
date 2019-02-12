package expression;

public class Subtract extends BinaryOperation {

	public Subtract(CommonExpression left, CommonExpression right) {
		super(left, right);
	}

	protected int calculate(int left, int right) {
		return left - right;
	}

	protected double calculate(double left, double right) {
		return left - right;
	}

}