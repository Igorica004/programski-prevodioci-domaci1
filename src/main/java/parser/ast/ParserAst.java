/* package parser.ast;

import lexer.token.Token;
import lexer.token.TokenType;
import parser.RecognizerParser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static lexer.token.TokenType.*;
import static lexer.token.TokenType.SEMICOL;
import parser.ast.Ast.Type;

public final class ParserAst {
    private final List<Token> tokens;
    private int cur = 0;
    public ParserAst(List<Token> tokens) { this.tokens = tokens; }

    // Ovo je valjda koren stabla, objekat koji sadrzi sve druge objekte.
    public Ast.Program parseProgram() {
        Ast.Program p = new Ast.Program();
        consume(NAMESPACE,"expected NAMESPACE");
        p.name = consume(IDENT,"expected identifier");
        consume(SEMICOL, "expected ';'" );
        while(true)
        {
            if(check(PROC)) p.items.add(parseProcDecl());
            else if(check(STRUCT)) p.items.add(parseStructDecl());
            else if(check(ENUM)) p.items.add(parseEnumDecl());
            else if (check(VAR_BLOCK)) break;
            else error(peek(),"Error");
        }
        consume(VAR_BLOCK,"expected VAR_BLOCK");
        p.items.add(parseVarBlock());
        consume(MAIN,"expected MAIN");
        p.items.add(parseMain());
        consume(EOF, "expected EOF");
        return p;
    }

    private Ast.TopItem parseProcDecl(){
        Ast.ProcDecl pd = new Ast.ProcDecl();
        consume(PROC,"expected PROC");
        if(!match(IDENT)) pd.returnType = parseType();
        else pd.returnType = new Type(Type.Kind.VOID,null,1);
        pd.name = consume(IDENT,"expected IDENT");
        consume(LPAREN,"expected LPAREN");
        if(!match(RPAREN))
        {
            do {
                Token ident = consume(IDENT, "identifier expected");
                pd.params.add(new Ast.Param(ident,parseType()));
            } while (match(SEMICOL));
        }
        consume(RPAREN,"expected RPAREN");
        pd.varBlock = (Ast.VarBlock) parseVarBlock();
        pd.body = parseBlock();
        return pd;
    }

    private Ast.TopItem parseStructDecl() {
        Ast.StructDecl sd = new Ast.StructDecl();
        consume(STRUCT,"expected STRUCT");
        sd.name = consume(IDENT,"expected IDENT");
        consume(LBRACE,"expected '{");
        while(!match(RBRACE)){
            Type t = parseType();
            Token ident = consume(IDENT, "identifier expected");
            consume(SEMICOL, "expected SEMICOL");
            sd.decls.add(new Ast.Pair<Type,Token>(t,ident));
        }
        return sd;
    }

    private Ast.TopItem parseEnumDecl() {
        Ast.EnumDecl ed = new Ast.EnumDecl();
        consume(ENUM,"expected ENUM");
        ed.name = consume(IDENT,"expected IDENT");
        consume(LBRACE,"expected '{'");
        do {
            ed.values.add(consume(IDENT, "identifier expected"));
            consume(SEMICOL, "expected SEMICOL");
        } while (!match(RBRACE));
        return ed;
    }

    private Ast.TopItem parseVarBlock() {
        Ast.VarBlock var = new Ast.VarBlock();
        consume(VAR_BLOCK,"expected VAR_BLOCK");
        consume(LBRACE,"expected '{'");
        while(!match(RBRACE))
           var.decls.add(parseVarDecl());
        return var;
    }

    private Stmt.VarDecl parseVarDecl() {
        Stmt.VarDecl vd = new Stmt.VarDecl();
        consume(VAR, "expected VAR");
        vd.type = parseType();
        vd.name = consume(IDENT, "identifier expected");
        if(match(ASSIGN))
           vd.assign = parseExpr();
        consume(SEMICOL, "expected ';'");
        return vd;
    }

    private Ast.TopItem parseMain(){
        Ast.MainBlock mb = new Ast.MainBlock();
        consume(MAIN,"expected MAIN");
        consume(LBRACE,"expected '{");
        while(!match(RBRACE))
            mb.body.add(parseStmt());
        return mb;
    }

    private Type parseType() {
        if (!match(INT,FLOAT,CHAR,STRING,BOOL,IDENT))
            error(peek(),"Error");
        Type t = new Type();
        if(match(INT)) t.kind = Type.Kind.INT;
        else if(match(FLOAT)) t.kind = Type.Kind.FLOAT;
        else if(match(CHAR)) t.kind = Type.Kind.CHAR;
        else if(match(STRING)) t.kind = Type.Kind.STRING;
        else if(match(BOOL)) t.kind = Type.Kind.BOOL;
        else t.kind = Type.Kind.IDENT;

        int rank=1;
        while(check(LBRACK)){
            rank++;
            consume(LBRACK, null);
            if(!match(FLOAT_LIT,CHAR_LIT,STRING_LIT,BOOL_LIT,LBRACE,NOT)){

                    t.dims.add(Integer.parseInt(parseAdd()));
                    error(peek(),"Dimension must be an integer");
                consume(RBRACK, "expected ']'");
            }
            else error(peek(), "Can not define array dimension");
        }
        t.rank = rank;
        return t;
    }
    private List<Stmt> parseBlock() {
        List<Stmt> body = new ArrayList<>();
        consume(LBRACE, "expected '{'");
        while(!match(RBRACE)) body.add(parseStmt());
        return body;
    }
    private Stmt parseStmt() {
        if (check(IDENT))
        {
            if(checkNext(LPAREN)) return parseProcStmt();
            return parseDodelaStmt();
        }
        if (match(IF))
            return parseIfStmt();
        if (match(FOR))
            return parseForStmt();
        if (match(BREAK)) consume(SEMICOL,"expected ';'");
        if (match(RETURN)) {
            Stmt.Return r = new Stmt.Return();
            r.expr = parseExpr();
            consume(SEMICOL,"expected ';'");
            return r;
        }
        if (match(CONT)){
            consume(SEMICOL,"expected ';'");
            return new Stmt.Continue();
        }
        error(peek(),"Unknown statement type.");
        return null;

    }

    private Stmt parseIfStmt(){
        Stmt.IfStmt is = new Stmt.IfStmt();
        consume(LPAREN, "expected '('");
        is.condition = parseExpr();
        consume(RPAREN, "expected ')'");
        consume(THEN, "expected pa");
        is.thenBranch = parseBlock();
        if(match(ELSE))
            is.elseBranch = parseBlock();
        return is;
    }

    private Stmt parseForStmt() {
        Stmt.ForStmt fs = new Stmt.ForStmt();
        consume(LPAREN, "expected '('");
        if(checkNext(SEMICOL)){
            fs.counter = consume(IDENT, "expected identifier");
            consume(SEMICOL, "expected ';'");
        }
        else fs.dodela = (Stmt.DodelaStmt) parseDodelaStmt();
        fs.condition = (Expr) parseExpr();
        consume(SEMICOL, "expected ';'");
        fs.increment = (Stmt.DodelaStmt) parseDodelaStmt();
        consume(RPAREN, "expected ')'");
        fs.body = parseBlock();
        return fs;
    }

    private Stmt parseDodelaStmt(){
        Stmt.DodelaStmt ds = new Stmt.DodelaStmt();
        if(check(IDENT)) {
            ds.lvalue = consume(IDENT, "expected identifier");
            if (match(LBRACK)) {
                ds.rvalue = parseExpr();
                consume(RBRACK, "expected ']'");
            } else {
                while(match(DOT))
                    ds.indentifiers.add(consume(IDENT, "expected identifier"));
            }
        }
        consume(ASSIGN, "expected ASSIGN");
        parseExpr();
        consume(SEMICOL, "expected ';'");
        return ds;
    }

    private Stmt parseProcStmt(){
        Stmt.ProcStmt ps = new Stmt.ProcStmt();
        ps.name = consume(IDENT, "expected identifier");
        consume(LPAREN, "expected '('");
        if(!check(RPAREN))
        {
            do ps.args.add(parseExpr());
            while (match(SEMICOL));
        }
        consume(RPAREN, "expected ')'");
        consume(SEMICOL, "expected ';'");
        return ps;
    }

    private Expr parseExpr(){

    }

    private boolean match(TokenType... types) {
        for (TokenType t : types) {
            if (check(t)) { advance(); return true; }
        }
        return false;
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) {
            advance();
            return;
        }
        error(peek(), message);
        throw new RecognizerParser.ParseError(message);
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return type == EOF;
        return peek().type == type;
    }

    private boolean checkNext(TokenType type) {
        if (cur + 1 >= tokens.size()) return false;
        return tokens.get(cur + 1).type == type;
    }

    private Token advance() {
        if (!isAtEnd()) cur++;
        return previous();
    }
    private Token previous() {
        return tokens.get(cur - 1);
    }

    private boolean isAtEnd() {
        return peek().type == EOF;
    }

    private Token peek() {
        return tokens.get(cur);
    }

    private void error(Token token, String message) {
        String where = token.type == EOF ? " at end" : " at '" + token.lexeme + "'";
        throw new RecognizerParser.ParseError("PARSER ERROR > Error" + where + ": " + message + " (line: " + token.line + ", col: " + token.colStart + ")");
    }

}
*/