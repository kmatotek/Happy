package Parser;
import java.util.ArrayList;
import java.util.function.Supplier;
import java.util.List;
import java.util.Arrays;

import Errors.*;
import Operators.*;
import Position.*;
import Token.*;
import DataStructures.*;

public class Parser {

    private ArrayList<Token<?>> tokens;
    private int currTokenIndex;
    private Token<?> currToken;

    public Parser(ArrayList<Token<?>> tokens){
        this.tokens = tokens;
        this.currTokenIndex = -1;
        this.advance();
    }

    // Moves forward one token
    private Token<?> advance(){
        this.currTokenIndex += 1;
        this.updateCurrToken();
        return this.currToken;
    }

    // Backtrack when parsing fails on ambiguous grammar
    private Token<?> reverse(int amount){
        this.currTokenIndex -= amount;
        this.updateCurrToken();
        return this.currToken;
    }

    private Token<?> reverse(){
        this.currTokenIndex -= 1;
        this.updateCurrToken();
        return this.currToken;
    }

    private void updateCurrToken(){
        if(this.currTokenIndex >= 0 && this.currTokenIndex < this.tokens.size()){
            this.currToken = this.tokens.get(this.currTokenIndex);
        }
    }

    // Ensures the program ends with EOF
    public ParseResult parse(){
        ParseResult res = this.statements();
        if(this.currToken.getType() != Token.TT_EOF || res.getError() != null){
            throw new InvalidSyntaxError(this.currToken.getPositionStart(), this.currToken.getPositionEnd(), "Expected '+', '-', '*', or '/'");
        }
        
        return res;
    }

    // Returns a ParseResult with the root of the AST (parses multiple statements when using ';' (newline token))
    private ParseResult statements(){
        ParseResult res = new ParseResult();
        ArrayList<ASTNode> statements = new ArrayList<>();

        while(this.currToken.getType().equals(Token.TT_NEWLINE)){
            res.registerAdvancement();
            this.advance();
        }

        boolean moreStatements = true;
        ASTNode statement = res.register(this.statement());
        if(res.getError() != null) return res;
        statements.add(statement);

        while(true){
            int newLineCount = 0;
            while(this.currToken.getType().equals(Token.TT_NEWLINE)){
                res.registerAdvancement();
                this.advance();
                newLineCount += 1;
            }
            if (newLineCount == 0){
                moreStatements = false;
            }
            if (!moreStatements) break;
            statement = res.tryRegister(this.statement());
            if(statement == null){
                this.reverse(res.getToReverseCount());
                moreStatements = false;
                continue;
            }
            statements.add(statement);
        }

        return res.success(new ListNode(statements));
    }

    // Parses a single statement
    private ParseResult statement(){
        ParseResult res = new ParseResult();
        //Position posStart = this.currToken.positionStart.copy();
        Position posStart = this.currToken.getPositionStart();

        if(this.currToken.matches(Token.TT_KEYWORD, "return")){
            res.registerAdvancement();
            this.advance();

            ASTNode expr = res.tryRegister(this.expression());
            if(expr == null){
                this.reverse(res.getToReverseCount());
            }
            return res.success(new ReturnNode(expr, posStart, this.currToken.getPositionStart()));
        }

        if(this.currToken.matches(Token.TT_KEYWORD,"continue")){
            res.registerAdvancement();
            this.advance();
            return res.success(new ContinueNode(posStart,this.currToken.getPositionStart()));
        }

        if(this.currToken.matches(Token.TT_KEYWORD, "break")){
            res.registerAdvancement();
            this.advance();
            return res.success(new BreakNode(posStart, this.currToken.getPositionStart()));
        }

        ASTNode expr = res.register(this.expression());
        if(res.getError() != null){
            throw new IllegalArgumentException("res.error is NOT NULL boii");
        }
        return res.success(expr);
    }

