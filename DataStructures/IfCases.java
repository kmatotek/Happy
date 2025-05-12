package DataStructures;
import Operators.*;
import Position.Position;

import java.util.List;
import java.util.ArrayList;

public class IfCases extends ASTNode{
    private List<Case> cases = new ArrayList<>();
    private ElseCase elseCase;
    private Position positionStart;
    private Position positionEnd;

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
