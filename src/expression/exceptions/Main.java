package expression.exceptions;

import expression.CommonExpression;
import expression.parser.ParserFormatException;

public class Main {
	public static void main(String[] args) {
		CommonExpression expr = null;
		try {
			expr = new ExpressionParser().parse(args[0]);
		} catch (ParserFormatException e) {
			System.out.print("error: ");
			System.out.println(e.getMessage());
			System.out.println(args[0]);
			for (int i = e.getErrorPosition(); i-- > 1;) {
				System.out.print(' ');
			}
			System.out.println('^');
		}
		if (expr != null) {
			expr.dump();
			System.out.println("\t\tx\t\tf");
			for (int x = 0; x <= 10; x++) {
				System.out.print("\t\t");
				System.out.print(x);
				System.out.print("\t\t");
				try {
					System.out.print(expr.evaluate(x));
				} catch (ArithmeticException e) {
					System.out.print(e.getMessage());
				}
				System.out.println();
			}
		}
	}
}