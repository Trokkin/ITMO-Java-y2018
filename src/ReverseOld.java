import java.util.Scanner;
import java.util.ArrayList;

public class Reverse {
	public static void main(String[] args) {
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		String line;
		Scanner sc = new Scanner(System.in);
		while (sc.hasNextLine()) {
			line = sc.nextLine();

			ArrayList<String> v = new ArrayList<String>();
			for (String s : line.split(" ")) {
				v.add(s);
			}

			list.add(v);
		}
		sc.close();

		for (int i = list.size() - 1; i >= 0; i--) {
			ArrayList<String> v = list.get(i);
			for (int j = v.size() - 1; j >= 0; j--) {
				System.out.printf("%s ", v.get(j));
			}
			System.out.println();
		}
	}
}