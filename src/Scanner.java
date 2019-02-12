import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class Scanner {
	InputStream is;
	StringBuilder buffer = new StringBuilder();
	ArrayList<String> bufferLine = new ArrayList<>();
	ArrayList<String> line = null;
	CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();

	public Scanner(File inputFile) throws FileNotFoundException {
		is = new FileInputStream(inputFile);
	}

	public Scanner(InputStream inputStream) {
		is = inputStream;
	}

	public boolean hasNextLine() throws IOException {
		while (is != null && line == null) {
			readChar();
		}
		return line != null;
	}

	public ArrayList<String> nextLine() throws IOException {
		while (is != null && line == null) {
			readChar();
		}
		ArrayList<String> temp = line;
		line = null;
		return temp;
	}

	private void flushWord() {
		if (buffer.length() > 0) {
			bufferLine.add(buffer.toString());
			buffer.setLength(0);
		}
	}

	private void flushLine() {
		line = bufferLine;
		bufferLine = new ArrayList<>();
	}

	static final int BUFFER_SIZE = 256;
	byte[] bbuf = new byte[BUFFER_SIZE];
	int bytePos = 0;
	int lenBytes = BUFFER_SIZE;
	char[] cbuf = new char[BUFFER_SIZE];
	int ix = BUFFER_SIZE;
	int len = BUFFER_SIZE;

	private int nextChar() throws IOException {
		if (ix >= len) {
			lenBytes = 0;
			while (lenBytes == 0) {
				lenBytes = is.read(bbuf, bytePos, BUFFER_SIZE - bytePos);
				if (lenBytes < 0) {
					return -1;
				}
			}
			lenBytes += bytePos;

			ByteBuffer bb = ByteBuffer.wrap(bbuf, 0, lenBytes);
			CharBuffer cb = CharBuffer.wrap(cbuf);

			CoderResult res = decoder.decode(bb, cb, false);

			bytePos = lenBytes - bb.position();
			System.arraycopy(bbuf, bb.position(), bbuf, 0, bytePos);
			len = cb.position();
			ix = 0;
		}
		return cbuf[ix++];
	}

	private void readChar() throws IOException {
		int ch = nextChar();
		if (ch < 0) {
			close();
		}
		if (Character.isLetter(ch) || Character.getType(ch) == Character.DASH_PUNCTUATION || ch == '\'') {
			buffer.append((char) ch);
		} else {
			flushWord();
			if (ch == '\n') {
				flushLine();
			}
		}
	}

	public void close() throws IOException {
		if (is != null) {
			is.close();
			is = null;
		}
	}
}