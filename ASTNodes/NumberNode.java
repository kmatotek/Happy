package ASTNodes;
import Lexer.Token;

public class NumberNode extends Node{
    public Token tok;

    public NumberNode(Token tok) {
        this.tok = tok;
    }

    @Override
    public String toString() {
        return tok.toString();
    }
}