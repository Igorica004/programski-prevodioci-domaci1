package application;

import lexer.Lekser;
import lexer.token.Token;
import lexer.token.TokenFormatter;
import parser.RecognizerParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Application {

    public static void main(String[] args) {

        if (args.length != 1) {
            System.err.println("Usage: java main.Application <source-file>");
            System.exit(64);
        }

        try {
            String code = Files.readString(Path.of(args[0]));
            Lekser lexer = new Lekser(code);
            List<Token> tokens = lexer.scanTokens();
            System.out.println(TokenFormatter.formatList(tokens));

            RecognizerParser rp = new RecognizerParser(tokens);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }



    }
}
