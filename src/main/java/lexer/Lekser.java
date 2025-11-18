package lexer;


import lexer.token.Token;
import lexer.token.TokenType;

import java.util.ArrayList;
import java.util.List;
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

        klase.add(new Pair(TokenType.INT, "\\bpodatak\\b"));
        klase.add(new Pair(TokenType.NAMESPACE, "\\bprogram\\b"));
        klase.add(new Pair(TokenType.FLOAT, "\\bplutajuci\\b"));
        klase.add(new Pair(TokenType.CHAR, "\\bpismo\\b"));
        klase.add(new Pair(TokenType.STRING, "\\bporuka\\b"));
        klase.add(new Pair(TokenType.BOOL, "\\bpojam\\b"));
        klase.add(new Pair(TokenType.MAIN, "\\bpocni\\b"));
        klase.add(new Pair(TokenType.IF, "\\bproveri\\b"));
        klase.add(new Pair(TokenType.ELSE, "\\bpak\\b")); // mora iznad pa, zbog First matcha. Slicno i za relacione operatore
        klase.add(new Pair(TokenType.THEN, "\\bpa\\b"));
        klase.add(new Pair(TokenType.FOR, "\\bponovi\\b"));
        klase.add(new Pair(TokenType.ASSIGN, "\\bpodesi\\b"));
        klase.add(new Pair(TokenType.ADD, "\\bplus\\b"));
        klase.add(new Pair(TokenType.SUBTRACT, "\\bpotkresi\\b"));
        klase.add(new Pair(TokenType.MULTIPLY, "\\bputa\\b"));
        klase.add(new Pair(TokenType.DIVIDE, "\\bpodeljeno\\b"));
        klase.add(new Pair(TokenType.BOOL_LIT, "\\bpozitivno\\b"));
        klase.add(new Pair(TokenType.BOOL_LIT, "\\bpogresno\\b"));
        klase.add(new Pair(TokenType.BREAK, "\\bprekini\\b"));
        klase.add(new Pair(TokenType.CONT, "\\bproduzi\\b"));
        klase.add(new Pair(TokenType.RETURN, "\\bposalji\\b"));
        klase.add(new Pair(TokenType.VAR, "\\bpostavi\\b"));
        klase.add(new Pair(TokenType.VAR_BLOCK, "\\bpromenljive\\b"));
        klase.add(new Pair(TokenType.PROC, "\\bprocedura\\b"));
        klase.add(new Pair(TokenType.ENUM, "\\bpopis\\b"));
        klase.add(new Pair(TokenType.STRUCT, "\\bpostoji\\b"));
        klase.add(new Pair(TokenType.PERCENT, "\\bpovrat\\b"));
        klase.add(new Pair(TokenType.POW, "\\bpodigni\\b"));
        klase.add(new Pair(TokenType.EQ, "=="));
        klase.add(new Pair(TokenType.GTE, ">="));
        klase.add(new Pair(TokenType.LTE, "<="));
        klase.add(new Pair(TokenType.GT, ">"));
        klase.add(new Pair(TokenType.LT, "<"));
        klase.add(new Pair(TokenType.NEQ, "!="));
        klase.add(new Pair(TokenType.NOT, "!"));
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
        klase.add(new Pair(TokenType.WHITESPACE, "\\s+")); // Da li sme da stoji na vrhu? Zbog optimizacije
        klase.add(new Pair(TokenType.COMMENT, "//.*|/\\*(.|\\R)*?\\*/")); // da li mora komentar?
        klase.add(new Pair(TokenType.IDENT, "[a-zA-Z][a-zA-Z0-9_]*")); // mora da bude posle rezervisanih reci. Sme iznad string literala jer ne moze poceti sa "
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
                        Posto iz cele preostale linije treba da nadjemo najlevlji lexer.token, on mora da bude na pocetku (^)
                        Ako izostavimo ^, pronaci ce lexer.token u sred linije, sto nam ne odgovara zbog col
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