    // Parses expressions, ex 2 * 3. Just like they are defined in Java docs
    private ParseResult expression(){
        // Handle addition and subtraction (lower precedence)
        ParseResult res = new ParseResult();
        if(this.currToken.matches(Token.TT_KEYWORD, "var")){
            res.registerAdvancement();
            this.advance();
            
            if(!this.currToken.getType().equals(Token.TT_IDENTIFIER)){
                throw new IllegalCharError("Expected identifier");
            }

            Token<?> varName = this.currToken;
            res.registerAdvancement();
            this.advance();

            if(!this.currToken.getType().equals(Token.TT_EQ)){
                throw new IllegalCharError("Expected =");
            }
            
            res.registerAdvancement();
            this.advance();
            
            ASTNode expr = res.register(this.expression());
            
            return res.success(new VarAssignNode(varName, expr));
        }
        ASTNode node = res.register(this.binOp(this::compExpression, Arrays.asList("and","or")));
        if(res.getError() != null) return res;
        return res.success(node);

    }   

    // Compares expressions ex `not 0`, `3+1 > 4`, etc.
    private ParseResult compExpression(){
        ParseResult res = new ParseResult();
        Token<?> opToken = null;

        if(this.currToken.getValue().equals("not")){
            opToken = this.currToken;
            res.registerAdvancement();
            this.advance();

            ASTNode node = res.register(this.compExpression());
            if(res.getError() != null) return res;

            return res.success(new UnaryOpNode(opToken, node));
        }

        ASTNode node = res.register(this.binOp(this::arithExpression, Arrays.asList(Token.TT_EE,Token.TT_NE,Token.TT_LT,Token.TT_GT,Token.TT_LTE,Token.TT_GTE)));
        if(res.getError() != null){
            throw new IllegalCharError("res.error is not null");
        }

        return res.success(node);
    }

    // *, /
    private ParseResult term(){
        // Handle multiplication and division first (higher precedence)
        return this.binOp(this::factor, Arrays.asList(Token.TT_MUL, Token.TT_DIV));           
    }

    // +, -, !
    private ParseResult arithExpression(){
        return this.binOp(this::term, Arrays.asList(Token.TT_PLUS,Token.TT_MINUS, Token.TT_EXCLM));
    }

    // Parses Unary operators
    private ParseResult factor(){
        ParseResult res = new ParseResult();
        Token<?> token = currToken;
            
        if(token.getType().equals(Token.TT_PLUS) || token.getType().equals(Token.TT_MINUS)){
            res.registerAdvancement();
            this.advance();

            ASTNode factor = res.register(this.factor());
            if(res.getError() != null){
                throw new InvalidSyntaxError(token.getPositionStart(),token.getPositionEnd(),"You fond a hidden error!");
            }
            return res.success(new UnaryOpNode(token, factor));
        } 
    
        return this.power();
        //throw new InvalidSyntaxError(Token.positionStart,Token.positionEnd,"Expected int or float");
    }

    // Currently under construction (not in use)
    private ParseResult power(){
        return  this.binOp(this::call, this::factor, Arrays.asList("POW")); 
    }

    private ParseResult call(){
        ParseResult res = new ParseResult();
        ASTNode atom = res.register(this.atom());
        if(res.getError() != null) return res;

        if(this.currToken.getType().equals(Token.TT_LPAREN)){
            res.registerAdvancement();
            this.advance();
            ArrayList<ASTNode> argNodes = new ArrayList<>();

            if(this.currToken.getType().equals(Token.TT_RPAREN)){
                res.registerAdvancement();
                this.advance();
            } else {
                argNodes.add(res.register(this.expression()));
                if(res.getError() != null) return res;

                while(this.currToken.getType().equals(Token.TT_COMMA)){
                    res.registerAdvancement();
                    this.advance();

                    argNodes.add(res.register(this.expression()));
                    if(res.getError() != null) return res;

                }
                
                if(!this.currToken.getType().equals(Token.TT_RPAREN)){
                    throw new InvalidSyntaxError(this.currToken.getPositionStart(),this.currToken.getPositionEnd(), "Expected ',' or ')'");
                }

                res.registerAdvancement();
                this.advance();
            }
            return res.success(new CallNode(atom,argNodes));
        }
        return res.success(atom);
    }

