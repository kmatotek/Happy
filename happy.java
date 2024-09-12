import java.util.ArrayList;

public class happy {
    
    public static final String DIGITS = "0123456789";

    // --------------- ERRORS --------------------- //
                // ## TO DO


    // --------------- TOKEN --------------------- //
    public class Token<T>{
        // TT : Token Type

        public static final String TT_INT    = "TT_INT"; 
        public static final String TT_FLOAT  = "FLOAT";
        public static final String TT_PLUS   = "PLUS";
        public static final String TT_MINUS  = "MINUS";
        public static final String TT_MUL    = "MUL";
        public static final String TT_DIV    = "DIV";
        public static final String TT_LPAREN = "LPAREN";
        public static final String TT_RPAREN = "RPAREN";


        private String type;
        private T value;

        public Token(String type){
            this.type = type;
        }

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
            
            while(this.currChar != '\0'){
                if(" \t".indexOf(this.currChar) != -1){
                    this.advance(); // Skip white space and tabs
                } else if (DIGITS.indexOf(this.currChar) != -1){
                    // # TO DO tokens.add();
                }
                
                
                else if(this.currChar == '+'){
                    tokens.add(new Token<>(Token.TT_PLUS));
                    this.advance();
                } else if(this.currChar == '-'){
                    tokens.add(new Token<>(Token.TT_MINUS));
                    this.advance();
                } else if(this.currChar == '*'){
                    tokens.add(new Token<>(Token.TT_MUL));
                    this.advance();
                } else if(this.currChar == '/'){
                    tokens.add(new Token<>(Token.TT_DIV));
                    this.advance();
                } else if(this.currChar == '('){
                    tokens.add(new Token<>(Token.TT_LPAREN));
                    this.advance();
                } else if(this.currChar == ')'){
                    tokens.add(new Token<>(Token.TT_RPAREN));
                    this.advance();
                } else {
                    // return some error
                }
            }

            return tokens;
        }

        public Token<?> makeNumber() {
            StringBuilder numStr = new StringBuilder();
            int dotCount = 0;
        
            while (this.currChar != '\0' && (DIGITS.indexOf(this.currChar) != -1 || this.currChar == '.')) {
                if (this.currChar == '.') {
                    if (dotCount == 1) break; // Only allow one dot for a floating-point number
                    dotCount++;
                    numStr.append('.');
                } else {
                    numStr.append(this.currChar);
                }
                this.advance(); // Advance to the next character
            }
        
            if (dotCount == 0) {
                // It's an integer
                return new Token<>(Token.TT_INT, Integer.parseInt(numStr.toString()));
            } else {
                // It's a float
                return new Token<>(Token.TT_FLOAT, Double.parseDouble(numStr.toString()));
            }
        }
        
    }
}
