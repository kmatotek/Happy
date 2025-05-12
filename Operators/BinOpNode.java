package Operators;
import Token.*;
import Position.*;

public class BinOpNode extends ASTNode {
    private ASTNode leftNode;
    private ASTNode rightNode;
    private Token<?> token;
    private Position positionStart;
    private Position positionEnd;
    
    public BinOpNode(ASTNode leftNode, Token<?> token, ASTNode rightNode){
        this.leftNode = leftNode;
        this.token = token;
        this.rightNode = rightNode;
        this.positionStart = this.leftNode.getPositionStart();
        this.positionEnd = this.rightNode.getPositionEnd();
    }
    public String toString(){
        return "(" + leftNode.toString() + " " + token.toString() + " " + rightNode.toString() + ")";
    }

    public ASTNode getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(ASTNode leftNode) {
        this.leftNode = leftNode;
    }

    public ASTNode getRightNode() {
        return rightNode;
    }

    public void setRightNode(ASTNode rightNode) {
        this.rightNode = rightNode;
    }

    public Token<?> getToken() {
        return token;
    }

    public void setToken(Token<?> token) {
        this.token = token;
    }

    public Position getPositionStart() {
        return positionStart;
    }

    public void setPositionStart(Position positionStart) {
        this.positionStart = positionStart;
    }

    public Position getPositionEnd() {
        return positionEnd;
    }

    public void setPositionEnd(Position positionEnd) {
        this.positionEnd = positionEnd;
    }
}

