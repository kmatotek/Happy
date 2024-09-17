package ASTNodes;
import Lexer.Token;

public class UnaryOpNode extends Node {
    public Token opToken;
    public Node node;

    public UnaryOpNode(Token opToken, Node node) {
        this.opToken = opToken;
        this.node = node;
    }

    @Override
    public String toString() {
        return "(" + opToken + " " + node + ")";
    }
}
