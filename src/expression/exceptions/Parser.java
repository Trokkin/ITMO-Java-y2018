package expression.exceptions;

import expression.TripleExpression;
import expression.parser.ParserFormatException;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface Parser {
	TripleExpression parse(String expression) throws ParserFormatException;
}