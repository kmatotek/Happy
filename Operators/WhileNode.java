package Operators;
import Position.*;

public class WhileNode extends ASTNode {
    private ASTNode conditionNode;
    private ASTNode bodyNode;
    private Position posStart;
    private Position posEnd;
    private boolean shouldReturnNull;

    public WhileNode(ASTNode conditionNode, ASTNode bodyNode, boolean shouldReturnNull){
        this.conditionNode = conditionNode;
        this.bodyNode = bodyNode;
        this.posStart = conditionNode.getPositionStart();
        this.posEnd = bodyNode.getPositionEnd();
        this.shouldReturnNull = shouldReturnNull;
    }

    public ASTNode getConditionNode() {
        return conditionNode;
    }

    public void setConditionNode(ASTNode conditionNode) {
        this.conditionNode = conditionNode;
    }

    public ASTNode getBodyNode() {
        return bodyNode;
    }

    public void setBodyNode(ASTNode bodyNode) {
        this.bodyNode = bodyNode;
    }

    public Position getPositionStart() {
        return posStart;
    }

    public void setPositionStart(Position posStart) {
        this.posStart = posStart;
    }

    public Position getPositionEnd() {
        return posEnd;
    }

    public void setPositionEnd(Position posEnd) {
        this.posEnd = posEnd;
    }

    public boolean isShouldReturnNull() {
        return shouldReturnNull;
    }

    public void setShouldReturnNull(boolean shouldReturnNull) {
        this.shouldReturnNull = shouldReturnNull;
    }
}
