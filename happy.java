import java.util.ArrayList;

public class happy {

    public static final String DIGITS = "0123456789";

    // --------------- ERRORS --------------------- //
    public static class Error extends RuntimeException {
        private String errorName;
        private String errorDetails;
        private Position positionStart;
        private Position positionEnd;

        public Error(Position positionStart, Position positionEnd, String errorName, String errorDetails) {
            super(errorName + ": " + errorDetails);
            this.errorName = errorName;
            this.errorDetails = errorDetails;
            this.positionStart = positionStart;
            this.positionEnd = positionEnd;
        }

        public String asString() {
            
            return "File: " + this.positionStart.fileName + " Line " + 
            this.positionStart.line + errorName + ". " + errorDetails;
        }
    }

    public static class IllegalCharError extends RuntimeException {
        public IllegalCharError(String details) {
            super("Illegal Character: " + details);
        }
    }

    // --------------- POSITION --------------------- //

    public static class Position {
        private int index;
        private int line;
        private int col;
        private String fileName;
        private String fileText;

        public Position(int index, int line, int col, String fileName, String fileText){
            this.index = index;
            this.line = line;
            this.col = col;
            this.fileName = fileName;
            this.fileText = fileText;
        }

        public void advance(char currChar){
            this.index += 1;
            this.col += 1;

            if(currChar == '\n'){
                this.line += 1;
                this.col = 0;
            }
        }

        public Position copy(){
            return new Position(this.index, this.line, this.col, this.fileName, this.fileText);
        }
    }

    // --------------- TOKEN --------------------- //
    public static class Token<T> {
        // TT : Token Type
        public static final String TT_INT = "TT_INT";
        public static final String TT_FLOAT = "FLOAT";
        public static final String TT_PLUS = "PLUS";
        public static final String TT_MINUS = "MINUS";
        public static final String TT_MUL = "MUL";
        public static final String TT_DIV = "DIV";
        public static final String TT_LPAREN = "LPAREN";
        public static final String TT_RPAREN = "RPAREN";

        private String type;
        private T value;

        public Token(String type) {
            this.type = type;
        }

        public Token(String type, T value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public String toString() {
            if (this.value != null) {
                return "Type: " + this.type + " Value: " + this.value;
            } else {
                return "Type: " + this.type;
            }
        }
    }

    // --------------- LEXER --------------------- //
    public static class Lexer {
        private String fileName;
        private String text;
        private Position currPosition;
        private char currChar;

        public Lexer(String fileName, String text) {
            this.fileName = fileName;
            this.text = text;
            this.currPosition = new Position(-1, 0, -1, fileName, text);
            this.currChar = '\0';
            this.advance();
        }

        // Advance to next character in text
        public void advance() {
            this.currPosition.advance(currChar);

            if (this.currPosition.index < this.text.length()) {
                this.currChar = this.text.charAt(currPosition.index);
            } else {
                this.currChar = '\0';
            }
        }

        // Making tokens
        public ArrayList<Token<?>> makeTokens() {
            ArrayList<Token<?>> tokens = new ArrayList<>();

            while (this.currChar != '\0') {
                if (" \t".indexOf(this.currChar) != -1) {
                    this.advance(); // Skip white space and tabs
                } else if (DIGITS.indexOf(this.currChar) != -1) {
                    tokens.add(this.makeNumber()); // Process number tokens
                } else if (this.currChar == '+') {
                    tokens.add(new Token<>(Token.TT_PLUS));
                    this.advance();
                } else if (this.currChar == '-') {
                    tokens.add(new Token<>(Token.TT_MINUS));
                    this.advance();
                } else if (this.currChar == '*') {
                    tokens.add(new Token<>(Token.TT_MUL));
                    this.advance();
                } else if (this.currChar == '/') {
                    tokens.add(new Token<>(Token.TT_DIV));
                    this.advance();
                } else if (this.currChar == '(') {
                    tokens.add(new Token<>(Token.TT_LPAREN));
                    this.advance();
                } else if (this.currChar == ')') {
                    tokens.add(new Token<>(Token.TT_RPAREN));
                    this.advance();
                } else {
                    // Handle unexpected character
                    Position positionStart = this.currPosition.copy();
                    char invalidChar = this.currChar; // Capture the invalid character
                    this.advance(); // Move to the next character
                    throw new IllegalCharError("Line " + this.currPosition.line + ": Unexpected character: " + invalidChar);
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

    public static ArrayList<Token<?>> run(String fileName, String text) {
        Lexer lexer = new Lexer(fileName, text);
        return lexer.makeTokens();
    }
}
