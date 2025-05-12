package Operators;

import java.util.ArrayList;
import Position.*;

public class CallNode extends ASTNode{
    private ASTNode nodeToCall;
    private ArrayList<ASTNode> argNodes;
    private Position positionStart;
    private Position positionEnd;
    
    public CallNode(ASTNode nodeToCall, ArrayList<ASTNode> argNodes){
        this.nodeToCall = nodeToCall;
        this.argNodes = argNodes;
        this.positionStart = nodeToCall.getPositionStart();

        if(!argNodes.isEmpty()) this.positionEnd = argNodes.get(argNodes.size()-1).getPositionEnd();
        else this.positionEnd = nodeToCall.getPositionEnd();
    }

    public ASTNode getNodeToCall() {
        return nodeToCall;
    }

    public void setNodeToCall(ASTNode nodeToCall) {
        this.nodeToCall = nodeToCall;
    }

    public ArrayList<ASTNode> getArgNodes() {
        return argNodes;
    }

    public void setArgNodes(ArrayList<ASTNode> argNodes) {
        this.argNodes = argNodes;
    }

    public Position getPositionStart() {
        return positionStart;
    }

    public void setPositionStart(Position posStart) {
        this.positionStart = posStart;
    }

    public Position getPositionEnd() {
        return positionEnd;
    }

    public void setPosEnd(Position posEnd) {
        this.positionEnd = posEnd;
    }
}
