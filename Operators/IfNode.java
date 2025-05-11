package Operators;

import java.util.List;
import Position.*;
import DataStructures.*;


public class IfNode extends ASTNode {
    public List<Case> cases;
    public ElseCase elseCase;
    public Position posStart;
    public Position posEnd;

    public IfNode(List<Case> cases, ElseCase elseCase){
        this.cases = cases;
        this.elseCase = elseCase;
        posStart = cases.get(0).positionStart;
        if(elseCase != null){
            this.posEnd = elseCase.getElseCase().positionEnd;
        } else {
            this.posEnd = this.cases.get(cases.size()-1).positionEnd;
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
}
