package Token;

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


    public String type;
    public T value;

    public Token(String type, T value){
        this.type = type;
        this.value = value;
    }

    public Token(String type){
        this.type = type;
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