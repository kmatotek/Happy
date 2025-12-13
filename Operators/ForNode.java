package Operators;
import Token.*;
import Position.*;

public class ForNode extends ASTNode {
    private Position positionStart;
    private Position positionEnd;
    private Token<?> varNameToken;
    private ASTNode startValueNode;
    private ASTNode endValueNode;
    private ASTNode stepValueNode;
    private ASTNode bodyNode;
    private boolean shouldReturnNull;
    

    public ForNode(Token <?> varNameToken, ASTNode bodyNode, ASTNode startValueNode, ASTNode endValueNode, ASTNode stepValuNode, boolean shouldReturnNull){
        this.positionStart = varNameToken.getPositionStart();
        this.positionEnd = bodyNode.getPositionEnd();
        this.startValueNode = startValueNode;
        this.endValueNode = endValueNode;
        this.stepValueNode = stepValuNode;
        this.varNameToken = varNameToken;
        this.bodyNode = bodyNode;
        this.shouldReturnNull = shouldReturnNull;

    }

    public ForNode(Token<?> varNameToken, ASTNode bodyNode, ASTNode startValueNode, ASTNode endValueNode){
        this.positionStart = varNameToken.getPositionStart();
        this.positionEnd = bodyNode.getPositionEnd();
        this.startValueNode = startValueNode;
        this.endValueNode = endValueNode;
        this.varNameToken = varNameToken;
        this.bodyNode = bodyNode;

    }

    public Position getPositionStart() {
        return positionStart;
    }

    public void setPositionStart(Position posStart) {
        this.positionStart = posStart;
    }

    public Position getPositionEnd() {
        return positionEnd;
    }

    public void setPositionEnd(Position posEnd) {
        this.positionEnd = posEnd;
    }

    public Token<?> getVarNameToken() {
        return varNameToken;
    }

    public void setVarNameToken(Token<?> varNameToken) {
        this.varNameToken = varNameToken;
    }

    public ASTNode getStartValueNode() {
        return startValueNode;
    }

    public void setStartValueNode(ASTNode startValueNode) {
        this.startValueNode = startValueNode;
    }

    public ASTNode getEndValueNode() {
        return endValueNode;
    }

    public void setEndValueNode(ASTNode endValueNode) {
        this.endValueNode = endValueNode;
    }

    public ASTNode getStepValueNode() {
        return stepValueNode;
    }

    public void setStepValueNode(ASTNode stepValueNode) {
        this.stepValueNode = stepValueNode;
    }

    public ASTNode getBodyNode() {
        return bodyNode;
    }

    public void setBodyNode(ASTNode bodyNode) {
        this.bodyNode = bodyNode;
    }

    public boolean isShouldReturnNull() {
        return shouldReturnNull;
    }

    public void setShouldReturnNull(boolean shouldReturnNull) {
        this.shouldReturnNull = shouldReturnNull;
    }
}
