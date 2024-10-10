package Parser;
import java.util.ArrayList;
import java.util.function.Supplier;
import java.util.List;
import java.util.Arrays;

import Errors.*;
import Operators.*;
import Token.*;

public class Parser {

    public ArrayList<Token<?>> tokens;
    public int currTokenIndex;
    public Token<?> currToken;
    public Parser(ArrayList<Token<?>> tokens){
        this.tokens = tokens;
        this.currTokenIndex = -1;
        this.advance();
    }

    public Token<?> advance(){
        currTokenIndex += 1;
        if(currTokenIndex < tokens.size()){
            this.currToken = tokens.get(currTokenIndex);
        }
        return currToken;
    }

    public ASTNode ifExpression(){
        ParseResult res = new ParseResult();
        List<List<ASTNode>> cases = new ArrayList<>();
        ASTNode elseCase = null;

        if(!this.currToken.matches(Token.TT_KEYWORD,"IF")){
            throw new IllegalArgumentException("Expected 'IF'");
        }

        this.advance();
        ASTNode condition = res.register(this.expression());

        if(!this.currToken.matches(Token.TT_KEYWORD,"THEN")){
            throw new IllegalArgumentException("Expected 'THEN'");
        }

        this.advance();
        ASTNode expr = res.register(this.expression());
        cases.add(Arrays.asList(condition,expr));

        while(this.currToken.matches(Token.TT_KEYWORD, "ELIF")){
            this.advance();

            condition = res.register(this.expression());
            if(!this.currToken.matches(Token.TT_KEYWORD,"THEN")){
                throw new IllegalArgumentException("Expected 'THEN'");
            }
            
            this.advance();
            expr = res.register(this.expression());
            cases.add(Arrays.asList(condition,expr));

        }

        if(this.currToken.matches(Token.TT_KEYWORD,"ELSE")){
            this.advance();
            expr = res.register(this.expression());
            elseCase = expr;
        }

        return res.success(new IfNode(cases,elseCase));


    }

    public ASTNode forExpression(){
        ParseResult res = new ParseResult();

        if(!this.currToken.matches(Token.TT_KEYWORD,"FOR")){
            throw new IllegalCharError("Expected FOR");
        }
        this.advance();
        if(this.currToken.type != Token.TT_IDENTIFIER){
            throw new IllegalCharError("Expected identifier");
        }

        Token<?> varName = this.currToken;
        this.advance();

        if(this.currToken.type != Token.TT_EQ){
            throw new InvalidSyntaxError(this.currToken.positionStart,this.currToken.positionEnd,"Expected identifier");
        }
        this.advance();
        ASTNode startValue = this.expression();
        

        if(!this.currToken.matches(Token.TT_KEYWORD,"TO")){
            throw new InvalidSyntaxError(this.currToken.positionStart,this.currToken.positionEnd,"Expected TO");
        }
        this.advance();
        ASTNode endValue = this.expression();

        ASTNode stepValue = null;

        if(this.currToken.matches(Token.TT_KEYWORD, "STEP")){
            this.advance();
            stepValue = this.expression();
        }

        if(!this.currToken.matches(Token.TT_KEYWORD, "THEN")){
            throw new InvalidSyntaxError(this.currToken.positionStart,this.currToken.positionEnd,"Expected THEN");
        }
        this.advance();

        ASTNode body = this.expression();

        ForNode resultForNode = null;
        if(stepValue == null) resultForNode = new ForNode(varName, body, startValue, endValue);
        else resultForNode = new ForNode(varName,body, startValue, endValue, stepValue);

        return res.success(resultForNode);
    }

    public ASTNode whileExpression(){
        ParseResult res = new ParseResult();
        if(!this.currToken.matches(Token.TT_KEYWORD, "WHILE")){
            throw new InvalidSyntaxError(this.currToken.positionStart,this.currToken.positionEnd,"Expected WHILE");
        }
        this.advance();

        ASTNode condition = this.expression();
        if(!this.currToken.matches(Token.TT_KEYWORD, "THEN")){
            throw new InvalidSyntaxError(this.currToken.positionStart,this.currToken.positionEnd,"Expected THEN");
        }
        this.advance();
        ASTNode body = this.expression();

        return res.success(new WhileNode(condition,body));
        
    }

