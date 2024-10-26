package Operators;
import Token.*;
import Position.*;

public class ForNode extends ASTNode {
    public Position posStart;
    public Position posEnd;
    public Token<?> varNameToken;
    public ASTNode startValueNode;
    public ASTNode endValueNode;
    public ASTNode stepValueNode;
    public ASTNode bodyNode;
    

    public ForNode(Token <?> varNameToken, ASTNode bodyNode, ASTNode startValueNode, ASTNode endValueNode, ASTNode stepValuNode){
        this.posStart = varNameToken.positionStart;
        this.posEnd = bodyNode.positionEnd;
        this.startValueNode = startValueNode;
        this.endValueNode = endValueNode;
        this.stepValueNode = stepValuNode;
        this.varNameToken = varNameToken;
        this.bodyNode = bodyNode;

    }

    public ForNode(Token<?> varNameToken, ASTNode bodyNode, ASTNode startValueNode, ASTNode endValueNode){
        this.posStart = varNameToken.positionStart;
        this.posEnd = bodyNode.positionEnd;
        this.startValueNode = startValueNode;
        this.endValueNode = endValueNode;
        this.varNameToken = varNameToken;
        this.bodyNode = bodyNode;

    }
}
