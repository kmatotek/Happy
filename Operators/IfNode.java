package Operators;

import java.util.List;
import Position.*;


public class IfNode extends ASTNode {
    public List<List<ASTNode>> cases;
    public ASTNode elseCase;
    public Position posStart;
    public Position posEnd;

    public IfNode(List<List<ASTNode>> cases, ASTNode elseCase){
        this.cases = cases;
        this.elseCase = elseCase;
        posStart = cases.get(0).get(0).positionStart;
        if(elseCase != null){
            this.posEnd = elseCase.positionEnd;
        } else {
            this.posEnd = this.cases.get(cases.size()-1).get(0).positionEnd;
        }
    }
}
