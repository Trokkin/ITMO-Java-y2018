import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Map;

public class WordStatIndex {
	public static void main(String[] args) {
		Scanner sc = null;
		PrintWriter out = null;

		try {
			sc = new Scanner(new File(args[0]));
			LinkedHashMap<String, ArrayList<Integer>> counter = new LinkedHashMap<>();
			int count = 0;


			while (sc.hasNextLine()) {
				for (String s : sc.nextLine()) {
					s = s.toLowerCase();
					ArrayList<Integer> list = counter.getOrDefault(s, null);
					if(list == null) {
						list = new ArrayList<>();
						counter.put(s, list);
					}
					list.add(++count);
				}
			}

			// while (sc.hasNext()) {
			// 	String s = sc.next().toLowerCase();
			// 	System.err.println(s);
			// 	ArrayList<Integer> list;
			// 	if(counter.containsKey(s)) {
			// 		list = counter.get(s);
			// 	} else {
			// 		list = new ArrayList<>();
			// 		counter.put(s, list);
			// 	}
			// 	list.add(++count);
			// }

			out = new PrintWriter(new File(args[1]));
			for (Map.Entry<String, ArrayList<Integer>> e : counter.entrySet()) {
				ArrayList<Integer> list = e.getValue();
				out.print(e.getKey() + " " + list.size());
				for (int i : list) {
					out.print(" " + i);
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
				try{
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