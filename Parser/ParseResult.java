package Parser;
import Operators.*;
import DataStructures.*;


public class ParseResult {
    public Error error;
    public ASTNode node;
    public ElseCase elseCase;
    public int lastRegisteredAdvanceCount = 0;
    public int advanceCount = 0;
    public int toReverseCount = 0;

    public ParseResult(){
        this.error = null;
        this.node = null;
    }

    public ASTNode register(ParseResult parseResult){ // Takes in parse result 
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


    public void failure(Error error){
        this.error = error;
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
