package Operators;

import Token.*;
import Position.*;

public class VarAssignNode extends ASTNode{
    private Token<?> varNameToken;
    private ASTNode valueNode;
    private Position positionStart;
    private Position positionEnd;
    
    public VarAssignNode(Token<?> varNameToken, ASTNode valueNode){
        this.varNameToken = varNameToken;
        this.valueNode = valueNode;
        this.positionStart = varNameToken.getPositionStart();
        this.positionEnd = varNameToken.getPositionEnd();
    }

    public Token<?> getVarNameToken() {
        return varNameToken;
    }

    public void setVarNameToken(Token<?> varNameToken) {
        this.varNameToken = varNameToken;
    }

    public ASTNode getValueNode() {
        return valueNode;
    }

    public void setValueNode(ASTNode valueNode) {
        this.valueNode = valueNode;
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
