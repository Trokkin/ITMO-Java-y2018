package expression;

public abstract class UnaryOperation extends CommonExpression {
	public final CommonExpression left;

	public UnaryOperation(CommonExpression left) {
		this.left = left;
	}

	protected abstract double calculate(double left);

	protected abstract int calculate(int left);

	public void dump() {
		System.out.print('(');
		System.out.print(this.getClass().getName());
		System.out.print(' ');
		left.dump();
		System.out.print(')');
	}

	public double evaluate(double x) {
		return calculate(left.evaluate(x));
	}

	public int evaluate(int x) {
		return calculate(left.evaluate(x));
	}

	public int evaluate(int x, int y, int z) {
		return calculate(left.evaluate(x, y, z));
	}

}