    // Parses the smallest syntactic units in an expression
    // ex 42, "hello", x, (x+4), [1,2,3], etc
    private ParseResult atom(){
        ParseResult res = new ParseResult();
        Token<?> token = currToken;

        if(token.getType().equals(Token.TT_INT) || token.getType().equals(Token.TT_FLOAT)){
            res.registerAdvancement();
            this.advance();
            return res.success(new NumberNode(token));

        } else if(token.getType().equals(Token.TT_STRING)){
            res.registerAdvancement();
            this.advance();
            return res.success(new StringNode(token));

        } else if (token.getType().equals(Token.TT_IDENTIFIER)){
            res.registerAdvancement();
            this.advance();
            return res.success(new VarAccessNode(token));
        }
        
        else if (token.getType().equals(Token.TT_LPAREN)){
            res.registerAdvancement();
            this.advance();

            ASTNode expr = res.register(this.expression());
            
            if(res.getError() != null) throw new InvalidSyntaxError(token.getPositionStart(),token.getPositionEnd(),"You fond a hidden error!");
            
            if(this.currToken.getType().equals(Token.TT_RPAREN)){
                res.registerAdvancement();
                this.advance();
                return res.success(expr);
            } else {
                throw new InvalidSyntaxError(token.getPositionStart(), token.getPositionEnd(),"Expected ')'");
            }
        } else if (token.getType().equals(Token.TT_LSQUAREB)){
            ASTNode listExpr = res.register(this.listExpression());
            return res.success(listExpr);
        } else if(token.matches(Token.TT_KEYWORD,"if")){
            ASTNode ifExpr = res.register(this.ifExpression());
            return res.success(ifExpr);
        } else if (token.matches(Token.TT_KEYWORD,"for")){
            ASTNode forExpr = res.register(this.forExpression());
            return res.success(forExpr);
        }  else if (token.matches(Token.TT_KEYWORD,"while")){
            ASTNode WhileExpr = res.register(this.whileExpression());
            return res.success(WhileExpr);
        } else if (token.matches(Token.TT_KEYWORD,"func")){
            ASTNode FuncDef = res.register(this.funcDef());
            return res.success(FuncDef);
        } 
        else throw new IllegalCharError("Expected int or float but got " + this.currToken.getType());
    }

    // Delegates the actual work to ifExpressionCases
    private ParseResult ifExpression(){
        ParseResult res = new ParseResult();

        IfCases allCases = (IfCases) res.register(this.ifExpressionCases("if"));
        if(res.getError() != null) return res;

        return res.success(new IfNode(allCases.getCases(),allCases.getElseCase()));
    }

    // Parses one if or elif case
    private ParseResult ifExpressionCases(String keyword){
        ParseResult res = new ParseResult();
        List<Case> cases = new ArrayList<>();
        ElseCase elseCase = null;

        if(!this.currToken.matches(Token.TT_KEYWORD,keyword)){
            throw new IllegalArgumentException("Expected 'if'");
        }
        res.registerAdvancement();
        this.advance();
        
        ASTNode condition = res.register(this.expression());
        if(res.getError() != null) return res;
        
        if(!this.currToken.matches(Token.TT_KEYWORD,"then")){
            throw new IllegalArgumentException("Expected 'then'");
        }

        res.registerAdvancement();
        this.advance();
        
        if(this.currToken.getType().equals(Token.TT_NEWLINE)){
            res.registerAdvancement();
            this.advance();
            
            ASTNode statements = res.register(this.statements());
            if(res.getError() != null) return res;
            cases.add(new Case(condition,statements,true));
            

            if(this.currToken.matches(Token.TT_KEYWORD, "end")){
                res.registerAdvancement();
                this.advance();
            } else {
                
                IfCases allCases = (IfCases) res.register(this.ifExprBorC());
                if(res.getError() != null) return res;
                List<Case> newCases = allCases.getCases();
                elseCase = allCases.getElseCase();
                cases.addAll(newCases);
                
            }
        } else {
            ASTNode expr = res.register(this.statement());
            if(res.getError() != null) return res;
            cases.add(new Case(condition, expr, false));

            IfCases allCases = (IfCases) res.register(this.ifExprBorC());
            if(res.getError() != null) return res;
            List<Case> newCases = allCases.getCases();
            elseCase = allCases.getElseCase();
            cases.addAll(newCases);
        }     
        return res.success(new IfCases(cases,elseCase));
    }

