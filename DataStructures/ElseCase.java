package DataStructures;
import Operators.*;

public class ElseCase extends ASTNode{
    public ASTNode elseCase;
    public boolean shouldReturnNull;

    public ElseCase(ASTNode elseCase, boolean shouldReturnNull){
        this.elseCase = elseCase;
        this.shouldReturnNull = shouldReturnNull;
    }
}