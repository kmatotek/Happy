package Lexer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import Position.*;

import Errors.IllegalCharError;
import Token.*;

public class Lexer{
    public static final String DIGITS = "0123456789";
    public static final ArrayList<String> KEYWORDS = new ArrayList<>(Arrays.asList("VAR"));

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
            } else if (DIGITS.indexOf(this.currChar) != -1){
                tokens.add(this.makeNumber());
            } else if(Character.isLetter(this.currChar)){
               // System.out.println(this.currChar);
                tokens.add(this.makeIdentifier());
            }   else if(this.currChar == '+'){
                tokens.add(new Token<>(Token.TT_PLUS, this.currPosition));
                this.advance();
            } else if(this.currChar == '-'){
                tokens.add(new Token<>(Token.TT_MINUS, this.currPosition));
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
            } else if(this.currChar == '='){
                tokens.add(new Token<>(Token.TT_EQ, this.currPosition));
                //out.println(tokens);
                this.advance();
            } else {
                // return some error
                Position positionStart = this.currPosition.copy();
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

        while(this.currChar != '\0' && (Character.isLetter(this.currChar) || DIGITS.indexOf(this.currChar) != -1) || this.currChar == '_'){
            idString += this.currChar;
            this.advance();
        }

        String tokenType = KEYWORDS.contains(idString) ? Token.TT_KEYWORD : Token.TT_IDENTIFIER;
        
        return new Token(tokenType, idString, posStart, this.currPosition);
    }

    public Token<?> makeNumber() {
        StringBuilder numStr = new StringBuilder();
        int dotCount = 0;
        Position positionStart = this.currPosition.copy();

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
            return new Token<>(Token.TT_INT, Integer.parseInt(numStr.toString()), positionStart, this.currPosition);
        } else {
            // It's a float
            return new Token<>(Token.TT_FLOAT, Double.parseDouble(numStr.toString()), positionStart, this.currPosition);
        }
    }

}