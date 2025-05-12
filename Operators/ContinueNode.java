package Operators;
import Position.*;

public class ContinueNode extends ASTNode{
    private Position positionStart;
    private Position positionEnd;

    public ContinueNode(Position posStart, Position posEnd){
        this.positionStart = posStart;
        this.positionEnd = posEnd;
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
