package search;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class BinarySearchMissing {

	// pre: a[i] >= a[i+1] for i in [0, size - 1)
	// post: if a.contains(key): R = i, a[i - 1] > key = a[i]
	// post: else: R = -(i + 1), a[i - 1] > key > a[i]
	public static int binarySearch0Recursive(int[] a, int fromIndex, int toIndex, int key) {
		// inv: a[-1..fromIndex - 1] > key >= a[toIndex..size]
		int l = fromIndex;
		int h = toIndex - 1;

		if (l > h) {
			return -(l + 1);
		}
		// l <= h
		int m = (l + h) / 2;
		// l <= m <= h
		if (a[m] == key) {
			// key == a[m]
			if (m > 0 && a[m - 1] == a[m]) {
				// a[m - 1] == key
				// a[-1..l] > key >= a[m..size]
				return binarySearch0Recursive(a, l, m, key);
			} else {
				// a[m - 1] > key = a[m]
				return m;
			}
		} else if (a[m] > key) {
			// key < a[m]
			// a[-1..m] > key > a[r..size]
			return binarySearch0Recursive(a, m + 1, h + 1, key);
		} else {
			// key > a[m]
			// a[-1..l] > key > a[m..size]
			return binarySearch0Recursive(a, l, m, key);
		}
	}

	// pre: a[i] < a[i+1] for i in [0, size - 1)
	// post: if a.contains(key): R = i, a[i] = key
	// post: else: R = i, a[-i - 2] < key < a[-i - 1]
	public static int binarySearch0(int[] a, int fromIndex, int toIndex, int key) {
		// inv: a[-1..fromIndex - 1] > key >= a[toIndex..size]
		int l = fromIndex;
		int h = toIndex - 1;

		while (l <= h) {
			// l <= h
			int m = (l + h) / 2;
			// l <= m <= h
			if (a[m] == key) {
				// key == a[m]
				if (m > 0 && a[m - 1] == a[m]) {
					// a[m - 1] == key
					// a[-1..l] > key >= a[m..size]
					h = m - 1;
				} else {
					// a[m - 1] > key = a[m]
					return m;
				}
			} else if (a[m] > key) {
				// key < a[m]
				// a[-1..m] > key > a[r..size]
				l = m + 1;
			} else {
				// key > a[m]
				// a[-1..l] > key > a[m..size]
				h = m - 1;
			}
		}
		// l > h
		return -(l + 1);
	}

	public static int binarySearch(int[] a, int key) {
		return binarySearch0(a, 0, a.length, key);
	}

	public static void main(String[] args) {
		int[] a = new int[args.length - 1];
		for (int i = 1; i < args.length; i++) {
			a[i - 1] = Integer.parseInt(args[i]);
		}
		System.out.println(binarySearch(a, Integer.parseInt(args[0])));
	}
}