    // Wrapper
    private ParseResult ifExprB(){
        return this.ifExpressionCases("elif");
    }

    // Parses the `else` block
    private ParseResult ifExprC(){
        ParseResult res = new ParseResult();
        ElseCase elseCase = null;
        
        if(this.currToken.matches(Token.TT_KEYWORD, "else")){
            res.registerAdvancement();
            this.advance();

            if(this.currToken.getType().equals(Token.TT_NEWLINE)){
                res.registerAdvancement();;
                this.advance();

                ASTNode statements = res.register(this.statements());
                if(res.getError() != null) return res;
                elseCase = new ElseCase(statements, true);

                if(this.currToken.matches(Token.TT_KEYWORD, "end")){
                    res.registerAdvancement();
                    this.advance();
                } else {
                    throw new InvalidSyntaxError(this.currToken.getPositionStart(),this.currToken.getPositionEnd(),"expected 'end'");
                }
            } else {
                ASTNode expr = res.register(this.expression());
                if(res.getError() != null) return res;
                elseCase = new ElseCase(expr,false);
            }
        }
        return res.success(elseCase);
    }

    // If next token is `elif` -> parse more cases
    // Else -> try parsing `else`
    private ParseResult ifExprBorC(){
        ParseResult res = new ParseResult();
        List<Case> cases = new ArrayList<>();
        ElseCase elseCase = null;

        if(this.currToken.matches(Token.TT_KEYWORD, "elif")){
            IfCases allCases = (IfCases) res.register(this.ifExprB());
            cases = allCases.getCases();
            elseCase = allCases.getElseCase();
        } else {
            elseCase = (ElseCase) res.register(this.ifExprC());
        }
        
        return res.success(new IfCases(cases, elseCase));

    }

    // Parses for loops
    private ParseResult forExpression(){
        ParseResult res = new ParseResult();

        if(!this.currToken.matches(Token.TT_KEYWORD,"for")){
            throw new IllegalCharError("Expected for");
        }
        
        res.registerAdvancement();
        this.advance();

        if(this.currToken.getType() != Token.TT_IDENTIFIER){
            throw new IllegalCharError("Expected identifier");
        }

        Token<?> varName = this.currToken;
        res.registerAdvancement();
        this.advance();

        if(this.currToken.getType() != Token.TT_EQ){
            throw new InvalidSyntaxError(this.currToken.getPositionStart(),this.currToken.getPositionEnd(),"Expected identifier");
        }

        res.registerAdvancement();
        this.advance();

        ASTNode startValue = res.register(this.expression());
        if(res.getError() != null) return res;

        if(!this.currToken.matches(Token.TT_KEYWORD,"to")){
            throw new InvalidSyntaxError(this.currToken.getPositionStart(),this.currToken.getPositionEnd(),"Expected TO");
        }

        res.registerAdvancement();
        this.advance();

        ASTNode endValue = res.register(this.expression());
        if(res.getError() != null) return res;
        
        ASTNode stepValue = null;

        if(this.currToken.matches(Token.TT_KEYWORD, "step")){
            res.registerAdvancement();
            this.advance();

            stepValue = res.register(this.expression());
            if(res.getError() != null) return res;
        }

        if(!this.currToken.matches(Token.TT_KEYWORD, "then")){
            throw new InvalidSyntaxError(this.currToken.getPositionStart(),this.currToken.getPositionEnd(),"Expected THEN");
        }

        res.registerAdvancement();
        this.advance();
        
        if(this.currToken.getType().equals(Token.TT_NEWLINE)){
            res.registerAdvancement();
            this.advance();

            ASTNode body = res.register(this.statements());
            if(res.getError() != null) return res;

            if(!this.currToken.matches(Token.TT_KEYWORD,"end")){
                throw new InvalidSyntaxError(this.currToken.getPositionStart(),this.currToken.getPositionEnd(),"Expected 'end'");
            }
            
            res.registerAdvancement();
            this.advance();

            return res.success(new ForNode(varName, body, startValue, endValue, stepValue, true));
        }


        ASTNode body = res.register(this.statement());
        if (res.getError() != null) return res;

        return res.success(new ForNode(varName, body, startValue, endValue, stepValue, false));

    }

