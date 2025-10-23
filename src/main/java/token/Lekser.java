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
        ArrayList<Pair> klase = new ArrayList<>(); //First match metod, ne max munch. Znaci da je poredak u listi bitan

        klase.add(new Pair(TokenType.INT, "podatak"));
        klase.add(new Pair(TokenType.FLOAT, "plutajuci"));
        klase.add(new Pair(TokenType.CHAR, "pismo"));
        klase.add(new Pair(TokenType.STRING, "poruka"));
        klase.add(new Pair(TokenType.BOOL, "pojam"));
        klase.add(new Pair(TokenType.MAIN, "program"));
        klase.add(new Pair(TokenType.IF, "proveri"));
        klase.add(new Pair(TokenType.ELSE, "pak")); // mora iznad pa, zbog First matcha. Slicno i za relacione operatore
        klase.add(new Pair(TokenType.THEN, "pa"));
        klase.add(new Pair(TokenType.FOR, "ponovi"));
        klase.add(new Pair(TokenType.ASSIGN, "podesi"));
        klase.add(new Pair(TokenType.ADD, "plus"));
        klase.add(new Pair(TokenType.SUBTRACT, "potkresi"));
        klase.add(new Pair(TokenType.MULTIPLY, "puta"));
        klase.add(new Pair(TokenType.DIVIDE, "podeljeno"));
        klase.add(new Pair(TokenType.TRUE, "pozitivno"));
        klase.add(new Pair(TokenType.FALSE, "pogresno"));
        klase.add(new Pair(TokenType.BREAK, "prekini"));
        klase.add(new Pair(TokenType.CONT, "produzi"));
        klase.add(new Pair(TokenType.RETURN, "povrati"));
        klase.add(new Pair(TokenType.VAR, "postavi"));
        klase.add(new Pair(TokenType.PROC, "procedura"));
        klase.add(new Pair(TokenType.ENUM, "popis"));
        klase.add(new Pair(TokenType.STRUCT, "postoji"));
        klase.add(new Pair(TokenType.STRUCT, "promenljive"));
        klase.add(new Pair(TokenType.PERCENT, "povrat"));
        klase.add(new Pair(TokenType.EQ, "=="));
        klase.add(new Pair(TokenType.GTE, ">="));
        klase.add(new Pair(TokenType.LTE, "<="));
        klase.add(new Pair(TokenType.GT, ">"));
        klase.add(new Pair(TokenType.LT, "<"));
        klase.add(new Pair(TokenType.NEQ, "!="));
        klase.add(new Pair(TokenType.AND, "&&"));
        klase.add(new Pair(TokenType.OR, "\\|\\|"));
        klase.add(new Pair(TokenType.LPAREN, "\\("));
        klase.add(new Pair(TokenType.RPAREN, "\\)"));
        klase.add(new Pair(TokenType.LBRACE, "\\{"));
        klase.add(new Pair(TokenType.RBRACE, "\\}"));
        klase.add(new Pair(TokenType.LBRACK, "\\["));
        klase.add(new Pair(TokenType.RBRACK, "\\]"));
        klase.add(new Pair(TokenType.SEMICOL, ";"));
        klase.add(new Pair(TokenType.COMMA, ","));
        klase.add(new Pair(TokenType.DOT, "\\."));
        klase.add(new Pair(TokenType.FLOAT_LIT, "[+-]?([0-9]+\\.[0-9]*|\\.[0-9]+)"));
        klase.add(new Pair(TokenType.INT_LIT, "[+-]?[0-9]+"));
        klase.add(new Pair(TokenType.CHAR_LIT, "'([^'\\\\]|\\\\[nrt\\\\'])'"));
        klase.add(new Pair(TokenType.STRING_LIT, "\"([^\"\\\\]|\\\\[nrt])*\""));
        klase.add(new Pair(TokenType.BOOL_LIT, "(pozitivno|pogresno)"));
        klase.add(new Pair(TokenType.WHITESPACE, "\\s+")); // Da li sme da stoji na vrhu? Zbog optimizacije
        klase.add(new Pair(TokenType.COMMENT, "//.*|/\\*(.|\\R)*?\\*/")); // da li mora komentar?
        klase.add(new Pair(TokenType.IDENT, "[a-zA-Z][a-zA-Z0-9_]*")); // mora da bude posle rezervisanih reci. Sme iznad string literala jer ne moze poceti sa "
        // tokeni za read/write??
        // sta se radi sa nizovima

        String[] lines = fajlTekst.split("\\R");

        int row = 1;

        for (String line : lines) {
            int col = 0;

            while (col < line.length()) {
                boolean hit = false;
                String ostatak = line.substring(col); // ono sto jos uvek nije rasporedjeno u trenutnoj liniji

                for (Pair pair : klase) {
                    TokenType type = pair.first;
                    Pattern pattern = Pattern.compile("^" + pair.second);
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
                            if (type == TokenType.INT_LIT)
                                tokens.add(new Token(type, leksema, Integer.parseInt(leksema), row, tokenStart, tokenEnd));
                            else if (type == TokenType.FLOAT_LIT)
                                tokens.add(new Token(type, leksema, Float.parseFloat(leksema), row, tokenStart, tokenEnd));
                            else if (type == TokenType.CHAR_LIT)
                                tokens.add(new Token(type, leksema, leksema.charAt(1), row, tokenStart, tokenEnd));
                            else if (type == TokenType.STRING_LIT)
                                tokens.add(new Token(type, leksema, leksema.substring(1, leksema.length() - 1), row, tokenStart, tokenEnd));
                            else if (type == TokenType.BOOL_LIT)
                                tokens.add(new Token(type, leksema, leksema.matches("pozitivno"), row, tokenStart, tokenEnd));
                            else tokens.add(new Token(type, leksema, null, row, tokenStart, tokenEnd));
                        }
                        col += leksema.length();
                        hit = true;
                        break;
                    }
                }
                if (!hit) { // todo handlovati gresku
                    throw new RuntimeException("Nevalidan karakter '" + line.charAt(col) + "' na poziciji " + row + ":" + col);
                }
            }
            row++;
        }
        tokens.add(new Token(TokenType.EOF,"pivo",null,row,0,0));
        return tokens;
    }
}

class Pair {
    TokenType first;
    String second;

    Pair(TokenType first, String second) {
        this.first = first;
        this.second = second;
    }
}