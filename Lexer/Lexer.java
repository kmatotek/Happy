package Lexer;
import java.util.ArrayList;
import java.util.HashMap;
import Position.*;

import Errors.IllegalCharError;
import Token.*;

public class Lexer{

    private String text;
    private Position currPosition;
    private char currChar;

    public Lexer(String text){
        this.text = text;
        this.currPosition = new Position(-1, 0, -1, text);
        this.currChar = '\0';
        this.advance();
    }
    // Advance to next character in text
    public void advance(){
        this.currPosition.advance(currChar);
        if(this.currPosition.index < this.text.length()){
            this.currChar = this.text.charAt(currPosition.index); 
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
            } else if (Token.DIGITS.indexOf(this.currChar) != -1){
                tokens.add(this.makeNumber());
            } else if(Character.isLetter(this.currChar)){
                tokens.add(this.makeIdentifier());
            }   else if(this.currChar == '+'){
                tokens.add(new Token<>(Token.TT_PLUS, this.currPosition));
                this.advance();
            } else if(this.currChar == '-'){
                tokens.add(this.makeMinusOrArrow());
                this.advance();
            } else if(this.currChar == '*'){
                tokens.add(new Token<>(Token.TT_MUL, this.currPosition));
                this.advance();
            } else if(this.currChar == '/'){
                tokens.add(new Token<>(Token.TT_DIV, this.currPosition));
                this.advance();
            } else if(this.currChar == '^'){
                tokens.add(new Token<>(Token.TT_POW, this.currPosition));
                this.advance();
            } else if(this.currChar == '('){
                tokens.add(new Token<>(Token.TT_LPAREN, this.currPosition));
                this.advance();
            } else if(this.currChar == ')'){
                tokens.add(new Token<>(Token.TT_RPAREN, this.currPosition));
                this.advance();
            } else if(this.currChar == '['){
                tokens.add(new Token<>(Token.TT_LSQUAREB, this.currPosition));
                this.advance();
            } else if(this.currChar == ']'){
                tokens.add(new Token<>(Token.TT_RSQUAREB, this.currPosition));
                this.advance();
            } else if(this.currChar == '!'){
                Token<?> token = this.makeNotEquals(this.currPosition);
                tokens.add(token);
                this.advance();
            } else if(this.currChar == '='){
                tokens.add(this.makeEquals(this.currPosition));
                this.advance();
            } else if(this.currChar == '<'){
                tokens.add(this.makeLessThan(this.currPosition));
                this.advance();
            } else if(this.currChar == '>'){
                tokens.add(this.makeGreaterThan(this.currPosition));
                this.advance();
            } else if(this.currChar == ','){
                tokens.add(new Token<>(Token.TT_COMMA, this.currPosition));
                this.advance();
            } else if(this.currChar == '"'){
                tokens.add(this.makeString(this.currPosition));
                this.advance();
            } else if(this.currChar == ';' || this.currChar == '\n'){
                tokens.add(new Token<>(Token.TT_NEWLINE, this.currPosition));
                this.advance();
            }
            
            
            else {
                // return some error
                //Position positionStart = this.currPosition.copy();
                    char invalidChar = this.currChar; // Capture the invalid character
                    this.advance(); // Move to the next character
                    throw new IllegalCharError("Line " + this.currPosition.line + ": Unexpected character: " + invalidChar);
            }
        }

        tokens.add(new Token<>(Token.TT_EOF));
        return tokens;
    }

    public Token<?> makeIdentifier(){
        String idString = "";
        Position posStart = this.currPosition.copy();
       

        while(this.currChar != '\0' && (Character.isLetter(this.currChar) || Token.DIGITS.indexOf(this.currChar) != -1) || this.currChar == '_'){
            idString += this.currChar;
            this.advance();
        }
        
        String tokenType = Token.KEYWORDS.contains(idString) ? Token.TT_KEYWORD : Token.TT_IDENTIFIER;
       //System.out.println(idString);
        return new Token<>(tokenType, idString, posStart, this.currPosition);
    }

    public Token<?> makeNumber() {
        StringBuilder numStr = new StringBuilder();
        int dotCount = 0;
        Position positionStart = this.currPosition.copy();

        while (this.currChar != '\0' && (Token.DIGITS.indexOf(this.currChar) != -1 || this.currChar == '.')) {
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
            return new Token<>(Token.TT_INT, Integer.parseInt(numStr.toString()), positionStart, this.currPosition);
        } else {
            // It's a float
            return new Token<>(Token.TT_FLOAT, Double.parseDouble(numStr.toString()), positionStart, this.currPosition);
        }
    }

    public Token<?> makeString(Position position) {
        StringBuilder sb = new StringBuilder();
        Position posStart = this.currPosition.copy();
        boolean escapeChar = false;
        HashMap<Character, String> escape_characters = new HashMap<>();
        escape_characters.put('n', "\n");
        escape_characters.put('t', "\t");
    
        this.advance();
        while (this.currChar != '\0' && (this.currChar != '"' || escapeChar)) {
            if (escapeChar) {
                if (escape_characters.containsKey(this.currChar)) {
                    sb.append(escape_characters.get(this.currChar));
                } else {
                    sb.append(this.currChar);
                }
                escapeChar = false; // Reset escapeChar after handling the escape sequence
            } else {
                if (this.currChar == '\\') {
                    escapeChar = true; // Set escapeChar if backslash is found
                } else {
                    sb.append(this.currChar);
                }
            }
            this.advance();
        }
       // this.advance();
        return new Token<>(Token.TT_STRING, sb.toString(), posStart, this.currPosition);
    }
    

    public Token<?> makeNotEquals(Position position){
        Position  currPosition = position.copy();
        this.advance();

        if(this.currChar == '='){
            this.advance();
            return new Token<>(Token.TT_NE, currPosition);
        }
        else return new Token<>(Token.TT_EXCLM, currPosition);
        
        // throw new IllegalCharError("Extected '=' after '!'");

    }

    public Token<?> makeEquals(Position position){
        Position  currPosition = position.copy();
        this.advance();

        if(this.currChar == '='){
            this.advance();
            return new Token<>(Token.TT_EE, currPosition);
        }
        return new Token<>(Token.TT_EQ, currPosition);   

    }

    public Token<?> makeLessThan(Position position){
        Position  currPosition = position.copy();
        this.advance();

        if(this.currChar == '='){
            this.advance();
            return new Token<>(Token.TT_LTE, currPosition);
        }
        return new Token<>(Token.TT_LT, currPosition);   
    }

    public Token<?> makeGreaterThan(Position position){
        Position  currPosition = position.copy();
        this.advance();

        if(this.currChar == '='){
            this.advance();
            return new Token<>(Token.TT_GTE, currPosition);
        }
        return new Token<>(Token.TT_GT, currPosition);   
    }

    public Token<?> makeMinusOrArrow(){
        String tokType = Token.TT_MINUS;
        Position posStart = this.currPosition.copy();

        if(this.currChar == '>'){
            this.advance();
            tokType = Token.TT_ARROW;
        }
        
        return new Token<> (tokType, posStart);
    }

}