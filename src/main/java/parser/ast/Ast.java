package parser.ast;

import java.util.List;

public final class Ast {
    public static final class Program {
        public final boolean explicitProgram; // da li je "umotan" u BEGIN PROGRAM ... END PROGRAM
        public final List<TopItem> items;
        public Program(boolean explicitProgram, List<TopItem> items) {
            this.explicitProgram = explicitProgram; this.items = items;
        }
    }

    public interface TopItem {}

    //sve klase implementiraju TopItem iako je interjefs prazan, da bi mogle da se stavljaju u istu listu

}
