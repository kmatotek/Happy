package Operators;
import Token.*;
import Position.*;
import java.util.ArrayList;

public class FuncDefNode extends ASTNode {
    public Token<?> varNameTok;
    public ArrayList<Token<?>> argNameTokens = new ArrayList<>();
    public ASTNode bodyNode;
    public Position posStart;
    public Position posEnd;

    
    public FuncDefNode(Token<?> varNameTok, ArrayList<Token<?>> argNameTokens, ASTNode bodyNode){
        this.varNameTok = varNameTok;
        this.argNameTokens = argNameTokens;
        this.bodyNode = bodyNode;

        if(argNameTokens.size() > 0) this.posStart = argNameTokens.get(0).positionStart;
        else this.posStart = bodyNode.positionStart;
        this.posEnd = bodyNode.positionEnd;

    }


}
