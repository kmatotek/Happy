package Operators;
import Token.*;
import Position.*;
import java.util.ArrayList;

public class FuncDefNode extends ASTNode {
    private Token<?> varNameTok;
    private ArrayList<Token<?>> argNameTokens = new ArrayList<>();
    private ASTNode bodyNode;
    private Position positionStart;
    private Position positionEnd;
    private boolean shouldAutoReturn;

    
    public FuncDefNode(Token<?> varNameTok, ArrayList<Token<?>> argNameTokens, ASTNode bodyNode, boolean autoRet){
        this.varNameTok = varNameTok;
        this.argNameTokens = argNameTokens;
        this.bodyNode = bodyNode;
        this.shouldAutoReturn = autoRet;

        if(!argNameTokens.isEmpty()) this.positionStart = argNameTokens.get(0).positionStart;
        else this.positionStart = bodyNode.getPositionStart();
        this.positionEnd = bodyNode.getPositionEnd();

    }

    public Token<?> getVarNameTok() {
        return varNameTok;
    }

    public void setVarNameTok(Token<?> varNameTok) {
        this.varNameTok = varNameTok;
    }

    public ArrayList<Token<?>> getArgNameTokens() {
        return argNameTokens;
    }

    public void setArgNameTokens(ArrayList<Token<?>> argNameTokens) {
        this.argNameTokens = argNameTokens;
    }

    public ASTNode getBodyNode() {
        return bodyNode;
    }

    public void setBodyNode(ASTNode bodyNode) {
        this.bodyNode = bodyNode;
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

    public boolean isShouldAutoReturn() {
        return shouldAutoReturn;
    }

    public void setShouldAutoReturn(boolean shouldAutoReturn) {
        this.shouldAutoReturn = shouldAutoReturn;
    }
}
