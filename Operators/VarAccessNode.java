package Operators;

import Token.*;
import Position.*;

public class VarAccessNode extends ASTNode {
    public Token<?> varNameToken;
    Position positionStart;
    Position positionEnd;

    public VarAccessNode(Token<?> varNameToken){
        this.varNameToken = varNameToken;
        this.positionStart = varNameToken.positionStart;
        this.positionEnd = varNameToken.positionEnd;
    }
}
