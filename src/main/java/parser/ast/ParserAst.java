package parser.ast;

import lexer.token.Token;

import java.util.List;

import static lexer.token.TokenType.EOF;

public final class ParserAst {
    private final List<Token> tokens;
    private int current = 0;
    public ParserAst(List<Token> tokens) { this.tokens = tokens; }

    // Ovo je valjda koren stabla, objekat koji sadrzi sve druge objekte.
    public Ast.Program parseProgram() {

        return null;
    }
}
