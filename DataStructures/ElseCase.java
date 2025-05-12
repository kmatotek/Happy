package DataStructures;
import Operators.*;
import Position.Position;

public class ElseCase extends ASTNode{
    private ASTNode elseCase;
    private boolean shouldReturnNull;
    private Position positionStart;
    private Position positionEnd;

    public ElseCase(ASTNode elseCase, boolean shouldReturnNull){
        this.elseCase = elseCase;
        this.shouldReturnNull = shouldReturnNull;
    }

    public ASTNode getElseCase() {
        return elseCase;
    }

    public void setElseCase(ASTNode elseCase) {
        this.elseCase = elseCase;
    }

    public boolean isShouldReturnNull() {
        return shouldReturnNull;
    }

    public void setShouldReturnNull(boolean shouldReturnNull) {
        this.shouldReturnNull = shouldReturnNull;
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