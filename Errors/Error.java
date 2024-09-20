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
        this.positionStart.line + errorName + ". " + errorDetails + 
        " " + stringWithArrows(errorDetails, positionStart, positionEnd);
    }

   
    public static String stringWithArrows(String text, Position posStart, Position posEnd) {
        StringBuilder result = new StringBuilder();

        // Calculate indices
        int idxStart = Math.max(text.lastIndexOf('\n', posStart.index), 0);
        int idxEnd = text.indexOf('\n', idxStart + 1);
        if (idxEnd < 0) idxEnd = text.length();

        // Generate each line
        int lineCount = posEnd.line - posStart.line + 1;
        for (int i = 0; i < lineCount; i++) {
            // Calculate line columns
            String line = text.substring(idxStart, idxEnd);
            int colStart = (i == 0) ? posStart.col : 0;
            int colEnd = (i == lineCount - 1) ? posEnd.col : line.length();

            // Append to result
            result.append(line).append('\n');
            result.append(" ".repeat(colStart)).append("^".repeat(colEnd - colStart));

            // Re-calculate indices
            idxStart = idxEnd;
            idxEnd = text.indexOf('\n', idxStart + 1);
            if (idxEnd < 0) idxEnd = text.length();
        }

        return result.toString().replace("\t", "");
    }
    
}