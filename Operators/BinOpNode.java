package Operators;
import Token.*;

public class BinOpNode extends ASTNode {
    public ASTNode leftNode;
    public ASTNode rightNode;
    public Token token;
    
    public BinOpNode(ASTNode leftNode, Token token, ASTNode rightNode){
        this.leftNode = leftNode;
        this.token = token;
        this.rightNode = rightNode;
    }
    public String toString(){
        return "(" + leftNode.toString() + " " + token.toString() + " " + rightNode.toString() + ")";
    }
}

