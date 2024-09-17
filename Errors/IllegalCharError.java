package Errors;

public class IllegalCharError extends RuntimeException {
    public IllegalCharError(String details) {
        super("Illegal Character: " + details);
    }
}
