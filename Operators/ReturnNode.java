package Operators;
import Position.*;


public class ReturnNode extends ASTNode{
    private ASTNode nodeToReturn;
    private Position positionStart;
    private Position positionEnd;

    public ReturnNode(ASTNode nodeToReturn, Position posStart, Position posEnd){
        this.nodeToReturn = nodeToReturn;
        this.positionEnd = posEnd;
        this.positionStart = posStart;
    }

    public ASTNode getNodeToReturn() {
        return nodeToReturn;
    }

    public void setNodeToReturn(ASTNode nodeToReturn) {
        this.nodeToReturn = nodeToReturn;
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