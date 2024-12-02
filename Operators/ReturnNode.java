package Operators;
import Position.*;


public class ReturnNode extends ASTNode{
    public ASTNode nodeToReturn;
    public Position posStart;
    public Position posEnd;

    public ReturnNode(ASTNode nodeToReturn, Position posStart, Position posEnd){
        this.nodeToReturn = nodeToReturn;
        this.posEnd = posEnd;
        this.posStart = posStart;
    }
}