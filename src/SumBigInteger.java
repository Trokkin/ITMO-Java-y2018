import java.math.BigInteger;

public class SumBigInteger {
	public static void main(String[] args) {
		StringBuilder builder = new StringBuilder();
		for (String s : args) {
			builder.append(s);
			builder.append(" ");
		}
		String resString = builder.toString();

		resString = resString.replaceAll("[^\\d-]+", " ").trim();
		BigInteger res = BigInteger.ZERO;

		if (resString.length() > 0) {
			for (var s : resString.split(" ")) {
				res = res.add(new BigInteger(s));
			}
		}

		System.out.println(res);
	}
}