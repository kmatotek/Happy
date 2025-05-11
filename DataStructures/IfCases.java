package DataStructures;
import Operators.*;
import java.util.List;
import java.util.ArrayList;

public class IfCases extends ASTNode{
    private List<Case> cases = new ArrayList<>();
    private ElseCase elseCase;

    public IfCases(List<Case> cases, ElseCase elseCase){
        this.cases = cases;
        this.elseCase = elseCase;
    }

    public List<Case> getCases() {
        return cases;
    }

    public void setCases(List<Case> cases) {
        this.cases = cases;
    }

    public ElseCase getElseCase() {
        return elseCase;
    }

    public void setElseCase(ElseCase elseCase) {
        this.elseCase = elseCase;
    }
}
