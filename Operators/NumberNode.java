package Operators;
import Token.*;


public class NumberNode extends ASTNode{
    public Token token;
    
    public NumberNode(Token token){
        this.token = token;
    }

    public String toString(){
        return token.value.toString();
    }
}
