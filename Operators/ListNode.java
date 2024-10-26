package Operators;

import Position.*;
import java.util.ArrayList;


public class ListNode extends ASTNode {
    public ArrayList<ASTNode> elementNodes;
    public Position posStart;
    public Position posEnd;

    public ListNode(ArrayList<ASTNode> elementNodes){
        this.elementNodes = elementNodes;
    }
    
    public ListNode(ArrayList<ASTNode> elementNodes, Position posStart, Position posEnd){
        this.elementNodes = elementNodes;
        this.posStart = posStart;
        this.posEnd = posEnd;
    }

}
