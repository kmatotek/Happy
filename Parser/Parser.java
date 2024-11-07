package Parser;
import java.util.ArrayList;
import java.util.function.Supplier;
import java.util.List;
import java.util.Arrays;

import Errors.*;
import Operators.*;
//import Position.Position;
import Token.*;
import DataStructures.*;

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
        updateCurrToken();
        return currToken;
    }

    public void updateCurrToken(){
        if(this.currTokenIndex >= 0 && this.currTokenIndex < this.tokens.size()){
            this.currToken = this.tokens.get(this.currTokenIndex);
        }
    }

    public ASTNode ifExpression(){
        IfCases allCases = (IfCases) this.ifExpressionCases("if");
        
        return new IfNode(allCases.cases,allCases.elseCase);
    }

    public IfCases ifExpressionCases(String keyword){    
        //ParseResult res = new ParseResult();
        List<Case> cases = new ArrayList<>();
        ElseCase elseCase = null;

        //System.out.println(this.currToken.value);
        if(!this.currToken.matches(Token.TT_KEYWORD,keyword)){
            
            throw new IllegalArgumentException("Expected 'if'");
        }
        
        this.advance();
        
        ASTNode condition = this.expression();
        
        if(!this.currToken.matches(Token.TT_KEYWORD,"then")){
            throw new IllegalArgumentException("Expected 'then'");
        }

        this.advance();
       
        
        if(this.currToken.type.equals(Token.TT_NEWLINE)){
            
            this.advance();
            
            ASTNode statements = this.statements();
            
            
            cases.add(new Case(condition,statements,true));
            

            if(this.currToken.matches(Token.TT_KEYWORD, "end")){
                this.advance();
            } else {
             
                
                IfCases allCases = (IfCases) this.ifExprBorC();
                
                List<Case> newCases = allCases.cases;
                elseCase = allCases.elseCase;
                cases.addAll(newCases);
                
            }
        } else {
            ASTNode expr = this.expression();
            cases.add(new Case(condition, expr, false));

            IfCases allCases = this.ifExprBorC();
            
            List<Case> newCases = allCases.cases;
            elseCase = allCases.elseCase;
           // Printing null System.out.println(elseCase);
            cases.addAll(newCases);
            
        }     
        
        return new IfCases(cases,elseCase);
    }

    public IfCases ifExprB(){
        return this.ifExpressionCases("elif");
    }

    public ElseCase ifExprC(){
        ElseCase elseCase = null;
        
        if(this.currToken.matches(Token.TT_KEYWORD, "else")){
            this.advance();

            if(this.currToken.type.equals(Token.TT_NEWLINE)){
                this.advance();

                ASTNode statements = this.statements();
                elseCase = new ElseCase(statements, true);

                if(this.currToken.matches(Token.TT_KEYWORD, "end")){
                    this.advance();
                } else {
                    throw new InvalidSyntaxError(this.currToken.positionStart,this.currToken.positionEnd,"expected 'end'");
                }
            } else {
                ASTNode expr = this.expression();
                
                elseCase = new ElseCase(expr,false);
            }
        }
        return elseCase;
    }

    public IfCases ifExprBorC(){
        List<Case> cases = new ArrayList<>();
        ElseCase elseCase = null;

        if(this.currToken.matches(Token.TT_KEYWORD, "elif")){
            IfCases allCases = this.ifExprB();
            cases = allCases.cases;
            elseCase = allCases.elseCase;
        } else {
            elseCase = this.ifExprC();
        }
        
        return new IfCases(cases, elseCase);

    }

    public ASTNode forExpression(){

        if(!this.currToken.matches(Token.TT_KEYWORD,"for")){
            throw new IllegalCharError("Expected for");
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
        
        

        if(!this.currToken.matches(Token.TT_KEYWORD,"to")){
            throw new InvalidSyntaxError(this.currToken.positionStart,this.currToken.positionEnd,"Expected TO");
        }
        this.advance();
        ASTNode endValue = this.expression();
        
        ASTNode stepValue = null;

        if(this.currToken.matches(Token.TT_KEYWORD, "step")){
            this.advance();
            stepValue = this.expression();
        }

        if(!this.currToken.matches(Token.TT_KEYWORD, "then")){
            throw new InvalidSyntaxError(this.currToken.positionStart,this.currToken.positionEnd,"Expected THEN");
        }
        this.advance();
        
        if(this.currToken.type.equals(Token.TT_NEWLINE)){
            this.advance();
            ASTNode body = this.statements();
            if(!this.currToken.matches(Token.TT_KEYWORD,"end")){
                throw new InvalidSyntaxError(this.currToken.positionStart,this.currToken.positionEnd,"Expected 'end'");

            }
            this.advance();
            return new ForNode(varName, body, startValue, endValue, stepValue, true);
        }

        ASTNode body = this.expression();

        ForNode resultForNode = null;
        resultForNode = new ForNode(varName,body, startValue, endValue, stepValue, false);

        return resultForNode;
    }

    public ASTNode whileExpression(){
        //ParseResult res = new ParseResult();
        if(!this.currToken.matches(Token.TT_KEYWORD, "while")){
            throw new InvalidSyntaxError(this.currToken.positionStart,this.currToken.positionEnd,"Expected WHILE");
        }
        this.advance();

        ASTNode condition = this.expression();
        if(!this.currToken.matches(Token.TT_KEYWORD, "then")){
            throw new InvalidSyntaxError(this.currToken.positionStart,this.currToken.positionEnd,"Expected THEN");
        }

        this.advance();
       
        if(this.currToken.type.equals(Token.TT_NEWLINE)){
            this.advance();
            ASTNode body = this.statements();

            if(!this.currToken.matches(Token.TT_KEYWORD,"end")){
                throw new InvalidSyntaxError(this.currToken.positionStart, this.currToken.positionEnd,"Expected end");
            }
            this.advance();

            return new WhileNode(condition, body, true);
           
        }
        

        
        ASTNode body = this.expression();
        
    
        return (new WhileNode(condition,body,false));
        
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
                    System.out.println("failed in call");
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

        } else if(token.type.equals(Token.TT_STRING)){
            parseResult.register(this.advance());
            
            return parseResult.success(new StringNode(token));

        } else if (token.type.equals(Token.TT_IDENTIFIER)){
            //System.out.println(token.value);
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
        } else if (token.type.equals(Token.TT_LSQUAREB)){
            ASTNode listExpr = parseResult.register(this.listExpression());
            return listExpr;
        } else if(token.matches(Token.TT_KEYWORD,"if")){
            ASTNode ifExpr = parseResult.register(this.ifExpression());
            //System.out.println(ifExpr);
            return ifExpr;
        } else if (token.matches(Token.TT_KEYWORD,"for")){
            ASTNode forExpr = parseResult.register(this.forExpression());
            return forExpr;
        }  else if (token.matches(Token.TT_KEYWORD,"while")){
            ASTNode WhileExpr = parseResult.register(this.whileExpression());
            return WhileExpr;
        } else if (token.matches(Token.TT_KEYWORD,"func")){
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
        return this.binOp(this::term, Arrays.asList(Token.TT_PLUS,Token.TT_MINUS, Token.TT_EXCLM));
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
        
        return this.binOp(this::compExpression, Arrays.asList("and","or"));           
    }

    public ASTNode listExpression(){
        ArrayList<ASTNode> elementNodes = new ArrayList<>();

        if(!this.currToken.type.equals(Token.TT_LSQUAREB)){
            throw new InvalidSyntaxError(this.currToken.positionStart,this.currToken.positionEnd,"Expected '[");
        }
        this.advance();
        
        if(this.currToken.type.equals(Token.TT_RSQUAREB)){
            this.advance();
        } else {
            elementNodes.add(this.expression());

            while(this.currToken.type.equals(Token.TT_COMMA)){
                this.advance();
                elementNodes.add(this.expression());
            }

            if(!this.currToken.type.equals(Token.TT_RSQUAREB)){
                
                throw new InvalidSyntaxError(this.currToken.positionStart,this.currToken.positionEnd, "Expected ',' or ']'");
            }
            this.advance();
        }
        if(this.currToken.positionEnd == null || this.currToken.positionStart == null) return new ListNode(elementNodes);
        else return new ListNode(elementNodes);
    }

    // binOp accepts a parsing function to handle the correct precedence levels
    public ASTNode binOp(Supplier<ASTNode> parseFunc, List<String> ops){
        ParseResult parseResult = new ParseResult();
        ASTNode left = parseResult.register(parseFunc.get());
          // Call the provided parse function (e.g., factor or term)
        if(parseResult.error != null) throw new InvalidSyntaxError(this.currToken.positionStart,this.currToken.positionEnd,"You fond a hidden error!");
        
        while(ops.contains(this.currToken.type) || ops.contains(this.currToken.value)){
            Token<?> opToken = this.currToken;
            parseResult.register(this.advance());
            ASTNode right = parseResult.register(parseFunc.get());
            // Use the same parse function for the right side
            if(parseResult.error != null) throw new InvalidSyntaxError(opToken.positionStart,opToken.positionEnd,"You fond a hidden error!");

            left = new BinOpNode(left, opToken, right);
           
        }
        return parseResult.success(left);
    }

    public ASTNode binOp(Supplier<ASTNode> parseFuncA, Supplier<ASTNode> parseFuncB, List<String> ops){
       
        ParseResult parseResult = new ParseResult();
        ASTNode left = parseResult.register(parseFuncA.get());
        
          // Call the provided parse function (e.g., factor or term)
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

        if(this.currToken.value.equals("not")){
            opToken = this.currToken;
            result.register(this.advance());
            ASTNode node = result.register(this.compExpression());
            return new UnaryOpNode(opToken, node);
        }

        
        ASTNode node = result.register(this.binOp(this::artithExpression, Arrays.asList(Token.TT_EE,Token.TT_NE,Token.TT_LT,Token.TT_GT,Token.TT_LTE,Token.TT_GTE)));
        return result.success(node);
    }


    public ASTNode parse(){
        ASTNode res = this.statements();
        if(this.currToken.type != Token.TT_EOF){
            throw new InvalidSyntaxError(this.currToken.positionStart, this.currToken.positionEnd, "Extecpted '+', '-', '*', or '/'");
        }
        
        return res;
    }

    public ASTNode funcDef(){
       // ParseResult res = new ParseResult();

        if(!this.currToken.matches(Token.TT_KEYWORD,"func")){
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
    public ListNode statements(){
        //ParseResult res = new ParseResult();
        ArrayList<ASTNode> statements = new ArrayList<>();
       // Position posStart = this.currToken.positionStart.copy();

        while(this.currToken.type.equals(Token.TT_NEWLINE)){
            this.advance();
        }

        boolean moreStatements = true;
        ASTNode statement = this.expression();
        statements.add(statement);

        while(true){
            int newLineCount = 0;
            while(this.currToken.type.equals(Token.TT_NEWLINE)){
                this.advance();
                newLineCount += 1;
            }
            if (newLineCount == 0){
                moreStatements = false;
            }
            if (!moreStatements) break;
            
            if(this.currToken.type.equals(Token.TT_EOF)){
                moreStatements = false;
                //this.reverse(res.toReverseCount);
                continue;
            } else {
                statement = this.expression();
                
                statements.add(statement);
                
            }
        }


        return new ListNode(statements);
    }
    /** 
    public ASTNode statement(){
        Position posStart = this.currToken.positionStart.copy();
        if(this.currToken.matches(Token.TT_KEYWORD, "return")){
            this.advance();
            // add try register
            ASTNode expr = this.expression();

            
        }

        if(this.currToken.matches(Token.TT_KEYWORD, "continue")){
            this.advance();
            return new ContinueNode(posStart, this.currToken.positionStart.copy());
        }

        if(this.currToken.matches(Token.TT_KEYWORD, "break")){
            return new BreakNode(posStart, this.currToken.positionStart.copy());
        }

        ASTNode expr = this.expression();


        return expr;
    }
    **/
}
