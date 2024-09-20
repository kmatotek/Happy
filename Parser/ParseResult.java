package Parser;

import Operators.*;

public class ParseResult {
    public Error error;
    public ASTNode node;

    public ParseResult(){
        
    }

    public ASTNode register(ParseResult parseResult){
        if(parseResult.error != null){
            this.error = parseResult.error;
        }
        return parseResult.node;
    }
    
    public ASTNode register(ASTNode node){
        if (node == null) {
            this.error = new Error("Error during parsing");
            return null;
        }
        this.node = node;
        return node;
    }

    public ASTNode success(ASTNode node){
        this.node = node;
        return this.node;
    }

    public Error failure(Error error){
        this.error = error;
        return this.error;
    }

}
