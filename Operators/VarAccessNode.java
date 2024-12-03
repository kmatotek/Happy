package Operators;

import Token.*;
import Position.*;

public class VarAccessNode extends ASTNode {
    public Token<?> varNameToken;
    public Position positionStart;
    public Position positionEnd;

    public VarAccessNode(Token<?> varNameToken){
        this.varNameToken = varNameToken;
        this.positionStart = varNameToken.positionStart;
        this.positionEnd = varNameToken.positionEnd;
    }
}
