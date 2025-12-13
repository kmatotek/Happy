package Operators;
import Position.Position;
import Token.*;

public class UnaryOpNode extends ASTNode {
    private Token<?> opToken;
    private ASTNode node;
    private Position positionStart;
    private Position positionEnd;

    public UnaryOpNode(Token<?> opToken, ASTNode node){
        this.opToken = opToken;
        this.node = node;
        this.positionStart = opToken.getPositionStart();
        this.positionEnd = node.getPositionEnd();

    }

    public String toString(){
        return "Type: "+ this.opToken.getType() +" Value: " + this.node;
    }

    public Token<?> getOpToken() {
        return opToken;
    }

    public void setOpToken(Token<?> opToken) {
        this.opToken = opToken;
    }

    public ASTNode getNode() {
        return node;
    }

    public void setNode(ASTNode node) {
        this.node = node;
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