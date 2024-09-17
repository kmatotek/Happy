package Errors;
import Position.*;

public class Error extends RuntimeException {
    private String errorName;
    private String errorDetails;
    private Position positionStart;
    private Position positionEnd;

    public Error(Position positionStart, Position positionEnd, String errorName, String errorDetails) {
        super(errorName + ": " + errorDetails);
        this.errorName = errorName;
        this.errorDetails = errorDetails;
        this.positionStart = positionStart;
        this.positionEnd = positionEnd;
    }

    public String asString() {

        return "File: " + this.positionStart.fileName + " Line " + 
        this.positionStart.line + errorName + ". " + errorDetails;
    }
}