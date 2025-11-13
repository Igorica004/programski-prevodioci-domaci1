package parser;

import java.util.List;
import lexer.token.Token;
import lexer.token.TokenType;
import static lexer.token.TokenType.*;

public final class RecognizerParser {
    private final List<Token> tokens;
    private int cur = 0;

    public RecognizerParser(List<Token> tokens) {
        this.tokens = tokens;
    }

    // namespace = NAMESPACE IDENT SEMICOL
    //          {proc_decl|enum_decl|struct_decl}
    //          var_blok
    //          main_blok
    //          EOF;
    public void parseProgram() {
        consume(NAMESPACE,"expected NAMESPACE");
        consume(IDENT,"expected identifier");
        consume(SEMICOL, "expected ';'" );
        while(true)
        {
            if(check(PROC)) parseProcDecl();
            else if(check(STRUCT)) parseStructDecl();
            else if(check(ENUM)) parseEnumDecl();
            else if (check(VAR_BLOCK)) break;
            else error(peek(),"Error");
        }
        consume(VAR_BLOCK,"expected VAR_BLOCK");
        parseVarBlock();
        consume(MAIN,"expected MAIN");
        parseMain();
        consume(EOF, "expected EOF");
    }

    // proc_decl = PROC [type] IDENT LPAREN [type IDENT {SEMICOL type IDENT}] RPAREN block
    private void parseProcDecl(){
        consume(PROC,"expected PROC");
        if(!match(IDENT)) parseType();
        consume(IDENT,"expected IDENT");
        consume(LPAREN,"expected LPAREN");
        if(!match(RPAREN))
        {
            do {
                parseType();
                consume(IDENT, "identifier expected");
            } while (match(SEMICOL));
        }
        consume(RPAREN,"expected RPAREN");
        parseVarBlock();
        parseBlock();
    }

    // struct_decl = STRUCT IDENT LBRACE {type IDENT SEMICOL} RBRACE
    private void parseStructDecl(){
        consume(STRUCT,"expected STRUCT");
        consume(IDENT,"expected IDENT");
        consume(LBRACE,"expected '{");
        while(!match(RBRACE))
        {
            parseType();
            consume(IDENT,"identifier expected");
            consume(SEMICOL, "expected SEMICOL");
        }
    }

    // enum_decl = ENUM IDENT LBRACE IDENT SEMICOL {IDENT SEMICOL} RBRACE
    private void parseEnumDecl(){
        consume(ENUM,"expected ENUM");
        consume(IDENT,"identifier expected");
        consume(LBRACE,"expected '{");
        do {
            consume(IDENT, "identifier expected");
            consume(SEMICOL, "expected SEMICOL");
        } while (!match(RBRACE));
    }

    // var_blok = VAR_BLOCK LBRACE {var_decl} RBRACE
    private void parseVarBlock(){
        consume(VAR_BLOCK,"expected VAR_BLOCK");
        consume(LBRACE,"expected '{'");
        while(!match(RBRACE)) parseVarDecl();
    }

    // var_decl = VAR type IDENT [ASSIGN expr] SEMICOL;
    private void parseVarDecl(){
        consume(VAR, "expected VAR");
        parseType();
        consume(IDENT, "identifier expected");
        if(match(ASSIGN)) parseExpr();
        consume(SEMICOL,"expected ';'");
    }

    // main_blok = MAIN LBRACE {stmt} RBRACE;
    private void parseMain(){
        consume(MAIN,"expected MAIN");
        consume(LBRACE,"expected '{'");
        while(!match(RBRACE)) parseStmt();
    }

    //type = (INT|FLOAT|CHAR|STRING|BOOL|IDENT) {LBRACK INT_LIT RBRACK} todo moze li samo INT_LIT bez npr niz[4+2*5^3]
    private void parseType() {
        if (!match(INT,FLOAT,CHAR,STRING,BOOL,IDENT)) error(peek(),"Error");
        while (check(LBRACK))
        {
            consume(LBRACK, null);
            if(!match(FLOAT_LIT,CHAR_LIT,STRING_LIT,BOOL_LIT,LBRACE,NOT))
            {
                parseAdd(); // sta ako funkcija vraca string
                consume(RBRACK, "expected ']'");
            }
            else error(peek(), "Can not define array dimension");
        }
    }

    //block = LBRACE {stmt} RBRACE
    private void parseBlock() {
        consume(LBRACE, "expected '{'");
        while(!match(RBRACE)) parseStmt();
    }

    //stmt = dodela_stmt
    //          | if_stmt
    //          | for_stmt
    //          | (BREAK SEMICOL)
    //          | (CONT SEMICOL)
    //          | (RETURN expr SEMICOL)
    //          | proc_stmt;
         private void parseStmt() {
        if (check(IDENT))
        {
            if(checkNext(LPAREN)) parseProcStmt();
            else parseDodelaStmt();
        }
        else if (match(IF)) parseIfStmt();
        else if (match(FOR)) parseForStmt();
        else if (match(BREAK)) consume(SEMICOL,"expected ';'");
        else if (match(RETURN))
        {
            parseExpr();
            consume(SEMICOL,"expected ';'");
        }
        else if (match(CONT)) consume(SEMICOL,"expected ';'");

    }

