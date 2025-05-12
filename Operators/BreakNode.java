package Operators;
import Position.*;

public class BreakNode extends ASTNode{
    private Position positionStart;
    private Position positionEnd;

    public BreakNode(Position positionStart, Position positionEnd){
        this.positionStart = positionStart;
        this.positionEnd = positionEnd;
    }

    @Override
    public Position getPositionStart() {
        return positionStart;
    }

    public void setPositionStart(Position positionStart) {
        this.positionStart = positionStart;
    }

    @Override
    public Position getPositionEnd() {
        return positionEnd;
    }

    public void setPositionEnd(Position positionEnd) {
        this.positionEnd = positionEnd;
    }
}
