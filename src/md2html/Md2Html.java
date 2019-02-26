package md2html;

import java.io.IOException;

public class Md2Html {
    public static void main(String[] args) {
        try {
            MarkdownParser2 parser = new MarkdownParser2(args[0]);
            parser.parse(args[1]);
        } catch (IOException e) {
            System.out.println("I/O error");
        }
    }
}
