package Errors;
import Lexer.Position;

public class InvalidSyntaxError extends MyError {
    public InvalidSyntaxError(Position posStart, Position posEnd, String details) {
        super(posStart, posEnd, "Invalid Syntax", details);
    }
}
