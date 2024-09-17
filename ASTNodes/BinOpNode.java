package ASTNodes;
import Lexer.Token;


public class BinOpNode extends Node{
    public Node leftNode;
    public Token opToken;
    public Node rightNode;

    public BinOpNode(Node leftNode, Token opToken, Node rightNode) {
        this.leftNode = leftNode;
        this.opToken = opToken;
        this.rightNode = rightNode;
    }

    @Override
    public String toString() {
        return "(" + leftNode + " " + opToken + " " + rightNode + ")";
    }
}
