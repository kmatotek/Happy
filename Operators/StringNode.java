package Operators;
import Position.Position;
import Token.*;


public class StringNode extends ASTNode{
    public Token<?> token;
    public Position positionStart;
    public Position positionEnd;
    
    public StringNode(Token<?> token){
        this.token = token;
        this.positionStart = this.token.positionStart;
        this.positionEnd = this.token.positionEnd;
    }

    public String toString(){
        return token.value.toString();
    }
}
