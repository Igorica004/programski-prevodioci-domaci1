package token;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lekser {
    private final String fajlTekst;

    public Lekser(String fajlTekst) {
        this.fajlTekst = fajlTekst;
    }

    public List<Token> scanTokens() {
        List<Token> tokens = new ArrayList<>();
        LinkedHashMap<TokenType, String> klase = new LinkedHashMap<>(); //First match metod, ne max munch. Znaci da je poredak u listi bitan

        klase.put(TokenType.INT, "podatak");
        klase.put(TokenType.FLOAT, "plutajuci");
        klase.put(TokenType.CHAR, "pismo");
        klase.put(TokenType.STRING, "poruka");
        klase.put(TokenType.BOOL, "pojam");
        klase.put(TokenType.MAIN, "program");
        klase.put(TokenType.IF, "proveri");
        klase.put(TokenType.ELSE, "pak"); // mora iznad pa, zbog First matcha. Slicno i za logicke operatore
        klase.put(TokenType.THEN, "pa");
        klase.put(TokenType.FOR, "ponovi");
        klase.put(TokenType.ASSIGN, "podesi");
        klase.put(TokenType.ADD, "plus");
        klase.put(TokenType.SUBTRACT, "potkresi");
        klase.put(TokenType.MULTIPLY, "puta");
        klase.put(TokenType.DIVIDE, "podeljeno");
        klase.put(TokenType.TRUE, "pozitivno");
        klase.put(TokenType.FALSE, "pogresno");
        klase.put(TokenType.BREAK, "prekini");
        klase.put(TokenType.CONT, "produzi");
        klase.put(TokenType.RETURN, "povrati");
        klase.put(TokenType.VAR, "postavi");
        klase.put(TokenType.PROC, "procedura");
        klase.put(TokenType.ENUM, "popis");
        klase.put(TokenType.STRUCT, "postoji");
        klase.put(TokenType.PERCENT, "%");
        klase.put(TokenType.EQ, "==");
        klase.put(TokenType.GTE, ">=");
        klase.put(TokenType.LTE, "<=");
        klase.put(TokenType.GT, ">");
        klase.put(TokenType.LT, "<");
        klase.put(TokenType.NEQ, "!=");
        klase.put(TokenType.AND, "&&");
        klase.put(TokenType.OR, "\\|\\|");
        klase.put(TokenType.LPAREN, "\\(");
        klase.put(TokenType.RPAREN, "\\)");
        klase.put(TokenType.LBRACE, "\\{");
        klase.put(TokenType.RBRACE, "\\}");
        klase.put(TokenType.LBRACK, "\\[");
        klase.put(TokenType.RBRACK, "\\]");
        klase.put(TokenType.SEMICOL, ";");
        klase.put(TokenType.COMMA, ",");
        klase.put(TokenType.DOT, ".");
        klase.put(TokenType.FLOAT_LIT, "[+-]?([0-9]+\\.[0-9]*|\\.[0-9]+)");
        klase.put(TokenType.INT_LIT, "[+-]?[0-9]+");
        klase.put(TokenType.CHAR_LIT, "'([^'\\\\]|\\\\[nrt\\\\'])'");
        klase.put(TokenType.STRING_LIT, "\"([^\"\\\\]|\\\\[nrt])*\"");
        klase.put(TokenType.BOOL_LIT, "(pozitivno|pogresno)");
        klase.put(TokenType.WHITESPACE, "\\s+"); // Da li sme da stoji na vrhu? Zbog optimizacije
        klase.put(TokenType.COMMENT, "//.*|/\\*(.|\\R)*?\\*/"); // da li mora komentar?
        klase.put(TokenType.IDENT, "[a-zA-Z][a-zA-Z0-9_]*"); // mora da bude posle rezervisanih reci. Sme iznad string literala jer ne moze poceti sa "

        String[] lines = fajlTekst.split("\\R");

        int row = 1;

        for (String line : lines) {
            int col = 0;

            while (col < line.length()) {
                boolean hit = false;
                String ostatak = line.substring(col); // ono sto jos uvek nije rasporedjeno u trenutnoj liniji

                for (Map.Entry<TokenType, String> klasa : klase.entrySet()) {
                    TokenType type = klasa.getKey();
                    Pattern pattern = Pattern.compile("^" + klasa.getValue());
                    /*
                        Posto iz cele preostale linije treba da nadjemo najlevlji token, on mora da bude na pocetku (^)
                        Ako izostavimo ^, pronaci ce token u sred linije, sto nam ne odgovara zbog col
                     */
                    Matcher matcher = pattern.matcher(ostatak);

                    if (matcher.find()) {
                        String leksema = matcher.group();
                        int tokenStart = col;
                        int tokenEnd = col + leksema.length() - 1;

                        if (type != TokenType.WHITESPACE && type != TokenType.COMMENT) {  // EOF ide ovde ili??
                            if (type == TokenType.INT_LIT) tokens.add(new Token(type, leksema, Integer.parseInt(leksema), row, tokenStart, tokenEnd));
                            else if (type == TokenType.FLOAT_LIT) tokens.add(new Token(type, leksema, Float.parseFloat(leksema), row, tokenStart, tokenEnd));
                            else if (type == TokenType.CHAR_LIT) tokens.add(new Token(type, leksema, leksema.charAt(1), row, tokenStart, tokenEnd));
                            else if (type == TokenType.STRING_LIT) tokens.add(new Token(type, leksema, leksema.substring(1,leksema.length()-1), row, tokenStart, tokenEnd));
                            else if (type == TokenType.BOOL_LIT) tokens.add(new Token(type, leksema, leksema.matches("pozitivno"), row, tokenStart, tokenEnd));
                            else tokens.add(new Token(type, leksema, null, row, tokenStart, tokenEnd));
                        }
                        col += leksema.length();
                        hit = true;
                        break;
                    }
                }
                if (!hit) { // todo handlovati gresku

                }
            }
            row++;
        }
        tokens.add(new Token(TokenType.EOF,"pivo",null,row,0,0));
        return tokens;
    }
}