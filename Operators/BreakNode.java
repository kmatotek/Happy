package Operators;
import Position.*;

public class BreakNode extends ASTNode{
    public Position posStart;
    public Position posEnd;

    public BreakNode(Position posStart, Position posEnd){
        this.posStart = posStart;
        this.posEnd = posEnd;
    }
}
