import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.TreeMap;
import java.util.Map;
import java.util.Scanner;

public class WordStatWords {
	public static void main(String[] args) {
		Scanner sc = null;
		PrintWriter out = null;

		try {
			sc = new Scanner(new File(args[0]));
			TreeMap<String, Integer> counter = new TreeMap<>();

			while (sc.hasNextLine()) {
				String resString = sc.nextLine().toLowerCase().replaceAll("[^'\\p{Pd}\\p{L}]+", " ").trim();
				for (String s : resString.split(" ")) {
					if(counter.containsKey(s)) {
						counter.put(s, counter.get(s) + 1);
					} else {
						counter.put(s, 1);
					}
				}
			}

			out = new PrintWriter(new File(args[1]));
			for (Map.Entry e : counter.entrySet()) {
				out.println(e.getKey() + " " + e.getValue());
			}

		} catch (SecurityException e) {
			System.out.println("SecurityException");
		} catch (ClassCastException e) {
			System.out.println("ClassCastException");
		} catch (NullPointerException e) {
			System.out.println("NullPointerException");
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException");
		} catch (IOException e) {
			System.out.println("IOException");
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