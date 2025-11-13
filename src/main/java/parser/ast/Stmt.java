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
        R visitIfStmt(IfStmt s);
        R visitBeginFor(BeginFor s);
        R visitContinue(Continue s);
        R visitForStmt(ForStmt s);
    }
    public abstract <R> R accept(Visitor<R> v);

    public static final class BeginFor extends Stmt {
        public Token var;   // IDENT
        public Expr from;   // aexpr
        public Expr to;     // aexpr
        public List<Stmt> body = new java.util.ArrayList<>();
        public BeginFor(Token var, Expr from, Expr to, List<Stmt> body) {
            this.var = var; this.from = from; this.to = to; this.body = body;
        }
        @Override public <R> R accept(Visitor<R> v) { return v.visitBeginFor(this); }
    }

    public static final class VarDecl extends Stmt {
        public List<Expr> dims = new java.util.ArrayList<>();
        public Token name;
        public Ast.Type type;
        public Expr assign;
        public VarDecl() {}
        public VarDecl(List<Expr> dims, Token name) {
            this.dims = dims; this.name = name;
        }
        @Override public <R> R accept(Visitor<R> v) { return v.visitVarDecl(this); }
    }
    public static final class IfStmt extends Stmt {
        Expr condition;
        List<Stmt> thenBranch = new java.util.ArrayList<>();
        List<Stmt> elseBranch = new java.util.ArrayList<>();
        public IfStmt() {}
        @Override
        public <R> R accept(Visitor<R> v) {return v.visitIfStmt(this);}
    }

    public static final class ForStmt extends Stmt {
        public Token counter;
        public DodelaStmt dodela;
        public Expr condition;
        public DodelaStmt increment;
        public List<Stmt> body = new java.util.ArrayList<>();
        public ForStmt() {}
        @Override public <R> R accept(Visitor<R> v) { return v.visitForStmt(this); }
    }
    public static final class DodelaStmt extends Stmt {
        public Token lvalue;
        public Expr rvalue;
        public List<Integer> dims = new java.util.ArrayList<>();
        public List<Token> indentifiers = new java.util.ArrayList<>();
        public DodelaStmt() {}
        public DodelaStmt(Token lvalue, Expr rvalue) {
            this.lvalue = lvalue; this.rvalue = rvalue;
        }
        @Override
        public <R> R accept(Visitor<R> v) { return v.visitDodelaStmt();}
    }

    public static final class ProcStmt extends Stmt {
        public Token name;
        public List<Token> args;

        @Override
        public <R> R accept(Visitor<R> v) {
            return v.visitProcStmt;
        }
    }

    public static final class Return extends Stmt {
        public Expr expr;
        public Return() {}
        public Return(Expr expr) { this.expr = expr; }
        @Override public <R> R accept(Visitor<R> v) { return v.visitReturn(this); }
    }

    public static final class Continue extends Stmt {
        public Continue() {}
        @Override public <R> R accept(Visitor<R> v) { return v.visitContinue(null); }
    }

}
