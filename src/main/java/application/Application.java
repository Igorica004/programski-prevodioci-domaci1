package application;

import com.fasterxml.jackson.databind.JsonNode;
import lexer.Lekser;
import lexer.token.Token;
import lexer.token.TokenFormatter;
import parser.RecognizerParser;
import parser.ast.Ast;
import parser.ast.JsonAstPrinter;
import parser.ast.ParserAst;

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

            new RecognizerParser(tokens).parseProgram();

            ParserAst parser = new ParserAst(tokens);

            String json = new JsonAstPrinter().print(parser.parseProgram());
            Files.writeString(Path.of("src/main/resources/OutJson.json"), json);


        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }



    }
}
