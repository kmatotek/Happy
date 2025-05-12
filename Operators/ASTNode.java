package Operators;

import Position.*;
import Token.*;

public abstract class ASTNode {
    private Position positionStart;
    private Position positionEnd;
    private Token<?> token;

    public abstract Position getPositionStart();
    public abstract Position getPositionEnd();
    
}
