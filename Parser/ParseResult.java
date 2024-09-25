package Parser;
import Operators.*;
import Token.*;

public class ParseResult {
    public Error error;
    public ASTNode node;

    public ParseResult(){
        this.error = null;
        this.node = null;
    }

    public ASTNode register(ParseResult parseResult){ // Takes in parse result 
        if(parseResult.error != null){
            
            return parseResult.node;
        }
        return null;
    }

    public ASTNode register(ASTNode node){ // Takes in Node 
        return node;
    }

    public Token register(Token token){ // Takes in Node 
        return token;
    }

    public ASTNode success(ASTNode node){
        this.node = node;
        return this.node;
    }

    public void failure(Error error){
        this.error = error;
    }
}
