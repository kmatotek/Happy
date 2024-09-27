package Token;
import Position.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Token<T>{
    // TT : Token Type

    public static final String TT_IDENTIFIER  = "IDENTIFIER";
    public static final String TT_KEYWORD   = "KEYWORD";
    public static final String TT_EQ   = "EQ";
    public static final String TT_POW   = "POW";
    public static final String TT_INT    = "TT_INT"; 
    public static final String TT_FLOAT  = "FLOAT";
    public static final String TT_PLUS   = "PLUS";
    public static final String TT_MINUS  = "MINUS";
    public static final String TT_MUL    = "MUL";
    public static final String TT_DIV    = "DIV";
    public static final String TT_LPAREN = "LPAREN";
    public static final String TT_RPAREN = "RPAREN";
    public static final String TT_EE = "EE";
    public static final String TT_NE = "NE";
    public static final String TT_LT = "LT";
    public static final String TT_GT = "GT";
    public static final String TT_LTE = "LTE";
    public static final String TT_GTE = "GTE";
    public static final String TT_EOF = "EOF";

    public static final String DIGITS = "0123456789";
    public static final ArrayList<String> KEYWORDS = new ArrayList<>(Arrays.asList("var","and","or","not"));
    


    public String type;
    public T value;
    public static Position positionStart;
    public static Position positionEnd;

    public Token(String type, T value, Position positionStart, Position positionEnd){
        this.type = type;
        this.value = value;
        if(positionStart != null){
            this.positionStart = positionStart.copy();
            this.positionEnd = positionStart.copy();
            this.positionEnd.advance();
        }
        if(positionEnd != null){
            this.positionEnd = positionEnd.copy();
        }
    }

    public Token(String type, T value){
        this.type = type;
        this.value = value;
    }

    public Token(String type){
        this.type = type;
    }

    public boolean matches(String type, T value){
        boolean matches = this.type.equals(type) && this.value.equals(value);
        return matches;
    }

    @Override
    public String toString(){
        if(this.value != null){
            return "Type: " + this.type; //+ " Value: " + this.value;
        } else {
            return "Type: " + this.type;
        }
    }
}