package Parser;
import java.util.ArrayList;
import java.util.function.Supplier;

import Errors.InvalidSyntaxError;
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
        ParseResult parseResult = new ParseResult();
        Token token = currToken;

        if(token.type.equals(Token.TT_INT) || token.type.equals(Token.TT_FLOAT)){
            // TODO parseResult.register(this.advance());
            return (NumberNode) parseResult.success(new NumberNode(token));
        }
        throw new InvalidSyntaxError(token.positionStart, token.positionEnd, "Expected int or float");
        
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
        ParseResult parseResult = new ParseResult();
        ASTNode left = parseResult.register(parseFunc.get());  // Call the provided parse function (e.g., factor or term)
       
        if(parseResult.error != null) throw new RuntimeException(parseResult.error.getMessage());;

        while(currToken.type.equals(op1) || currToken.type.equals(op2)){
            Token opToken = currToken; 
            this.advance();
            ASTNode right = parseFunc.get();  // Use the same parse function for the right side
            left = new BinOpNode(left, opToken, right);
        }
        return parseResult.success(left);
    }

    public ASTNode parse(){
        ASTNode result = this.expression();
        if(result == null && this.currToken.type != Token.TT_EOF){
            throw new InvalidSyntaxError(this.currToken.positionStart, this.currToken.positionEnd, "Expected '+', '-', '*', or '/'");
        }
        return this.expression();  // Start parsing with the lowest precedence (expression)
    }
}
