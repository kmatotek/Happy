package Lexer;


public class Token {
    public static final String TT_INT = "INT";
    public static final String TT_FLOAT = "FLOAT";
    public static final String TT_PLUS = "PLUS";
    public static final String TT_MINUS = "MINUS";
    public static final String TT_MUL = "MUL";
    public static final String TT_DIV = "DIV";
    public static final String TT_LPAREN = "LPAREN";
    public static final String TT_RPAREN = "RPAREN";
    public static final String TT_EOF = "EOF";

    public String type;
    public Object value;
    public Position posStart;
    public Position posEnd;

    public Token(String type, Object value, Position posStart, Position posEnd) {
        this.type = type;
        this.value = value;
        this.posStart = posStart;
        this.posEnd = posEnd;
    }

    public Token(String type, Position posStart, Position posEnd) {
        this(type, null, posStart, posEnd);
    }

    @Override
    public String toString() {
        if (value != null) {
            return type + ":" + value;
        }
        return type;
    }
}
