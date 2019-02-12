package expression;

public abstract class BinaryOperation extends CommonExpression {
	public final CommonExpression left;
	public final CommonExpression right;

	public BinaryOperation(CommonExpression left, CommonExpression right) {
		this.left = left;
		this.right = right;
	}

	public void dump() {
		System.out.print('(');
		left.dump();
		System.out.print(' ');
		System.out.print(this.getClass().getName());
		System.out.print(' ');
		right.dump();
		System.out.print(')');
	}

	protected abstract double calculate(double left, double right);

	protected abstract int calculate(int left, int right);

	public double evaluate(double x) {
		return calculate(left.evaluate(x), right.evaluate(x));
	}

	public int evaluate(int x) {
		return calculate(left.evaluate(x), right.evaluate(x));
	}

	public int evaluate(int x, int y, int z) {
		return calculate(left.evaluate(x, y, z), right.evaluate(x, y, z));
	}

}