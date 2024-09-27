package Parser;
import java.util.ArrayList;
import java.util.function.Supplier;

import Errors.*;
import Operators.*;
import Token.*;

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

    public ASTNode atom(){
        ParseResult parseResult = new ParseResult();
        Token<?> token = currToken;

        if(token.type.equals(Token.TT_INT) || token.type.equals(Token.TT_FLOAT)){
            parseResult.register(this.advance());

            return parseResult.success(new NumberNode(token));

        } else if (token.type.equals(Token.TT_IDENTIFIER)){
            parseResult.register(this.advance());
            return parseResult.success(new VarAccessNode(token));
        }
        
        else if (token.type.equals(Token.TT_LPAREN)){
            parseResult.register(this.advance());
            ASTNode expr = parseResult.register(this.expression());
            
            if(parseResult.error != null) throw new InvalidSyntaxError(Token.positionStart,Token.positionEnd,"You fond a hidden error!");
            
            if(this.currToken.type.equals(Token.TT_RPAREN)){
                parseResult.register(this.advance());
                return (BinOpNode) parseResult.success(expr);
            } else {
                throw new InvalidSyntaxError(Token.positionStart,Token.positionEnd,"Expected ')'");
            }
        } 
        else throw new IllegalCharError("Expected int or float but got " + this.currToken.type);
    }

    public ASTNode power(){
        return this.binOp(this::atom, this::factor, Token.TT_POW, Token.TT_POW);
        
    }

    public ASTNode factor(){
        ParseResult parseResult = new ParseResult();
        Token<?> token = currToken;
            
        if(token.type.equals(Token.TT_PLUS) || token.type.equals(Token.TT_MINUS)){
            parseResult.register(this.advance());
            ASTNode factor = parseResult.register(this.factor());
            if(parseResult.error != null){
                throw new InvalidSyntaxError(Token.positionStart,Token.positionEnd,"You fond a hidden error!");
            }
            return parseResult.success(new UnaryOpNode(token, factor));
        } 
    
        
        return this.power();
        //throw new InvalidSyntaxError(Token.positionStart,Token.positionEnd,"Expected int or float");
    }

    public ASTNode term(){
        // Handle multiplication and division first (higher precedence)
        return this.binOp(this::factor, Token.TT_MUL, Token.TT_DIV);           
    }

    public ASTNode expression(){
        // Handle addition and subtraction (lower precedence)
        ParseResult res = new ParseResult();
        if(this.currToken.matches(Token.TT_KEYWORD, "var")){
            res.register(this.advance());

            if(!this.currToken.type.equals(Token.TT_IDENTIFIER)){
                throw new IllegalCharError("Expected identifier");
            }
            Token<?> varName = this.currToken;
            res.register(this.advance());

            if(!this.currToken.type.equals(Token.TT_EQ)){
                throw new IllegalCharError("Excpected =");
            }

            res.register(this.advance());
            ASTNode expr = res.register(this.expression());
            return res.success(new VarAssignNode(varName, expr));
        }

        return this.binOp(this::term, Token.TT_PLUS, Token.TT_MINUS);           
    }

    // binOp accepts a parsing function to handle the correct precedence levels
    public ASTNode binOp(Supplier<ASTNode> parseFunc, String op1, String op2){
        ParseResult parseResult = new ParseResult();
        ASTNode left = parseResult.register(parseFunc.get());  // Call the provided parse function (e.g., factor or term)
        if(parseResult.error != null) throw new InvalidSyntaxError(Token.positionStart,Token.positionEnd,"You fond a hidden error!");
        
        while(currToken.type.equals(op1) || currToken.type.equals(op2)){
            Token opToken = currToken;
            parseResult.register(this.advance());
            ASTNode right = parseResult.register(parseFunc.get());  // Use the same parse function for the right side
            if(parseResult.error != null) throw new InvalidSyntaxError(Token.positionStart,Token.positionEnd,"You fond a hidden error!");

            left = new BinOpNode(left, opToken, right);
        }
        return parseResult.success(left);
    }

    public ASTNode binOp(Supplier<ASTNode> parseFuncA, Supplier<ASTNode> parseFuncB,String op1, String op2){
        ParseResult parseResult = new ParseResult();
        ASTNode left = parseResult.register(parseFuncA.get());  // Call the provided parse function (e.g., factor or term)
        if(parseResult.error != null) throw new InvalidSyntaxError(Token.positionStart,Token.positionEnd,"You fond a hidden error!");
        
        while(currToken.type.equals(op1) || currToken.type.equals(op2)){
            Token opToken = currToken;
            parseResult.register(this.advance());
            ASTNode right = parseResult.register(parseFuncB.get());  // Use the same parse function for the right side
            if(parseResult.error != null) throw new InvalidSyntaxError(Token.positionStart,Token.positionEnd,"You fond a hidden error!");

            left = new BinOpNode(left, opToken, right);
        }
        return parseResult.success(left);
    }

    public ASTNode parse(){
        ASTNode res = this.expression();
        if(this.currToken.type != Token.TT_EOF){
            throw new InvalidSyntaxError(this.currToken.positionStart, this.currToken.positionEnd, "Extecpted '+', '-', '*', or '/'");
        }
        return res;
    }
}