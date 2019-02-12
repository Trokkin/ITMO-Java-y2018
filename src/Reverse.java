import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

// class Scanner {
// 	private InputStream is;
// 	private ByteArrayOutputStream buffer = new ByteArrayOutputStream();
// 	// private StringBuilder buffer = new StringBuilder();
// 	private String token = null;

// 	public Scanner(File inputFile) {
// 		try {
// 			is = new FileInputStream(inputFile);
// 		} catch (FileNotFoundException e) {
// 			System.err.println("FileNotFoundException");
// 		}
// 	}

// 	public Scanner(InputStream inputStream) {
// 		is = inputStream;
// 	}

// 	private void flush() {
// 		token = buffer.toString();
// 		buffer.reset();
// 	}

// 	private static final int BUFFER_SIZE = 16;
// 	private byte[] bbuf = new byte[BUFFER_SIZE];
// 	private int ix = BUFFER_SIZE;
// 	private int len = BUFFER_SIZE;

// 	private void readBuffer() {
// 		len = 0;
// 		while(len == 0) {
// 			try {
// 				len = is.read(bbuf);
// 			} catch (IOException e) {
// 				System.err.println("IOException");
// 			}
// 		}
// 		ix = 0;
// 	}

// 	private void retrieveLine() {
// 		skippedLineBreaks = 0;
// 		nextSkippedLineBreaks = 1;
// 		int i = ix;
// 		while (true) {
// 			if (i >= len) {
// 				buffer.write(bbuf, ix, i - ix);
// 				readBuffer();
// 				i = ix;
// 				if (len == -1) {
// 					close();
// 					return;
// 				}
// 			}
// 			if (bbuf[i] == '\n') {
// 				buffer.write(bbuf, ix, i - ix);
// 				ix = i + 1;
// 				flush();
// 				return;
// 			}
// 			i++;
// 		}
// 	}

// 	private int skippedLineBreaks = 0;
// 	private int nextSkippedLineBreaks = 0;


// 	public int getSkippedLineBreaks() {
// 		return skippedLineBreaks;
// 	}

// 	private void retrieveToken() {
// 		skippedLineBreaks = nextSkippedLineBreaks;
// 		nextSkippedLineBreaks = 0;
// 		int i = ix;
// 		while (true) {
// 			if (i >= len) {
// 				buffer.write(bbuf, ix, i - ix);
// 				readBuffer();
// 				i = ix;
// 				if (len == -1) {
// 					close();
// 					return;
// 				}
// 			}
// 			if (Character.isWhitespace(bbuf[i])) {
// 				if (bbuf[i] == '\n') {
// 					skippedLineBreaks++;
// 				}
// 				if (i - ix > 0) {
// 					if (bbuf[i] == '\n') {
// 						skippedLineBreaks--;
// 						nextSkippedLineBreaks++;
// 					}
// 					buffer.write(bbuf, ix, i - ix);
// 					ix = i + 1;
// 					flush();
// 					return;
// 				}
// 				ix = i;
// 			}
// 			i++;
// 		}
// 	}

// 	public boolean hasNextLine() {
// 		if (is != null && token == null) {
// 			retrieveLine();
// 		}
// 		return token != null;
// 	}

// 	public boolean hasNext() {
// 		if (is != null && token == null) {
// 			retrieveToken();
// 		}
// 		return token != null;
// 	}

// 	public boolean hasNextInt() {
// 		if (hasNext()) {
// 			if (token == null) {
// 				return false;
// 			}
// 			int length = token.length();
// 			if (length == 0) {
// 				return false;
// 			}
// 			int i = 0;
// 			if (token.charAt(0) == '-') {
// 				if (length == 1) {
// 					return false;
// 				}
// 				i = 1;
// 			}
// 			for (; i < length; i++) {
// 				char c = token.charAt(i);
// 				if (c < '0' || c > '9') {
// 					return false;
// 				}
// 			}
// 			return true;
// 		}
// 		return false;
// 	}

// 	private String nextToken() {
// 		String temp = token;
// 		token = null;
// 		return temp;
// 	}

// 	public String nextLine() {
// 		hasNextLine();
// 		return nextToken();
// 	}

// 	public String next() {
// 		hasNext();
// 		return nextToken();
// 	}

// 	public int nextInt() {
// 		if (hasNextInt()) {
// 			return Integer.parseInt(nextToken());
// 		}
// 		return 0;
// 	}

// 	public void close() {
// 		if (buffer != null) {
// 			if (buffer.size() > 0) {
// 				flush();
// 			}
// 			try {
// 				buffer.close();
// 			} catch (IOException e) {
// 				System.err.println("IOException");
// 			}
// 			buffer = null;
// 		}
// 		if (is != null) {
// 			try {
// 				is.close();
// 			} catch (IOException e) {
// 				System.err.println("IOException");
// 			}
// 			is = null;
// 		}
// 	}
// }

public class Reverse {
	public static void main(String[] args) {
		ArrayList<ArrayList<Integer>> list = new ArrayList<>();

		Scanner sc = new Scanner(System.in);
		// Scanner sc = new Scanner(new File("in.txt"));
		ArrayList<Integer> line = new ArrayList<>();
		while (sc.hasNext()) {
			if(!sc.hasNextInt()) {
				continue;
			}
			int a = sc.nextInt();
			for (int i = sc.getSkippedLineBreaks(); i > 0; i--) {
				list.add(line);
				line = new ArrayList<>();
				System.err.println();
			}
			line.add(a);
			System.err.print(a);
			System.err.print(' ');
		}
		if (line.size() > 0) {
			list.add(line);
		}
		sc.close();

		for (int i = list.size() - 1; i >= 0; i--) {
			ArrayList<Integer> v = list.get(i);
			for (int j = v.size() - 1; j >= 0; j--) {
				System.out.print(v.get(j));
				System.out.print(" ");
			}
			System.out.println();
		}
	}
}