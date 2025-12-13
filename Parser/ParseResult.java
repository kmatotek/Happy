package Parser;
import Operators.*;

public class ParseResult {
    private Error error;
    private ASTNode node;
    private int lastRegisteredAdvanceCount = 0;
    private int advanceCount = 0;
    private int toReverseCount = 0;

    public ParseResult(){
        this.error = null;
        this.node = null;
    }

    public Error getError() {
        return error;
    }

    public int getToReverseCount() {
        return toReverseCount;
    }

    public ASTNode getNode() {
        return node;
    }
    
    public ASTNode register(ParseResult parseResult){
        if(parseResult.error != null){  
            return parseResult.node;
        }
        this.lastRegisteredAdvanceCount = parseResult.advanceCount;
        this.advanceCount += 1;
        return parseResult.node;
    }

    public ParseResult success(ASTNode node){
        this.node = node;
        return this;
    }

    public ASTNode tryRegister(ParseResult res){
        if(res.error != null) this.toReverseCount = res.advanceCount;
        return this.register(res);
    }

    public void registerAdvancement(){
        this.lastRegisteredAdvanceCount = 1;
        this.advanceCount += 1;
    }
}
