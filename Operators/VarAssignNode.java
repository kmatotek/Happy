package Operators;

import Token.*;
import Position.*;

public class VarAssignNode extends ASTNode{
    public Token<?> varNameToken;
    public ASTNode valueNode;
    public Position positionStart;
    public Position positionEnd;
    
    public VarAssignNode(Token<?> varNameToken, ASTNode valueNode){
        this.varNameToken = varNameToken;
        this.valueNode = valueNode;
        this.positionStart = varNameToken.positionStart;
        this.positionEnd = varNameToken.positionEnd;
    }
}
