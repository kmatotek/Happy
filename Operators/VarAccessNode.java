package Operators;

import Token.*;
import Position.*;

public class VarAccessNode extends ASTNode {
    private Token<?> varNameToken;
    private Position positionStart;
    private Position positionEnd;

    public VarAccessNode(Token<?> varNameToken){
        this.varNameToken = varNameToken;
        this.positionStart = varNameToken.getPositionStart();
        this.positionEnd = varNameToken.getPositionEnd();
    }

    public Token<?> getVarNameToken() {
        return varNameToken;
    }

    public void setVarNameToken(Token<?> varNameToken) {
        this.varNameToken = varNameToken;
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
