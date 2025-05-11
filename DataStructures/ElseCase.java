package DataStructures;
import Operators.*;

public class ElseCase extends ASTNode{
    private ASTNode elseCase;
    private boolean shouldReturnNull;

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
}