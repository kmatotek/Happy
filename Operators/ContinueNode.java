package Operators;
import Position.*;

public class ContinueNode extends ASTNode{
    public Position posStart;
    public Position posEnd;

    public ContinueNode(Position posStart, Position posEnd){
        this.posStart = posStart;
        this.posEnd = posEnd;
    }
}
