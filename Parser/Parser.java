package Parser;
import java.util.ArrayList;
import java.util.function.Supplier;
import Operators.NumberNode;
import Token.*;
import Operators.*;

public class Parser {
    public ArrayList<Token<?>> tokens;
    public int currTokenIndex;
    public Token currToken;
    public Parser(ArrayList<Token<?>> tokens){
        this.tokens = tokens;
        this.currTokenIndex = -1;
        this.advance();
    }
    public Token advance(){
        currTokenIndex += 1;
        if(currTokenIndex < tokens.size()){
            this.currToken = tokens.get(currTokenIndex);
        }
        return currToken;
    }
    public NumberNode factor(){
        Token token = currToken;
        if(token.type.equals(Token.TT_INT) || token.type.equals(Token.TT_FLOAT)){
            this.advance();
            return new NumberNode(token);
        }
        return null;
    }
    public ASTNode term(){
        // Handle multiplication and division first (higher precedence)
        return this.binOp(this::factor, Token.TT_MUL, Token.TT_DIV);           
    }
    public ASTNode expression(){
        // Handle addition and subtraction (lower precedence)
        return this.binOp(this::term, Token.TT_PLUS, Token.TT_MINUS);           
    }
    // binOp accepts a parsing function to handle the correct precedence levels
    public ASTNode binOp(Supplier<ASTNode> parseFunc, String op1, String op2){
        ASTNode left = parseFunc.get();  // Call the provided parse function (e.g., factor or term)
        while(currToken.type.equals(op1) || currToken.type.equals(op2)){
            Token opToken = currToken;
            this.advance();
            ASTNode right = parseFunc.get();  // Use the same parse function for the right side
            left = new BinOpNode(left, opToken, right);
        }
        return left;
    }
    public ASTNode parse(){
        return this.expression();  // Start parsing with the lowest precedence (expression)
    }
}