package Operators;
import Position.Position;
import Token.*;

public class UnaryOpNode extends ASTNode {
    public Token opToken;
    public ASTNode node;
    public Position positionStart;
    public Position positionEnd;

    public UnaryOpNode(Token opToken, ASTNode node){
        this.opToken = opToken;
        this.node = node;
        this.positionStart = opToken.positionStart;
        this.positionEnd = node.positionEnd;

    }

    public String toString(){
        return "Type: "+ this.opToken.type +" Value: " + this.node;
    }
}