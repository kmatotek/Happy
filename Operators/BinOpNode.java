package Operators;
import Token.*;
import Position.*;

public class BinOpNode extends ASTNode {
    public ASTNode leftNode;
    public ASTNode rightNode;
    public Token<?> token;
    public Position positionStart;
    public Position positionEnd;
    
    public BinOpNode(ASTNode leftNode, Token<?> token, ASTNode rightNode){
        this.leftNode = leftNode;
        this.token = token;
        this.rightNode = rightNode;
        this.positionStart = this.leftNode.positionStart;
        this.positionEnd = this.rightNode.positionEnd;
    }
    public String toString(){
        return "(" + leftNode.toString() + " " + token.toString() + " " + rightNode.toString() + ")";
    }
}

