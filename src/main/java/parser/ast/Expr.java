package parser.ast;

import lexer.token.Token;

import java.util.ArrayList;
import java.util.List;

public abstract class Expr {
    public interface Visitor<R> {
        R visitLiteral(Literal e);
        R visitIdent(Ident e);
        R visitUnary(Unary e);
        R visitGrouping(Grouping e);
        R visitBinary(Binary e);
        R visitExprList(ExprList e);
    }

    public abstract <R> R accept(Visitor<R> v);

    public static final class Binary extends Expr {
        public final Token op;
        public final Expr left;
        public final Expr right;
        public Binary(Expr left, Token op, Expr right) { this.left = left; this.op = op; this.right = right; }
        @Override public <R> R accept(Visitor<R> v) { return v.visitBinary(this); }
    }

    public static final class Literal extends Expr {
        public final Token token;
        public Literal(Token token) { this.token = token; }
        @Override public <R> R accept(Visitor<R> v) { return v.visitLiteral(this); }
    }

    public static final class Ident extends Expr {
        public enum Ref{ARRAY, STRUCT}
        public Token name; // IDENT
        public List<Expr> dims = new ArrayList<>();
        public List<Token> indentifiers = new ArrayList<>();
        public List<Expr> params = new ArrayList<>();
        public List<Ref> references = new ArrayList<>();
        public Ident(){}
        public Ident(Token name) { this.name = name; }
        @Override public <R> R accept(Visitor<R> v) { return v.visitIdent(this); }
    }
    public static final class Unary extends Expr {
        public final Token op; public final Expr right;
        public Unary(Token op, Expr right) { this.op = op; this.right = right; }
        @Override public <R> R accept(Visitor<R> v) { return v.visitUnary(this); }
    }
    // Zagradjen izraz
    public static final class Grouping extends Expr {
        public final Expr expr;
        public Grouping(Expr expr) { this.expr = expr; }
        @Override public <R> R accept(Visitor<R> v) { return v.visitGrouping(this); }
    }

    public static final class ExprList extends Expr {
        public List<Expr> exprs = new ArrayList<>();
        public ExprList() {}

        @Override
        public <R> R accept(Visitor<R> v) { return v.visitExprList(this); }
    }




}