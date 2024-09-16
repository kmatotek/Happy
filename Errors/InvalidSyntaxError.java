package Errors;
import Lexer.Position;

public class InvalidSyntaxError extends Error {
    public InvalidSyntaxError(Position posStart, Position posEnd, String details) {
        super(posStart, posEnd, "Invalid Syntax", details);
    }
}
