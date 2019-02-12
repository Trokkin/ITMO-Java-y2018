package expression;

public abstract class CommonExpression implements Expression, DoubleExpression, TripleExpression {
	public void dump() {
		System.out.print(this.getClass().getName());
	}

	public abstract double evaluate(double x);

	public abstract int evaluate(int x);

	public abstract int evaluate(int x, int y, int z);
}