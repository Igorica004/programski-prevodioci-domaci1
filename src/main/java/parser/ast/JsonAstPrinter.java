package parser.ast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import lexer.token.Token;


public final class JsonAstPrinter implements
        Expr.Visitor<JsonNode>,
        Stmt.Visitor<JsonNode>,
        Ast.Visitor<JsonNode> {

    private static final ObjectMapper M = new ObjectMapper();

    public String print(Ast.Program p) {
        try {
            ObjectNode root = (ObjectNode) p.accept(this);
            return M.writerWithDefaultPrettyPrinter().writeValueAsString(root);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JsonNode visitLiteral(Expr.Literal e) {
        ObjectNode o = M.createObjectNode();
        o.put("expr", "literal");
        o.put("value", e.token.lexeme);
        return o;
    }

    @Override
    public JsonNode visitIdent(Expr.Ident e) {
        ObjectNode o = M.createObjectNode();
        o.put("expr", "ident");

        ArrayNode name = M.createArrayNode();
        for (Token t : e.name) name.add(t.lexeme);
        o.set("name", name);

        if (!e.dims.isEmpty()) {
            ArrayNode dims = M.createArrayNode();
            for (Expr ex : e.dims) dims.add(ex.accept(this));
            o.set("dims", dims);
        }

        if (!e.references.isEmpty()) {
            ArrayNode refs = M.createArrayNode();
            for (Expr.Ident.Ref r : e.references) refs.add(r.name());
            o.set("references", refs);
        }

        if (!e.params.isEmpty()) {
            ArrayNode params = M.createArrayNode();
            for (Expr p : e.params) params.add(p.accept(this));
            o.set("params", params);
        }

        return o;
    }

    @Override
    public JsonNode visitUnary(Expr.Unary e) {
        ObjectNode o = M.createObjectNode();
        o.put("expr", "unary");
        o.put("operator", e.op.lexeme);
        o.set("operand", e.right.accept(this));
        return o;
    }

    @Override
    public JsonNode visitGrouping(Expr.Grouping e) {
        ObjectNode o = M.createObjectNode();
        o.put("expr", "group");
        o.set("expr", e.expr.accept(this));
        return o;
    }

    @Override
    public JsonNode visitBinary(Expr.Binary e) {
        ObjectNode o = M.createObjectNode();
        o.put("expr", "binary");
        o.put("op", e.op.lexeme);
        o.set("left", e.left.accept(this));
        o.set("right", e.right.accept(this));
        return o;
    }

    @Override
    public JsonNode visitExprList(Expr.ExprList e) {
        ObjectNode o = M.createObjectNode();
        o.put("expr", "exprList");
        ArrayNode exprs = M.createArrayNode();
        for (Expr ex : e.exprs) exprs.add(ex.accept(this));
        o.set("exprs", exprs);
        return o;
    }

    @Override
    public JsonNode visitVarDecl(Stmt.VarDecl s) {
        ObjectNode o = M.createObjectNode();
        o.put("stmt", "VarDecl");
        o.put("name", s.name.lexeme);
        ObjectNode type = M.createObjectNode();

        type.put("type", s.type.kind.name());
        ArrayNode dims = M.createArrayNode();
        for (Expr ex : s.dims) dims.add(ex.accept(this));
        type.set("dims", dims);
        o.set("type", type);

        if(s.assign != null) o.set("rvalue", s.assign.accept(this));
        return o;
    }

    @Override
    public JsonNode visitReturn(Stmt.Return s) {
        ObjectNode o = M.createObjectNode();
        o.put("stmt", "return");
        o.set("expr", s.expr.accept(this));
        return o;
    }

    @Override
    public JsonNode visitIfStmt(Stmt.IfStmt s) {
        ObjectNode o = M.createObjectNode();
        o.put("stmt", "ifStmt");
        o.set("condition", s.condition.accept(this));
        ArrayNode then = M.createArrayNode();
        for (Stmt stmt : s.thenBranch) then.add(stmt.accept(this));
        o.set("then", then);
        if (s.elseBranch == null) return o;
        ArrayNode els = M.createArrayNode();
        for (Stmt stmt : s.elseBranch) els.add(stmt.accept(this));
        o.set("then", els);
        return o;
    }

    @Override
    public JsonNode visitContinue(Stmt.Continue s) {
        ObjectNode o = M.createObjectNode();
        o.put("stmt", "continue");
        return o;
    }

    @Override
    public JsonNode visitBreak(Stmt.Break s) {
        ObjectNode o = M.createObjectNode();
        o.put("stmt", "break");
        return o;
    }

    @Override
    public JsonNode visitForStmt(Stmt.ForStmt s) {
        ObjectNode o = M.createObjectNode();
        o.put("stmt", "forStmt");
        if (s.dodela == null) o.put("counter", s.counter.lexeme);
        else o.set("counter", s.dodela.accept(this));
        o.set("condition", s.condition.accept(this));
        o.set("increment", s.increment.accept(this));
        ArrayNode args = M.createArrayNode();
        for (Stmt stmt : s.body) args.add(stmt.accept(this));
        o.set("body", args);
        return o;
    }

    @Override
    public JsonNode visitProcStmt(Stmt.ProcStmt s) {
        ObjectNode o = M.createObjectNode();
        o.put("stmt", "procStmt");
        o.put("ime", s.name.lexeme);
        ArrayNode args = M.createArrayNode();
        for (Expr ex : s.args) args.add(ex.accept(this));
        o.set("args", args);
        return o;
    }

    @Override
    public JsonNode visitDodelaStmt(Stmt.DodelaStmt s) {
        ObjectNode o = M.createObjectNode();
        o.put("stmt", "dodelaStmt");

        ArrayNode lvalue = M.createArrayNode();
        for (Token t : s.lvalue) lvalue.add(t.lexeme);
        o.set("lvalue", lvalue);

        ArrayNode dims = M.createArrayNode();
        for (Expr d : s.dims) dims.add(d.accept(this));
        o.set("dims", dims);

        ArrayNode refs = M.createArrayNode();
        for (Expr.Ident.Ref r : s.references) refs.add(r.name());
        o.set("references", refs);

        o.set("rvalue", s.rvalue.accept(this));
        return o;
    }

    @Override
    public JsonNode visitProgram(Ast.Program program) throws JsonProcessingException {
        ObjectNode o = M.createObjectNode();
        o.put("block", "program");
        o.put("name", program.name.lexeme);
        ArrayNode args = M.createArrayNode();
        for(Ast.TopItem topItem : program.items) args.add(topItem.accept(this));
        o.set("args", args);

        return o;
    }

    @Override
    public JsonNode visitProcDecl(Ast.ProcDecl procDecl) {
        ObjectNode o = M.createObjectNode();
        o.put("block", "procDecl");
        o.put("name", procDecl.name.lexeme);

        if(procDecl.returnType != null) {
            ObjectNode type = M.createObjectNode();
            type.put("type", procDecl.returnType.kind.name());
            ArrayNode dims = M.createArrayNode();
            for (Expr ex : procDecl.returnType.dims) dims.add(ex.accept(this));
            type.set("dims", dims);
            o.set("type", type);
        }

        ArrayNode params = M.createArrayNode();
        for(Ast.Param p : procDecl.params)
        {
            ObjectNode param = M.createObjectNode();
            param.put("name", p.name.lexeme);
            ObjectNode ptype = M.createObjectNode();
            ptype.put("type", p.type.kind.name());
            ArrayNode pdims = M.createArrayNode();
            for (Expr ex : p.type.dims) pdims.add(ex.accept(this));
            ptype.set("dims", pdims);
            param.set("type", ptype);
            params.add(param);
        }
        o.set("params", params);
        if(procDecl.varBlock != null) o.set("varBlock", procDecl.varBlock.accept(this));

        ArrayNode body = M.createArrayNode();
        for(Stmt ex : procDecl.body)  body.add(ex.accept(this));
        o.set("body", body);
        return o;
    }

    @Override
    public JsonNode visitStructDecl(Ast.StructDecl structDecl) throws JsonProcessingException {
        ObjectNode o = M.createObjectNode();
        o.put("block", "structDecl");
        o.put("name", structDecl.name.lexeme);
        ArrayNode pairs =  M.createArrayNode();
        for(Ast.Pair<Ast.Type,Token> pair : structDecl.decls)
        {
            ObjectNode pairNode = M.createObjectNode();

            ObjectNode typeNode = M.createObjectNode();
            typeNode.put("type", pair.first.kind.name());
            ArrayNode dims = M.createArrayNode();
            for (Expr ex : pair.first.dims) dims.add(ex.accept(this));
            typeNode.set("dims", dims);
            pairNode.set("type", typeNode);

            pairNode.put("name", pair.second.lexeme);

            pairs.add(pairNode);
        }
        o.set("fields",pairs);
        return o;
    }

    @Override
    public JsonNode visitEnumDecl(Ast.EnumDecl enumDecl) {
        ObjectNode o = M.createObjectNode();
        o.put("block", "enumDecl");
        o.put("name", enumDecl.name.lexeme);
        ArrayNode enums = M.createArrayNode();
        for(Token t : enumDecl.values) enums.add(t.lexeme);
        o.set("values", enums);

        return o;
    }

    @Override
    public JsonNode visitVarBlock(Ast.VarBlock varBlock) {
        ObjectNode o = M.createObjectNode();
        o.put("block", "varBlock");
        ArrayNode decls = M.createArrayNode();
        for(Stmt.VarDecl varDecl : varBlock.decls) decls.add(varDecl.accept(this));
        o.set("decls", decls);
        return o;
    }

    @Override
    public JsonNode visitMainBlock(Ast.MainBlock mainBlock) {
        ObjectNode o = M.createObjectNode();
        o.put("block","mainBlock");
        ArrayNode stmts = M.createArrayNode();
        for(Stmt st : mainBlock.body) stmts.add(st.accept(this));
        o.set("stmts", stmts);
        return o;
    }
}
