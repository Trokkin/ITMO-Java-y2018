package expression;

public class Const extends CommonExpression {
	private final int intValue;
	private final double doubleValue;

	public Const(int value) {
		intValue = value;
		doubleValue = value;
	}

	public Const(double value) {
		intValue = 0;
		doubleValue = value;
	}

	public int evaluate(int x) {
		return intValue;
	}

	public int evaluate(int x, int y, int z) {
		return intValue;
	}

	public double evaluate(double x) {
		return doubleValue;
	}
}