    // Parses While loops
    private ParseResult whileExpression(){
        ParseResult res = new ParseResult();

        if(!this.currToken.matches(Token.TT_KEYWORD, "while")){
            throw new InvalidSyntaxError(this.currToken.getPositionStart(),this.currToken.getPositionEnd(),"Expected WHILE");
        }

        res.registerAdvancement();
        this.advance();

        ASTNode condition = res.register(this.expression());
        if(res.getError() != null) return null;

        if(!this.currToken.matches(Token.TT_KEYWORD, "then")){
            throw new InvalidSyntaxError(this.currToken.getPositionStart(),this.currToken.getPositionEnd(),"Expected THEN");
        }

        res.registerAdvancement();
        this.advance();
       
        if(this.currToken.getType().equals(Token.TT_NEWLINE)){
            res.registerAdvancement();
            this.advance();

            ASTNode body = res.register(this.statement());
            if(res.getError() != null) return res;

            if(!this.currToken.matches(Token.TT_KEYWORD,"end")){
                throw new InvalidSyntaxError(this.currToken.getPositionStart(), this.currToken.getPositionEnd(),"Expected end");
            }

            res.registerAdvancement();
            this.advance();

            return res.success(new WhileNode(condition, body, true)); 
        }
           
        ASTNode body = res.register(this.statement());
        if(res.getError() != null) return res;

        return res.success(new WhileNode(condition, body, false));
    }

    // Parses Lists
    private ParseResult listExpression(){
        ParseResult res = new ParseResult();
        ArrayList<ASTNode> elementNodes = new ArrayList<>();
        //Position posStart = this.currToken.positionStart.copy();

        if(!this.currToken.getType().equals(Token.TT_LSQUAREB)){
            throw new InvalidSyntaxError(this.currToken.getPositionStart(),this.currToken.getPositionEnd(),"Expected '[");
        }
        res.registerAdvancement();
        this.advance();
        
        if(this.currToken.getType().equals(Token.TT_RSQUAREB)){
            res.registerAdvancement();
            this.advance();
        } else {
            elementNodes.add(res.register(this.expression()));

            while(this.currToken.getType().equals(Token.TT_COMMA)){
                res.registerAdvancement();
                this.advance();

                elementNodes.add(res.register(this.expression()));
                if(res.getError() != null) return res;
            }

            if(!this.currToken.getType().equals(Token.TT_RSQUAREB)){
                throw new InvalidSyntaxError(this.currToken.getPositionStart(),this.currToken.getPositionEnd(), "Expected ',' or ']'");
            }
            res.registerAdvancement();
            this.advance();
        }
        return res.success(new ListNode(elementNodes));
    }

    // Accepts a single parsing function to handle the correct precedence levels
    private ParseResult binOp(Supplier<ParseResult> parseFuncA, List<String> ops){
        ParseResult res = new ParseResult();
        Supplier<ParseResult> parseFuncB = parseFuncA;

        ASTNode left = res.register(parseFuncA.get());

        // Call the provided parse function (e.g., factor or term)
        if(res.getError() != null) throw new InvalidSyntaxError(this.currToken.getPositionStart(),this.currToken.getPositionEnd(),"You fond a hidden error!");
        
        while(ops.contains(this.currToken.getType())){
            Token<?> opToken = this.currToken;
            res.registerAdvancement();
            this.advance();

            ASTNode right = res.register(parseFuncB.get());
            // Use the same parse function for the right side
            if(res.getError() != null) throw new InvalidSyntaxError(opToken.getPositionStart(),opToken.getPositionEnd(),"You fond a hidden error!");

            left = new BinOpNode(left, opToken, right); 
        }
        return res.success(left);
    }

