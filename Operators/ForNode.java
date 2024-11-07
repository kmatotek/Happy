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
    public boolean shouldRetunNull;
    

    public ForNode(Token <?> varNameToken, ASTNode bodyNode, ASTNode startValueNode, ASTNode endValueNode, ASTNode stepValuNode, boolean shouldReturnNull;){
        this.posStart = varNameToken.positionStart;
        this.posEnd = bodyNode.positionEnd;
        this.startValueNode = startValueNode;
        this.endValueNode = endValueNode;
        this.stepValueNode = stepValuNode;
        this.varNameToken = varNameToken;
        this.bodyNode = bodyNode;
        this.shouldReturnNull = shouldReturnNull;

    }

    public ForNode(Token<?> varNameToken, ASTNode bodyNode, ASTNode startValueNode, ASTNode endValueNode, boolean shouldReturnNull){
        this.posStart = varNameToken.positionStart;
        this.posEnd = bodyNode.positionEnd;
        this.startValueNode = startValueNode;
        this.endValueNode = endValueNode;
        this.varNameToken = varNameToken;
        this.bodyNode = bodyNode;
        this.shouldReturnNull = shouldReturnNull;

    }
}
