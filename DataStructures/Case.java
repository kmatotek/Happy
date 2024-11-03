package DataStructures;
import Operators.*;

public class Case extends ASTNode {
    public ASTNode condition;
    public ASTNode expression;
    public boolean shouldReturnNull;

    public Case(ASTNode condition, ASTNode expression, boolean shouldReturnNull){
        this.condition = condition;
        this.expression = expression;
        this.shouldReturnNull = shouldReturnNull;
    }
}
