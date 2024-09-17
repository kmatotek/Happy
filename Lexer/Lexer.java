package Lexer;
import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private String fn, text;
    private Position pos;
    private Character currentChar;

    public Lexer(String fn, String text) {
        this.fn = fn;
        this.text = text;
        this.pos = new Position(-1, 0, -1, fn, text);
        this.advance();
    }

    public void advance() {
        pos.advance(currentChar);
        if (pos.idx < text.length()) {
            currentChar = text.charAt(pos.idx);
        } else {
            currentChar = null;
        }
    }

    public List<Token> makeTokens() {
        List<Token> tokens = new ArrayList<>();

        while (currentChar != null) {
            if (Character.isWhitespace(currentChar)) {
                advance();
            } else if (Character.isDigit(currentChar)) {
                tokens.add(makeNumber());
            } else if (currentChar == '+') {
                tokens.add(new Token(Token.TT_PLUS, pos, pos.copy().advance(currentChar)));
                advance();
            } else if (currentChar == '-') {
                tokens.add(new Token(Token.TT_MINUS, pos, pos.copy().advance(currentChar)));
                advance();
            } else if (currentChar == '*') {
                tokens.add(new Token(Token.TT_MUL, pos, pos.copy().advance(currentChar)));
                advance();
            } else if (currentChar == '/') {
                tokens.add(new Token(Token.TT_DIV, pos, pos.copy().advance(currentChar)));
                advance();
            } else if (currentChar == '(') {
                tokens.add(new Token(Token.TT_LPAREN, pos, pos.copy().advance(currentChar)));
                advance();
            } else if (currentChar == ')') {
                tokens.add(new Token(Token.TT_RPAREN, pos, pos.copy().advance(currentChar)));
                advance();
            } else {
                throw new RuntimeException("Illegal Character: " + currentChar);
            }
        }

        tokens.add(new Token(Token.TT_EOF, pos, pos));
        return tokens;
    }

    private Token makeNumber() {
        StringBuilder numStr = new StringBuilder();
        int dotCount = 0;
        Position posStart = pos.copy();

        while (currentChar != null && (Character.isDigit(currentChar) || currentChar == '.')) {
            if (currentChar == '.') {
                if (dotCount == 1) break;
                dotCount += 1;
                numStr.append('.');
            } else {
                numStr.append(currentChar);
            }
            advance();
        }

        if (dotCount == 0) {
            return new Token(Token.TT_INT, Integer.parseInt(numStr.toString()), posStart, pos);
        } else {
            return new Token(Token.TT_FLOAT, Double.parseDouble(numStr.toString()), posStart, pos);
        }
    }
}
