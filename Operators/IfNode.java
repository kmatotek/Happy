package Operators;

import java.util.List;
import Position.*;
import DataStructures.*;

public class IfNode extends ASTNode {
    private List<Case> cases;
    private ElseCase elseCase;
    private Position positionStart;
    private Position positionEnd;

    public IfNode(List<Case> cases, ElseCase elseCase){
        this.cases = cases;
        this.elseCase = elseCase;
        positionStart = cases.get(0).getPositionStart();
        if(elseCase != null){
            this.positionEnd = elseCase.getElseCase().getPositionEnd();
        } else {
            this.positionEnd = this.cases.get(cases.size()-1).getPositionEnd();
        }
    }

    public String toString(){
        String ans = "";
        for(Case c : this.cases){
            ans += c.toString();
            ans += " ";
        }
        return ans;
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
