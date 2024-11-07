package Operators;
import Position.*;

public class WhileNode extends ASTNode {
    public ASTNode conditionNode;
    public ASTNode bodyNode;
    public Position posStart;
    public Position posEnd;
    public boolean shouldReturnNull;

    public WhileNode(ASTNode conditionNode, ASTNode bodyNode, boolean shouldReturnNull){
        this.conditionNode = conditionNode;
        this.bodyNode = bodyNode;
        this.posStart = conditionNode.positionStart;
        this.posEnd = bodyNode.positionEnd;
        this.shouldReturnNull = shouldReturnNull;
    }
}
