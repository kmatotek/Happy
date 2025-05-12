package Operators;

import Position.*;
import java.util.ArrayList;


public class ListNode extends ASTNode {
    private ArrayList<ASTNode> elementNodes;
    private Position positionStart;
    private Position positionEnd;

    public ListNode(ArrayList<ASTNode> elementNodes){
        this.elementNodes = elementNodes;
    }
    
    public ListNode(ArrayList<ASTNode> elementNodes, Position posStart, Position posEnd){
        this.elementNodes = elementNodes;
        this.positionStart = posStart;
        this.positionEnd = posEnd;
    }

    public ArrayList<ASTNode> getElementNodes() {
        return elementNodes;
    }

    public void setElementNodes(ArrayList<ASTNode> elementNodes) {
        this.elementNodes = elementNodes;
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
