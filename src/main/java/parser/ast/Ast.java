package parser.ast;

import lexer.token.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class Ast {
    public static final class Program {
        public Token name;
        public List<TopItem> items;
        public Program() {
            items = new ArrayList<>();
        }
        public Program(List<TopItem> items) {
            this.items = items;
        }
    }

    public interface TopItem {}

    public static final class ProcDecl implements TopItem {
        public Token name; // IDENT
        public List<Param> params = new ArrayList<>();
        public Type returnType;
        public List<Stmt> body = new ArrayList<>();
        public VarBlock varBlock; // block
        public ProcDecl() {}
        public ProcDecl(Token name, List<Param> params, Type returnType, List<Stmt> body) {
            this.name = name; this.params = params; this.returnType = returnType; this.body = body;
        }
    }

    public static final class StructDecl implements TopItem {
        public Token name;
        // first - type, second - ident
        public List<Pair<Type,Token>> decls = new ArrayList<>();
        public StructDecl() {}
        public StructDecl(Token name, List<Pair<Type,Token>> decls) {
            this.name = name; this.decls = decls;
        }
    }

    public static final class EnumDecl implements TopItem {
        public Token name;
        public List<Token> values = new ArrayList<>();
        public EnumDecl() {}
        public EnumDecl(Token name, List<Token> values) {
            this.name = name; this.values = values;
        }
    }

    public static final class VarBlock implements TopItem {
        public List<Stmt.VarDecl> decls = new ArrayList<>();
        public VarBlock() {}
        public VarBlock(List<Stmt.VarDecl> decls) { this.decls = decls; }
    }

    public static final class Param {
        public Token name; // IDENT
        public Type type;
        public Param(Token name, Type type) { this.name = name; this.type = type; }
    }

    public static final class MainBlock implements TopItem {
        public List<Stmt> body = new ArrayList<>();
        public MainBlock() {}
        public MainBlock(List<Stmt> body) { this.body = body; }
    }

    // Povratna vrednost funkcije
    public static final class Type {
        public enum Kind {INT, VOID, CHAR, STRING, BOOL, IDENT, FLOAT}
        public Kind kind;

        public Token baseType;
        public int rank;      // broj [] zagrada, tj dimenzija
        public List<Integer> dims = new ArrayList<>();
        public Type() {}
        public Type(Kind kind, Token baseType, int rank) {
            this.kind = kind; this.baseType = baseType; this.rank = rank;
        }
        // vise tipova treba da se napise modularnije
    }

    public static class Pair<A, B> {
        public final A first;
        public final B second;
        public Pair(A first, B second) {
            this.first = first;
            this.second = second;
        }
    }
}
