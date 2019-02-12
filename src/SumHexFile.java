import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class SumHexFile {
	public static void main(String[] args) {
		Scanner sc = null;
		PrintWriter out = null;

		try {
			sc = new Scanner(new File(args[0]));

			int res = 0;
			while (sc.hasNextLine()) {
				String resString = sc.nextLine().toLowerCase().replaceAll("[^\\d-xa-f]+", " ").trim();
				if (resString.length() > 0) {
					for (String s : resString.split(" ")) {
						if (s.startsWith("0x")) {
							res += Integer.parseUnsignedInt(s.substring(2), 16);
						} else {
							res += Integer.parseInt(s);
						}
					}
				}
			}

			out = new PrintWriter(new File(args[1]));
			out.println(res);

		} catch (SecurityException e) {
			System.out.println("SecurityException");
		} catch (InputMismatchException e) {
			System.out.println("InputMismatchException");
		} catch (IndexOutOfBoundsException e) {
			System.out.println("IndexOutOfBoundsException");
		} catch (NumberFormatException e) {
			System.out.println("NumberFormatException");
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException");
		} catch (NullPointerException e) {
			System.out.println("NullPointerException");
		} finally {
			if (sc != null) {
				sc.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}
}