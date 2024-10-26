package Operators;

import java.util.ArrayList;
import Position.*;

public class CallNode extends ASTNode{
    public ASTNode nodeToCall;
    public ArrayList<ASTNode> argNodes;
    public Position posStart;
    public Position posEnd;
    
    public CallNode(ASTNode nodeToCall, ArrayList<ASTNode> argNodes){
        this.nodeToCall = nodeToCall;
        this.argNodes = argNodes;
        this.posStart = nodeToCall.positionStart;

        if(argNodes.size() > 0) this.posEnd = argNodes.get(argNodes.size()-1).positionEnd;
        else this.posEnd = nodeToCall.positionEnd;
    }
}
