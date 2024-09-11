import java.util.ArrayList;

public class happy {
    // --------------- TOKEN --------------------- //
    public class Token<T>{
        // TT : Token Type

        public static final String TT_INT    = "TT_INT"; 
        String TT_FLOAT  = "FLOAT";
        String TT_PLUS   = "PLUS";
        String TT_MINUS  = "MINUS";
        String TT_MUL    = "MUL";
        String TT_DIV    = "DIV";
        String TT_LPAREN = "LPAREN";
        String TT_RPAREN = "RPAREN";


        private String type;
        private T value;

        public Token(String type, T value){
            this.type = type;
            this.value = value;
        }

        @Override
        public String toString(){
            if(this.value != null){
                return "Type: " + this.type + " Value: " + this.value;
            } else {
                return "Type: " + this.type;
            }
        }
    }
    // --------------- LEXER --------------------- //
    public class Lexer{

        private String text;
        private int currPosition;
        private char currChar;

        public Lexer(String text){
            this.text = text;
            this.currPosition = -1;
            this.currChar = '\0';
            this.advance();
        }

        // Advance to next character in text
        public void advance(){
            this.currPosition += 1;
            if(this.currPosition < this.text.length()){
                this.currChar = this.text.charAt(currPosition); 
            } else {
                this.currChar = '\0';
            }
        }
        
        // Making tokens
        public ArrayList<Token<?>> makeTokens(){
            ArrayList<Token<?>> tokens = new ArrayList<>();

            return tokens;
        }
    }
}
