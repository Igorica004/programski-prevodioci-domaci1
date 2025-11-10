package parser.ast;

import lexer.token.Token;

import java.util.List;

public abstract class Stmt {
    // Ovo je uzeto sa drajva. Neke metode su nam mozda visak, a neke mozda treba da dodamo. Metode su implementirane u JsonAstPrinter
    public interface Visitor<R> {
        R visitVarDecl(VarDecl s);
        R visitReturn(Return s);
        R visitAssign(Assign s);
        R visitCallStmt(CallStmt s);
        R visitBeginIf(BeginIf s);
        R visitBeginFor(BeginFor s);
    }
    public abstract <R> R accept(Visitor<R> v);

    public static final class BeginFor extends Stmt {
        public final Token var;   // IDENT
        public final Expr from;   // aexpr
        public final Expr to;     // aexpr
        public final List<Stmt> body;
        public BeginFor(Token var, Expr from, Expr to, List<Stmt> body) {
            this.var = var; this.from = from; this.to = to; this.body = body;
        }
        @Override public <R> R accept(Visitor<R> v) { return v.visitBeginFor(this); }
    }

    public static final class Return extends Stmt {
        public final Expr expr;
        public Return(Expr expr) { this.expr = expr; }
        @Override public <R> R accept(Visitor<R> v) { return v.visitReturn(this); }
    }

}
