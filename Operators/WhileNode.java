package Operators;
import Position.*;

public class WhileNode extends ASTNode {
    public ASTNode conditionNode;
    public ASTNode bodyNode;
    public Position posStart;
    public Position posEnd;

    public WhileNode(ASTNode conditionNode, ASTNode bodyNode){
        this.conditionNode = conditionNode;
        this.bodyNode = bodyNode;
        this.posStart = conditionNode.positionStart;
        this.posEnd = bodyNode.positionEnd;
    }
}