    // if_stmt = IF LPAREN expr RPAREN THEN block [ELSE block]
    private void parseIfStmt() {
        consume(LPAREN, "expected '('");
        parseExpr();
        consume(RPAREN, "expected ')'");
        consume(THEN, "expected pa");
        parseBlock();
        if(match(ELSE)) parseBlock();
    }

    // for_stmt = FOR LPAREN dodela_stmt|(IDENT SEMICOL) expr SEMICOL dodela_stmt RPAREN block
    private void parseForStmt() {
        consume(LPAREN, "expected '('");
        if(checkNext(SEMICOL)) consume(IDENT, "expected identifier");
        else parseDodelaStmt();
        parseExpr();
        consume(SEMICOL, "expected ';'");
        parseDodelaStmt();
        consume(RPAREN, "expected ')'");
        parseBlock();
    }

    // dodela_stmt = lvalue ASSIGN expr SEMICOL
    private void parseDodelaStmt() {
        if(match(IDENT)) {
            if (match(LBRACK)) {
                parseExpr();
                consume(RBRACK, "expected ']'");
            }
            else
            {
                while(match(DOT)) consume(IDENT, "expected identifier");
            }
        }
        consume(ASSIGN, "expected ASSIGN");
        parseExpr();
        consume(SEMICOL, "expected ';'");
    }

    // proc_stmt = IDENT LPAREN [expr {SEMICOL expr}] RPAREN SEMICOL
    private void parseProcStmt(){
        consume(IDENT,"expected identifier");
        consume(LPAREN, "expected '('");
        if(!check(RPAREN))
        {
            do parseExpr();
            while (match(SEMICOL));
        }
        consume(RPAREN, "expected ')'");
        consume(SEMICOL, "expected ';'");
    }

    // expr = or_expr
    private void parseExpr() {
        parseOrExpr();
    }

    // or_expr = and_expr {OR and_expr}
    private void parseOrExpr() {
        do parseAndExpr();
        while (match(OR));
    }

    // and_expr = rel_expr {AND rel_expr}
    private void parseAndExpr() {
        do parseRelExpr();
        while (match(AND));
    }

    // rel_expr = add [(EQ|NEQ|LT|GT|LTE|GTE) add];
    private void parseRelExpr() {
        parseAdd();
        if(match(EQ,NEQ,LT,GT,LTE,GTE)) parseAdd();
    }

    // add = mul {(ADD|SUBTRACT) mul};
    private void parseAdd(){
        do parseMul();
        while (match(ADD, SUBTRACT));
    }

    // mul = unary {(MULTIPLY|DIVIDE|PERCENT) unary}
    private void parseMul(){
        do parseUnary();
        while (match(MULTIPLY, DIVIDE, PERCENT));
    }

    // unary = [ADD|SUBTRACT] power
    private void parseUnary(){
        match(SUBTRACT, ADD);
        parsePower();
    }

    // power = primary [POW power]
    private void parsePower() {
        parsePrimary();
        if(match(POW)) parsePower();
    }

    //primary = literal
    //       | lvalue
    //       | struct_lit
    //       | LPAREN expr RPAREN
    //       | NOT primary
    //       | proc_expr;
    private void parsePrimary() {
        if(match(IDENT))
        {
            if(match(LBRACK))
            {
                parseExpr();
                consume(RBRACK, "expected ']'");
            }
            else if(match(DOT)) consume(IDENT, "expected identifier");
            else if(match(LPAREN))
            {
                do parseExpr();
                while (match(SEMICOL));
                consume(RPAREN, "expected ')'");
            }
        }
        else if(match(LBRACE)) parseStructLit();
        else if(match(NOT)) parsePrimary();
        else if(match(LPAREN))
        {
            parseExpr();
            consume(RPAREN, "expected ')'");
        }
        else if(!match(INT_LIT,FLOAT_LIT,CHAR_LIT,STRING_LIT,BOOL_LIT)) error(peek(),"Error");
    }

    //struct_lit = LBRACE expr {SEMICOL expr} RBRACE;
    private void parseStructLit() {
        consume(LBRACE, "expected '{'");
        do parseExpr();
        while (match(SEMICOL));
        consume(RBRACE, "expected '}'");
    }

    private boolean match(TokenType... types) {
        for (TokenType t : types) {
            if (check(t)) { advance(); return true; }
        }
        return false;
    }

    private void consume(TokenType type, String message) {
        if (check(type)) {
            advance();
            return;
        }
        error(peek(), message);
        throw new ParseError(message);
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return type == EOF;
        return peek().type == type;
    }

    private boolean checkNext(TokenType type) {
        if (cur + 1 >= tokens.size()) return false;
        return tokens.get(cur + 1).type == type;
    }

    private void advance() {
        if (!isAtEnd()) cur++;
    }

    private boolean isAtEnd() {
        return peek().type == EOF;
    }

    private Token peek() {
        return tokens.get(cur);
    }

    private void error(Token token, String message) {
        String where = token.type == EOF ? " at end" : " at '" + token.lexeme + "'";
        throw new ParseError("PARSER ERROR > Error" + where + ": " + message + " (line: " + token.line + ", col: " + token.colStart + ")");
    }

    public static final class ParseError extends RuntimeException {
        public ParseError(String s) { super(s); }
    }
}
