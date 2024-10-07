package Errors;

import Position.Position;

public class InvalidSyntaxError extends Error{

    public InvalidSyntaxError(Position positionStart, Position positionEnd, String errorDetails) {
        super(positionStart, positionEnd, "Invalid Syntax Error", errorDetails);
        //TODO Auto-generated constructor stub
    }

}
