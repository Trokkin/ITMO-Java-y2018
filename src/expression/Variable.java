package expression;

public class Variable extends CommonExpression {
	private final String name;

	public Variable(String name) {
		this.name = name;
	}

	public int evaluate(int x) {
		return x;
	}

	public int evaluate(int x, int y, int z) {
		return name.equals("x") ? x : name.equals("y") ? y : name.equals("z") ? z : 0;
	}

	public double evaluate(double x) {
		return x;
	}
}