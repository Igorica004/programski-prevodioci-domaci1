/*package parser.ast;

import lexer.token.Token;

public abstract class Expr {
    // Ovo je uzeto sa drajva. Neke metode su nam mozda visak, a neke mozda treba da dodamo. Metode su implementirane u JsonAstPrinter
    public interface Visitor<R> {
        R visitLiteral(Literal e);
        R visitIdent(Ident e);
        R visitIndex(Index e);
        R visitGrouping(Grouping e);
        R visitCall(Call e);
        R visitBinary(Binary e);
    }

    public abstract <R> R accept(Visitor<R> v);

    // Za svaki tip iz gramatike treba da se napravi klasa koja implementira Visitor interfejs tako da poziva odgovarajucu metodu u JsonAstPrinter klasi
    public final class Binary extends Expr {
        public final Token op;
        public final Expr left;
        public final Expr right;
        public Binary(Expr left, Token op, Expr right) { this.left = left; this.op = op; this.right = right; }
        @Override public <R> R accept(Visitor<R> v) { return v.visitBinary(this); }
    }

    public static final class Literal extends Expr {
        public final Token token; // INT_LIT
        public final int value;
        public Literal(Token token, int value) { this.token = token; this.value = value; }
        @Override public <R> R accept(Visitor<R> v) { return v.visitLiteral(this); }
    }

    public static final class Ident extends Expr {
        public final Token name; // IDENT
        public Ident(Token name) { this.name = name; }
        @Override public <R> R accept(Visitor<R> v) { return v.visitIdent(this); }
    }


}
*/