import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Map;

class Pair {
	int line;
	int ix;

	Pair(int line, int ix) {
		this.line = line;
		this.ix = ix;
	}
}

public class WordStatLineIndex {
	public static void main(String[] args) {
		Scanner sc = null;
		PrintWriter out = null;

		try {
			sc = new Scanner(new File(args[0]));
			TreeMap<String, ArrayList<Pair>> counter = new TreeMap<>();
			int countLine = 0;

			while (sc.hasNextLine()) {
				countLine++;
				int countWord = 0;
				for (String s : sc.nextLine()) {
					s = s.toLowerCase();
					ArrayList<Pair> list = counter.getOrDefault(s, null);
					if (list == null) {
						list = new ArrayList<>();
						counter.put(s, list);
					}
					list.add(new Pair(countLine, ++countWord));
				}
			}

			out = new PrintWriter(new File(args[1]));
			for (Map.Entry<String, ArrayList<Pair>> e : counter.entrySet()) {
				ArrayList<Pair> list = e.getValue();
				out.print(e.getKey() + " " + list.size());
				for (Pair i : list) {
					out.print(" " + i.line + ":" + i.ix);
				}
				out.println();
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
				try {
					sc.close();
				} catch (IOException e) {
					System.out.println("IOException");
				}
			}
			if (out != null) {
				out.close();
			}
		}
	}
}