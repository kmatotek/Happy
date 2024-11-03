package DataStructures;
import Operators.*;
import java.util.List;
import java.util.ArrayList;

public class IfCases extends ASTNode{
    public List<Case> cases = new ArrayList<>();
    public ElseCase elseCase;

    public IfCases(List<Case> cases, ElseCase elseCase){
        this.cases = cases;
        this.elseCase = elseCase;
    }
}
