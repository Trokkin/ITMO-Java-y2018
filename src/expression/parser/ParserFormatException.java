package expression.parser;

public class ParserFormatException extends Exception {
	private final int pos;

	public ParserFormatException(String message, int pos) {
		super(message);
		this.pos = pos;
	}

	public int getErrorPosition() {
		return pos;
	}
}