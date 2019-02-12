import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class ReverseMin {
	public static void main(String[] args) {
		ArrayList<Integer> sizes = new ArrayList<>();
		ArrayList<Integer> horMin = new ArrayList<>();
		ArrayList<Integer> verMin = new ArrayList<>();
		try {
			Scanner sc = new Scanner(System.in);
			while (sc.hasNextLine()) {
				ArrayList<String> v = sc.nextLine();
				int min = Integer.MAX_VALUE;
				int i = 0;
				for (String s : v) {
					int val = Integer.parseInt(s);
					min = Integer.min(min, val);
					if (verMin.size() == i) {
						verMin.add(val);
					} else {
						verMin.set(i, Integer.min(val, verMin.get(i)));
					}
					i++;
				}
				horMin.add(min);
				sizes.add(v.size());
			}
			sc.close();
		} catch (FileNotFoundException e) {
			System.err.println("FileNotFoundException");
		} catch (IOException e) {
			System.err.println("IOException");
		}
		for (int i = 0; i < sizes.size(); i++) {
			for (int j = 0; j < sizes.get(i); j++) {
				System.out.print(Integer.min(horMin.get(i), verMin.get(j)));
				System.out.print(" ");
			}
			System.out.println();
		}
	}
}