    // Accepts two parsing function to handle the correct precedence levels
    private ParseResult binOp(Supplier<ParseResult> parseFuncA, Supplier<ParseResult> parseFuncB, List<String> ops){
        ParseResult res = new ParseResult();
        ASTNode left = res.register(parseFuncA.get());
        
        // Call the provided parse function (e.g., factor or term)
        if(res.getError() != null) throw new InvalidSyntaxError(this.currToken.getPositionStart(),this.currToken.getPositionEnd(),"You fond a hidden error!");
        
        while(ops.contains(this.currToken.getValue())){
            Token<?> opToken = this.currToken;
            res.registerAdvancement();
            this.advance();
            ASTNode right = res.register(parseFuncB.get());  // Use the same parse function for the right side
            if(res.getError() != null) throw new InvalidSyntaxError(opToken.getPositionStart(),opToken.getPositionEnd(),"You fond a hidden error!");

            left = new BinOpNode(left, opToken, right);
        }

        return res.success(left);
    }

    // Parses functions
    private ParseResult funcDef(){
        ParseResult res = new ParseResult();

        if(!this.currToken.matches(Token.TT_KEYWORD,"func")){
            throw new InvalidSyntaxError(this.currToken.getPositionStart(), this.currToken.getPositionEnd(), "Expected 'FUNC'");
        }
        
        res.registerAdvancement();
        this.advance();

        Token<?> varNameTok = null;
        if(this.currToken.getType().equals(Token.TT_IDENTIFIER)){
            varNameTok = this.currToken;
            res.registerAdvancement();
            this.advance();
            if(!this.currToken.getType().equals(Token.TT_LPAREN)){
                throw new InvalidSyntaxError(this.currToken.getPositionStart(), this.currToken.getPositionEnd(), "Expected '('");
            }
        } else {
            if(!this.currToken.getType().equals(Token.TT_LPAREN)){
                throw new InvalidSyntaxError(this.currToken.getPositionStart(), this.currToken.getPositionEnd(), "Expected identifier or '('");
            }
        }
        
        res.registerAdvancement();
        this.advance();
        ArrayList<Token<?>> argNameTokens = new ArrayList<>();

        if(this.currToken.getType().equals(Token.TT_IDENTIFIER)){
            argNameTokens.add(this.currToken);
            res.registerAdvancement();
            this.advance();

            while(this.currToken.getType().equals(Token.TT_COMMA)){
                res.registerAdvancement();
                this.advance();

                if(!this.currToken.getType().equals(Token.TT_IDENTIFIER)){
                    throw new InvalidSyntaxError(this.currToken.getPositionStart(), this.currToken.getPositionEnd(), "Expected identifier");
                } else {
                    argNameTokens.add(this.currToken);
                    res.registerAdvancement();
                    this.advance();
                }
            }
            if(!this.currToken.getType().equals(Token.TT_RPAREN)){
                throw new InvalidSyntaxError(this.currToken.getPositionStart(), this.currToken.getPositionEnd(), "Expected ',' or ')'");
            }

        } else {
            if(!this.currToken.getType().equals(Token.TT_RPAREN)){
                throw new InvalidSyntaxError(this.currToken.getPositionStart(), this.currToken.getPositionEnd(), "Expected identifier or ')'");
            }
        }

        res.registerAdvancement();
        this.advance();

        if(this.currToken.getType().equals(Token.TT_ARROW)){
            res.registerAdvancement();
            this.advance();

            ASTNode body = res.register(this.expression());
            if(res.getError() != null) return res;

            return res.success(new FuncDefNode(varNameTok, argNameTokens, body, true));
        }

        if(!this.currToken.getType().equals(Token.TT_NEWLINE)){
            throw new InvalidSyntaxError(this.currToken.getPositionStart(), this.currToken.getPositionEnd(), "Expected identifier or '->' or newline");
        }

        res.registerAdvancement();
        this.advance();

        ASTNode body = res.register(this.statements());
        if(res.getError() != null) return res;

        if(!this.currToken.matches(Token.TT_KEYWORD, "end")){
            throw new InvalidSyntaxError(this.currToken.getPositionStart(), this.currToken.getPositionEnd(), "Expected end");
        }

        res.registerAdvancement();
        this.advance();

        return res.success(new FuncDefNode(varNameTok, argNameTokens, body, false));
    }
}