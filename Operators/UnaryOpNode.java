package Operators;
import Token.*;

public class UnaryOpNode extends ASTNode {
    public Token<?> opToken;
    public ASTNode node;

    public UnaryOpNode(Token<?> opToken, ASTNode node){
        this.opToken = opToken;
        this.node = node;
    }

    public String toString(){
        return "Type: "+ this.opToken.type +" Value: " + this.node;
    }
}