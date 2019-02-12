TARGETS=SumBigInteger ReverseTranspose SumHexFile WordStatWords WordStatIndex WordStatLineIndex BinarySearchMissing Reverse
SOURCE_TESTS=expression.ExpressionTest expression.DoubleExpressionTest expression.TripleExpressionTest expression.parser.ParserTest expression.parser.ParserBitwiseTest expression.parser.ParserNotCountTest expression.exceptions.ExceptionsTest expression.exceptions.ExceptionsMinMaxTest expression.exceptions.ExceptionsPowLog2Test expression.exceptions.ExceptionsHighLowTest
BINS=expression.exceptions.Main

$(BINS):
	javac -d ./ -sourcepath src src/$(subst .,/,$@).java

$(TARGETS):
	javac -d ./ -sourcepath src src/$@.java
	java -jar tests/$@Test.jar

$(SOURCE_TESTS):
	javac -d ./ -sourcepath src src/$(subst .,/,$@).java
	java -ea $@

clean:
	rm -d -rf base expression


search_BinarySearchMissing:
	javac -d ./ -sourcepath src src/search/BinarySearchMissing.java
	java -jar tests/BinarySearchMissingTest.jar

queue_ArrayQueueDeque:
	javac -d ./ -sourcepath src src/queue/ArrayQueue.java
	javac -d ./ -sourcepath src src/queue/ArrayQueueADT.java
	javac -d ./ -sourcepath src src/queue/ArrayQueueModule.java
	java -ea -jar tests/ArrayQueueDequeTest.jar

queueAll:
	javac -d ./ -sourcepath src src/queue/ArrayQueue.java
	javac -d ./ -sourcepath src src/queue/LinkedQueue.java
	java -ea -jar tests/QueueFunctionsTest.jar

testWordStat:
	javac -d ./ -sourcepath src src/WordStatLineIndex.java
	java WordStatLineIndex "in.txt" "out.txt"

expression: expression.ExpressionTest expression.DoubleExpressionTest expression.TripleExpressionTest

parser: expression.parser.ParserTest expression.parser.ParserBitwiseTest expression.parser.ParserNotCountTest

exc_parser: expression.exceptions.ExceptionsMinMaxTest expression.exceptions.ExceptionsPowLog2Test

testParser:
	javac -d ./ -sourcepath src src/expression/exceptions/Main.java
	java expression.exceptions.Main "1000000*x*x*x*x*x/(x-1)"
