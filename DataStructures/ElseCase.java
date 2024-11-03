package DataStructures;
import Operators.*;

public class ElseCase{
    public ASTNode elseCase;
    public boolean shouldReturnNull;

    public ElseCase(ASTNode elseCase, boolean shouldReturnNull){
        this.elseCase = elseCase;
        this.shouldReturnNull = shouldReturnNull;
    }
}