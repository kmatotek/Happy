package DataStructures;
import Operators.*;

public class Case extends ASTNode {
    private ASTNode condition;
    private ASTNode expression;
    private boolean shouldReturnNull;

    public Case(ASTNode condition, ASTNode expression, boolean shouldReturnNull){
        this.condition = condition;
        this.expression = expression;
        this.shouldReturnNull = shouldReturnNull;
    }

    public ASTNode getExpression() {
        return expression;
    }

    public void setExpression(ASTNode expression) {
        this.expression = expression;
    }

    public ASTNode getCondition() {
        return condition;
    }

    public void setCondition(ASTNode condition) {
        this.condition = condition;
    }

    public boolean isShouldReturnNull() {
        return shouldReturnNull;
    }

    public void setShouldReturnNull(boolean shouldReturnNull) {
        this.shouldReturnNull = shouldReturnNull;
    }
}
