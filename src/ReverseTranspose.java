import java.util.Scanner;
import java.util.ArrayList;

public class ReverseTranspose {
	public static void main(String[] args) {
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();

		Scanner sc = new Scanner(System.in);
		while (sc.hasNextLine()) {
			int j = 0;
			for (String s : sc.nextLine().split(" ")) {
				if (list.size() == j) {
					list.add(new ArrayList<String>());
				}
				list.get(j++).add(s);
			}
		}
		sc.close();

		for (ArrayList<String> v : list) {
			for (String s : v) {
				if (s.length() > 0) {
					System.out.printf("%s ", s);
				}
			}
			System.out.println();
		}
	}
}