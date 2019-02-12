import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

class Scanner {
	private InputStream is;
	private ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	// private StringBuilder buffer = new StringBuilder();
	private String token = null;

	public Scanner(File inputFile) {
		try {
			is = new FileInputStream(inputFile);
		} catch (FileNotFoundException e) {
			System.err.println("FileNotFoundException");
		}
	}

	public Scanner(InputStream inputStream) {
		is = inputStream;
	}

	private void flush() {
		token = buffer.toString();
		buffer.reset();
	}

	private static final int BUFFER_SIZE = 16;
	private byte[] bbuf = new byte[BUFFER_SIZE];
	private int ix = BUFFER_SIZE;
	private int len = BUFFER_SIZE;

	private void readBuffer() {
		len = 0;
		while(len == 0) {
			try {
				len = is.read(bbuf);
			} catch (IOException e) {
				System.err.println("IOException");
			}
		}
		ix = 0;
	}

	private void retrieveLine() {
		int i = ix;
		while (true) {
			if (i >= len) {
				buffer.write(bbuf, ix, i - ix);
				readBuffer();
				i = ix;
				if (len == -1) {
					close();
					return;
				}
			}
			if (bbuf[i] == '\n') {
				buffer.write(bbuf, ix, i - ix);
				ix = i + 1;
				flush();
				return;
			}
			i++;
		}
	}
	
	private void retrieveToken() {
		int i = ix;
		while (true) {
			if (i >= len) {
				buffer.write(bbuf, ix, i - ix);
				readBuffer();
				i = ix;
				if (len == -1) {
					close();
					return;
				}
			}
			if (Character.isWhitespace(bbuf[i])) {
				if (i - ix > 0) {
					buffer.write(bbuf, ix, i - ix);
					ix = i + 1;
					flush();
					return;
				}
				ix = i;
			}
			i++;
		}
	}

	public boolean hasNextLine() {
		if (is != null && token == null) {
			retrieveLine();
		}
		return token != null;
	}

	public boolean hasNext() {
		if (is != null && token == null) {
			retrieveToken();
		}
		return token != null;
	}

	private String nextToken() {
		String temp = token;
		token = null;
		return temp;
	}

	public String nextLine() {
		hasNextLine();
		return nextToken();
	}

	public String next() {
		hasNext();
		return nextToken();
	}

	public void close() {
		if (buffer != null) {
			if (buffer.size() > 0) {
				flush();
			}
			try {
				buffer.close();
			} catch (IOException e) {
				System.err.println("IOException");
			}
			buffer = null;
		}
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				System.err.println("IOException");
			}
			is = null;
		}
	}
}