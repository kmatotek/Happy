package Operators;
import Position.Position;
import Token.*;


public class NumberNode extends ASTNode{
    public Token<?> token;
    public Position positionStart;
    public Position positionEnd;
    
    public NumberNode(Token<?> token){
        this.token = token;
        this.positionStart = this.token.positionStart;
        this.positionEnd = this.token.positionEnd;
    }

    public String toString(){
        return token.value.toString();
    }
}
