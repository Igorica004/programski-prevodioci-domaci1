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
        consume(NAMESPACE,"planiran program");
        consume(IDENT,"planirana promenljiva");
        consume(SEMICOL, "planiran ';'" );
        while(true)
        {
            if(check(PROC)) parseProcDecl();
            else if(check(STRUCT)) parseStructDecl();
            else if(check(ENUM)) parseEnumDecl();
            else if (check(VAR_BLOCK)) break;
            else error(peek(),"Error");
        }
        consume(VAR_BLOCK,"planiran promenljive blok");
        parseVarBlock();
        consume(MAIN,"planiran pocni blok");
        parseMain();
        consume(EOF, "planiran EOF");
        System.out.println("Program je parsirabilan");
    }

    // proc_decl = PROC [type] IDENT LPAREN [type IDENT {SEMICOL type IDENT}] [var_block] RPAREN block
    private void parseProcDecl() {
        consume(PROC, "planirana procedura");
        if (!check(IDENT)) parseType();
        consume(IDENT, "planirana promenljiva");
        consume(LPAREN, "planirana '('");
        if (!match(RPAREN)) {
            do {
                parseType();
                consume(IDENT, "planirana promenljiva");
            } while (match(SEMICOL));
        }
        consume(RPAREN, "planirana ')'");
        if (match(VAR_BLOCK)) parseVarBlock();
        parseBlock();
    }

    // struct_decl = STRUCT IDENT LBRACE {type IDENT SEMICOL} RBRACE
    private void parseStructDecl(){
        consume(STRUCT,"planiran postoji blok");
        consume(IDENT,"planirana promenljiva");
        consume(LBRACE,"planirana '{");
        while(!match(RBRACE))
        {
            parseType();
            consume(IDENT,"planirana promenljiva");
            consume(SEMICOL, "planirana ';'");
        }
    }

    // enum_decl = ENUM IDENT LBRACE IDENT SEMICOL {IDENT SEMICOL} RBRACE
    private void parseEnumDecl(){
        consume(ENUM,"planiran popis");
        consume(IDENT,"planirana promenljiva");
        consume(LBRACE,"planirana '{");
        do {
            consume(IDENT, "planirana promenljiva");
            consume(SEMICOL, "planirana ';'");
        } while (!match(RBRACE));
    }

    // var_blok = VAR_BLOCK LBRACE {var_decl} RBRACE
    private void parseVarBlock(){
        consume(LBRACE,"planirana '{'");
        while(!match(RBRACE)) parseVarDecl();
    }

    // var_decl = VAR type IDENT [ASSIGN expr] SEMICOL;
    private void parseVarDecl(){
        consume(VAR, "planirano postavi");
        parseType();
        consume(IDENT, "planirana promenljiva");
        if(match(ASSIGN)) parseExpr();
        consume(SEMICOL,"planirana ';'");
    }

    // main_blok = MAIN LBRACE {stmt} RBRACE;
    private void parseMain(){
        consume(LBRACE,"planirana '{'");
        while(!match(RBRACE)) parseStmt();
    }

    //type = (INT|FLOAT|CHAR|STRING|BOOL|IDENT) {LBRACK expr RBRACK}
    private void parseType() {
        if (!match(INT,FLOAT,CHAR,STRING,BOOL,IDENT)) error(peek(),"Error");
        while (check(LBRACK))
        {
            consume(LBRACK, "planirana '['");
            parseExpr();
            consume(RBRACK, "planirana ']'");
        }
    }

    //block = LBRACE {stmt} RBRACE
    private void parseBlock() {
        consume(LBRACE, "planirana '{'");
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
        else if (match(BREAK)) consume(SEMICOL,"planirana ';'");
        else if (match(RETURN))
        {
            parseExpr();
            consume(SEMICOL,"planirana ';'");
        }
        else if (match(CONT)) consume(SEMICOL,"planirana ';'");

    }

    // if_stmt = IF LPAREN expr RPAREN THEN block [ELSE block]
    private void parseIfStmt() {
        consume(LPAREN, "planirana '('");
        parseExpr();
        consume(RPAREN, "planirana ')'");
        consume(THEN, "planirano pa");
        parseBlock();
        if(match(ELSE)) parseBlock();
    }

    // for_stmt = FOR LPAREN dodela_stmt|(IDENT SEMICOL) expr SEMICOL dodela_stmt RPAREN block
    private void parseForStmt() {
        consume(LPAREN, "planirana '('");
        if(checkNext(SEMICOL)) consume(IDENT, "planirana promenljiva");
        else parseDodelaStmt();
        parseExpr();
        consume(SEMICOL, "planirana ';'");
        parseDodelaStmt();
        consume(RPAREN, "planirana ')'");
        parseBlock();
    }

    // dodela_stmt = lvalue ASSIGN expr SEMICOL
    private void parseDodelaStmt() {
        if(match(IDENT)) {
            if(check(LBRACK) || check(DOT))
            {
                while(check(LBRACK) || check(DOT))
                {
                    if (match(LBRACK))
                    {
                        parseExpr();
                        consume(RBRACK, "planirana ']'");
                    }
                    if (match(DOT)) consume(IDENT, "planirana promenljiva");
                }
            }
        }
        else
            error(peek(),"Dodela statement must start with identifier");
        consume(ASSIGN, "planirano podesi");
        parseExpr();
        consume(SEMICOL, "planirana ';'");
    }

    // proc_stmt = IDENT LPAREN [expr {SEMICOL expr}] RPAREN SEMICOL
    private void parseProcStmt(){
        consume(IDENT,"planirana promenljiva");
        consume(LPAREN, "planirana '('");
        if(!check(RPAREN))
        {
            do parseExpr();
            while (match(SEMICOL));
        }
        consume(RPAREN, "planirana ')'");
        consume(SEMICOL, "planirana ';'");
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
            if(check(LBRACK) || check(DOT))
            {
                while(check(LBRACK) || check(DOT))
                {
                    if (match(LBRACK))
                    {
                        parseExpr();
                        consume(RBRACK, "planirana ']'");
                    }
                    if (match(DOT)) consume(IDENT, "planirana promenljiva");
                }
            }
            else if(match(LPAREN))
            {
                do parseExpr();
                while (match(SEMICOL));
                consume(RPAREN, "planirana ')'");
            }
        }
        else if(match(LBRACE)) parseStructLit();
        else if(match(NOT)) parsePrimary();
        else if(match(LPAREN))
        {
            parseExpr();
            consume(RPAREN, "planirana ')'");
        }
        else if(!match(INT_LIT,FLOAT_LIT,CHAR_LIT,STRING_LIT,BOOL_LIT)) error(peek(),"Error");
    }

    //struct_lit = LBRACE expr {SEMICOL expr} RBRACE;
    private void parseStructLit() {
        do parseExpr();
        while (match(SEMICOL));
        consume(RBRACE, "planirana '}'");
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
