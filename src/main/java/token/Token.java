package token;

public class Token {// zasto ne rekord klasa?
    public final TokenType type;
    public final String lexeme;
    public final Object literal;
    public final int line, colStart, colEnd;
    public Token(TokenType t, String lx, Object lit, int line, int cs, int ce) {
        this.type=t; this.lexeme=lx; this.literal=lit; this.line=line; this.colStart=cs; this.colEnd=ce;
    }

    public String toString()
    {
        return (type+" '"+lexeme+"' at line: "+line+", column: "+colStart). // razmisliti o formatiranju string i char literala
                replace("\n", "\\n").
                replace("\0", "\\0");
    }
}