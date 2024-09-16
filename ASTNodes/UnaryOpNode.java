package ASTNodes;
import Lexer.Token;

public class UnaryOpNode extends Node {
    public Token opToken;
    public NumberNode node;

    public UnaryOpNode(Token opToken, NumberNode node) {
        this.opToken = opToken;
        this.node = node;
    }

    @Override
    public String toString() {
        return "(" + opToken + " " + node + ")";
    }
}
