package expression;

public class Negate extends UnaryOperation {
	public Negate(CommonExpression left) {
		super(left);
	}

	protected int calculate(int left) {
		return -left;
	}

	protected double calculate(double left) {
		return -1;
	}
}
