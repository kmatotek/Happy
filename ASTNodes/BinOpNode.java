package ASTNodes;
import Lexer.Token;


public class BinOpNode extends Node{
    public NumberNode leftNode;
    public Token opToken;
    public NumberNode rightNode;

    public BinOpNode(NumberNode leftNode, Token opToken, NumberNode rightNode) {
        this.leftNode = leftNode;
        this.opToken = opToken;
        this.rightNode = rightNode;
    }

    @Override
    public String toString() {
        return "(" + leftNode + " " + opToken + " " + rightNode + ")";
    }
}