    public ASTNode call(){
        //ParseResult res = new ParseResult();
        ASTNode atom = this.atom();

        if(this.currToken.type.equals(Token.TT_LPAREN)){
            this.advance();
            ArrayList<ASTNode> argNodes = new ArrayList<>();

            if(this.currToken.type.equals(Token.TT_RPAREN)){
                this.advance();
            } else {
                argNodes.add(this.expression());

                while(this.currToken.type.equals(Token.TT_COMMA)){
                    this.advance();
                    argNodes.add(this.expression());

                }

                if(!this.currToken.type.equals(Token.TT_RPAREN)){
                     throw new InvalidSyntaxError(this.currToken.positionStart,this.currToken.positionEnd, "Expected ',' or ')'");
                }
                this.advance();
            }
            return new CallNode(atom,argNodes);
        }
        return atom;
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
            
            if(parseResult.error != null) throw new InvalidSyntaxError(token.positionStart,token.positionEnd,"You fond a hidden error!");
            
            if(this.currToken.type.equals(Token.TT_RPAREN)){
                parseResult.register(this.advance());
                return parseResult.success(expr);
            } else {
                throw new InvalidSyntaxError(token.positionStart, token.positionEnd,"Expected ')'");
            }
        } else if(token.matches(Token.TT_KEYWORD,"IF")){
            ASTNode ifExpr = parseResult.register(this.ifExpression());
            return ifExpr;
        } else if (token.matches(Token.TT_KEYWORD,"FOR")){
            ASTNode forExpr = parseResult.register(this.forExpression());
            return forExpr;
        }  else if (token.matches(Token.TT_KEYWORD,"WHILE")){
            ASTNode WhileExpr = parseResult.register(this.whileExpression());
            return WhileExpr;
        } else if (token.matches(Token.TT_KEYWORD,"FUNC")){
            ASTNode FuncDef = parseResult.register(this.funcDef());
            return FuncDef;
        }
        else throw new IllegalCharError("Expected int or float but got " + this.currToken.type);
    }

    public ASTNode power(){
        return this.binOp(this::call, this::factor, Arrays.asList("POW"));
        
    }

    public ASTNode factor(){
        ParseResult parseResult = new ParseResult();
        Token<?> token = currToken;
            
        if(token.type.equals(Token.TT_PLUS) || token.type.equals(Token.TT_MINUS)){
            parseResult.register(this.advance());
            ASTNode factor = parseResult.register(this.factor());
            if(parseResult.error != null){
                throw new InvalidSyntaxError(token.positionStart,token.positionEnd,"You fond a hidden error!");
            }
            return parseResult.success(new UnaryOpNode(token, factor));
        } 
    
        
        return this.power();
        //throw new InvalidSyntaxError(Token.positionStart,Token.positionEnd,"Expected int or float");
    }

    public ASTNode term(){
        // Handle multiplication and division first (higher precedence)
        return this.binOp(this::factor, Arrays.asList(Token.TT_MUL, Token.TT_DIV));           
    }

    public ASTNode artithExpression(){
        return this.binOp(this::term,Arrays.asList(Token.TT_PLUS,Token.TT_MINUS));
    }


    public ASTNode expression(){
        // Handle addition and subtraction (lower precedence)
        ParseResult res = new ParseResult();
        if(this.currToken.matches(Token.TT_KEYWORD, "VAR")){
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
        
        return this.binOp(this::compExpression, Arrays.asList("AND","OR"));           
    }

    // binOp accepts a parsing function to handle the correct precedence levels
    public ASTNode binOp(Supplier<ASTNode> parseFunc, List<String> ops){
        ParseResult parseResult = new ParseResult();
        ASTNode left = parseResult.register(parseFunc.get());  // Call the provided parse function (e.g., factor or term)
        if(parseResult.error != null) throw new InvalidSyntaxError(this.currToken.positionStart,this.currToken.positionEnd,"You fond a hidden error!");
        
        while(ops.contains(this.currToken.type) || ops.contains(this.currToken.value)){
            Token<?> opToken = this.currToken;
            parseResult.register(this.advance());
            ASTNode right = parseResult.register(parseFunc.get());  // Use the same parse function for the right side
            if(parseResult.error != null) throw new InvalidSyntaxError(opToken.positionStart,opToken.positionEnd,"You fond a hidden error!");

            left = new BinOpNode(left, opToken, right);
        }
       
        return parseResult.success(left);
    }

    public ASTNode binOp(Supplier<ASTNode> parseFuncA, Supplier<ASTNode> parseFuncB, List<String> ops){
        ParseResult parseResult = new ParseResult();
        ASTNode left = parseResult.register(parseFuncA.get());  // Call the provided parse function (e.g., factor or term)
        if(parseResult.error != null) throw new InvalidSyntaxError(this.currToken.positionStart,this.currToken.positionEnd,"You fond a hidden error!");
        
        while(ops.contains(this.currToken.value) || ops.contains(this.currToken.value)){
            Token<?> opToken = this.currToken;
            parseResult.register(this.advance());
            ASTNode right = parseResult.register(parseFuncB.get());  // Use the same parse function for the right side
            if(parseResult.error != null) throw new InvalidSyntaxError(opToken.positionStart,opToken.positionEnd,"You fond a hidden error!");

            left = new BinOpNode(left, opToken, right);
        }
        return parseResult.success(left);
    }

    public ASTNode compExpression(){
        ParseResult result = new ParseResult();
        Token<?> opToken = null;

        if(this.currToken.value.equals("NOT")){
            opToken = this.currToken;
            result.register(this.advance());
            ASTNode node = result.register(this.compExpression());
            return new UnaryOpNode(opToken, node);
        }

        
        ASTNode node = result.register(this.binOp(this::artithExpression, Arrays.asList(Token.TT_EE,Token.TT_NE,Token.TT_LT,Token.TT_GT,Token.TT_LTE,Token.TT_GTE)));
         
        return result.success(node);
    }


    public ASTNode parse(){
        ASTNode res = this.expression();
        if(this.currToken.type != Token.TT_EOF){
            throw new InvalidSyntaxError(this.currToken.positionStart, this.currToken.positionEnd, "Extecpted '+', '-', '*', or '/'");
        }
        return res;
    }

    public ASTNode funcDef(){
       // ParseResult res = new ParseResult();

        if(!this.currToken.matches(Token.TT_KEYWORD,"FUNC")){
            throw new InvalidSyntaxError(this.currToken.positionStart, this.currToken.positionEnd, "Expected 'FUNC'");
        }
        this.advance();

        Token<?> varNameTok = null;
        if(this.currToken.type.equals(Token.TT_IDENTIFIER)){
            varNameTok = this.currToken;
            this.advance();
            if(!this.currToken.type.equals(Token.TT_LPAREN)){
                throw new InvalidSyntaxError(this.currToken.positionStart, this.currToken.positionEnd, "Expected '('");
            }
        } else {
            if(!this.currToken.type.equals(Token.TT_LPAREN)){
                throw new InvalidSyntaxError(this.currToken.positionStart, this.currToken.positionEnd, "Expected identifier or '('");
            }
        }
        
        this.advance();
        ArrayList<Token<?>> argNameTokens = new ArrayList<>();
        if(this.currToken.type.equals(Token.TT_IDENTIFIER)){
            argNameTokens.add(this.currToken);
            this.advance();

            while(this.currToken.type.equals(Token.TT_COMMA)){
                this.advance();
                if(!this.currToken.type.equals(Token.TT_IDENTIFIER)){
                    throw new InvalidSyntaxError(this.currToken.positionStart, this.currToken.positionEnd, "Expected identifier");
                } else {
                    argNameTokens.add(this.currToken);
                    this.advance();
                }
            }

            if(!this.currToken.type.equals(Token.TT_RPAREN)){
                throw new InvalidSyntaxError(this.currToken.positionStart, this.currToken.positionEnd, "Expected ',' or ')'");
            }

        } else {
            if(!this.currToken.type.equals(Token.TT_RPAREN)){
                throw new InvalidSyntaxError(this.currToken.positionStart, this.currToken.positionEnd, "Expected identifier or ')'");
            }
        }
        this.advance();

        if(!this.currToken.type.equals(Token.TT_ARROW)){
            throw new InvalidSyntaxError(this.currToken.positionStart, this.currToken.positionEnd, "Expected identifier or '->'");
        }
        this.advance();
        ASTNode nodeToReturn = this.expression();

        return new FuncDefNode(varNameTok, argNameTokens, nodeToReturn);
    }
}