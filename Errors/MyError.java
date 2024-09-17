package Errors;
import Lexer.Position;

public class MyError {
    protected Position posStart;
    protected Position posEnd;
    protected String errorName;
    protected String details;

    public MyError(Position posStart, Position posEnd, String errorName, String details) {
        this.posStart = posStart;
        this.posEnd = posEnd;
        this.errorName = errorName;
        this.details = details;
    }

    public String asString() {
        StringBuilder result = new StringBuilder();
        result.append(errorName).append(": ").append(details).append("\n");
        result.append("File ").append(posStart.fn).append(", line ").append(posStart.ln + 1).append("\n");
        result.append("\n").append(stringWithArrows(posStart.ftxt, posStart, posEnd));
        return result.toString();
    }

    // Helper function for arrow display (replace this with your actual logic)
    private String stringWithArrows(String text, Position start, Position end) {
        // For illustration; use actual logic to visualize the error position
        return text.substring(start.idx, end.idx);
    }
}
