package Parser;
import Operators.*;
import Token.*;

public class ParseResult {
    public Error error;
    public ASTNode node;
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

    public ASTNode register(ASTNode node){ // Takes in Node 
        return node;
    }

    public Token<?> register(Token<?> token){ // Takes in Node 
        return token;
    }

    public ASTNode success(ASTNode node){
        this.node = node;
        return this.node;
    }

    public void failure(Error error){
        this.error = error;
    }

    public ASTNode tryRegister(ParseResult res){
        return this.register(res);
    }
}
