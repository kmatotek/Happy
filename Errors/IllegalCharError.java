package Errors;
import Lexer.Position;

public class IllegalCharError extends MyError {
    public IllegalCharError(Position posStart, Position posEnd, String details) {
        super(posStart, posEnd, "Illegal Character", details);
    }
}
