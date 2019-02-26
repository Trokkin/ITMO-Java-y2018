package md2html;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class MarkdownParser2 {
    private BufferedReader reader;
    private PrintWriter writer;
    private Map<Character, Integer> symbols = new HashMap<>();
    private Map<Character, String> tags = new HashMap<>() {{
        put('`', "code");
        put('*', "em");
        put('_', "em");
        put('S', "strong");
        put('-', "s");
        put('+', "u");
        put('~', "mark");
    }};
    private ArrayList<String> paragraph = new ArrayList<>();

    public MarkdownParser2(final String inputFileName) throws IOException {
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFileName), StandardCharsets.UTF_8));
        refresh();
    }

    public void parse(final String outputFileName) throws IOException {
        writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFileName), StandardCharsets.UTF_8));
        String str;
        while ((str = reader.readLine()) != null) {
            if (str.isEmpty()) {
                printParagraph();
                paragraph.clear();
            } else {
                paragraph.add(str);
            }

        }
        printParagraph();
        reader.close();
        writer.close();
    }

    private void printParagraph() {
        if (paragraph.isEmpty()) {
            return;
        }
        int paragraphLevel = headerLevel(paragraph.get(0));
        if (paragraphLevel > 0) {
            paragraph.set(0, clearHeaderSymbols(paragraph.get(0), paragraphLevel));
            writer.write("<h" + paragraphLevel + ">");
            parseParagraph();
            writer.write("</h" + paragraphLevel + ">");
        } else {
            writer.write("<p>");
            parseParagraph();
            writer.write("</p>");
        }
        writer.write(System.lineSeparator());
    }

    private void preprocess() {
        for (String s : paragraph) {
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                switch (c) {
                    case '-':
                    case '+':
                        if (checkNextSymbol(s, i, c)) {
                            i++;
                            symbols.put(c, symbols.get(c) + 1);
                        }
                        break;
                    case '*':
                    case '_':
                        symbols.put(c, symbols.get(c) + 1);
                        break;
                    case '\\':
                        i++;
                        break;
                }
            }
        }
    }

    private void parseParagraph() {
        refresh();
        preprocess();
        StringBuilder outLine = new StringBuilder();
        Stack<Character> symbolStack = new Stack<>();
        int pos = 0;
        for (String s : paragraph) {
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                boolean chk = !symbolStack.empty() && symbolStack.peek() == c;
                switch (c) {
                    case '`':
                    case '~':
                        outLine.append(makeTag(tags.get(c), chk));
                        break;
                    case '-':
                    case '+':
                        if (checkNextSymbol(s, i, c)) {
                            outLine.append(makeTag(tags.get(c), chk));
                            i++;
                            symbols.put(c, symbols.get(c) - 1);
                        } else {
                            outLine.append(c);
                        }
                        break;
                    case '*':
                    case '_':
                        if (symbols.get(c) == 0) {
                            break;
                        }
                        if (checkNextSymbol(s, i, c)) {
                            symbols.put(c, symbols.get(c) - 2);
                            c = 'S';  // strong
                            chk = !symbolStack.empty() && symbolStack.peek() == c;
                            outLine.append(makeTag(tags.get('S'), chk));
                            i++;
                        } else {
                            if (symbols.get(c) == 1 && !chk) {
                                outLine.append(c);
                                continue;
                            } else {
                                outLine.append(makeTag(tags.get(c), chk));
                                symbols.put(c, symbols.get(c) - 1);
                            }
                        }
                        break;
                    case '\\':
                        // Skip
                        break;
                    case '<':
                        outLine.append("&lt;");
                        break;
                    case '>':
                        outLine.append("&gt;");
                        break;
                    case '&':
                        outLine.append("&amp;");
                        break;
                    default:
                        outLine.append(c);
                        break;
                }
                if (tags.containsKey(c)) {
                    if (!chk) {
                        symbolStack.push(c);
                    } else {
                        symbolStack.pop();
                    }
                }
            }
            writer.write(outLine.toString());
            if (pos < paragraph.size() - 1) {
                writer.write(System.lineSeparator());
            }
            outLine.setLength(0);
            pos++;
        }
    }

    private String clearHeaderSymbols(String s, int size) {
        return new StringBuilder(s).delete(0, size + 1).toString();
    }

    private String makeTag(String s, boolean closing) {
        StringBuilder builder = new StringBuilder();
        builder.append('<');
        if (closing) {
            builder.append('/');
        }
        return builder.append(s).append('>').toString();
    }

    private boolean checkNextSymbol(String s, int pos, char c) {
        return pos + 1 < s.length() && s.charAt(pos + 1) == c;
    }

    private int headerLevel(String s) {
        int level = 0;
        while (s.charAt(level) == '#') {
            level++;
        }
        if (!Character.isWhitespace(s.charAt(level))) {
            return 0;
        }
        return level;
    }


    private void refresh() {
        symbols.put('*', 0);
        symbols.put('_', 0);
        symbols.put('-', 0);
        symbols.put('+', 0);
    }
}
