package Operators;
import Position.Position;
import Token.*;


public class NumberNode extends ASTNode{
    private Token<?> token;
    private Position positionStart;
    private Position positionEnd;
    
    public NumberNode(Token<?> token){
        this.token = token;
        this.positionStart = this.token.positionStart;
        this.positionEnd = this.token.positionEnd;
    }

    public String toString(){
        return token.value.toString();
    }

    public Token<?> getToken() {
        return token;
    }

    public void setToken(Token<?> token) {
        this.token = token;
    }

    @Override
    public Position getPositionStart() {
        return positionStart;
    }

    public void setPositionStart(Position positionStart) {
        this.positionStart = positionStart;
    }

    @Override
    public Position getPositionEnd() {
        return positionEnd;
    }

    public void setPositionEnd(Position positionEnd) {
        this.positionEnd = positionEnd;
    }
}
