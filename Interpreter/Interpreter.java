package Interpreter;


import Operators.*;
import Parser.ParseResult;
import Values.Number;
import Token.*;
import Context.*;
import DataStructures.numberContext;

public class Interpreter {
    Context Context = new Context("Program");
    
    public numberContext visit(ASTNode node, Context context){
        if(node instanceof NumberNode){
            return new numberContext(visitNumberNode((NumberNode)node, context).number, context);
    
        } else if(node instanceof BinOpNode){
            return new numberContext(visitBinaryOpNode((BinOpNode)node, context).number, context);
        } if(node instanceof UnaryOpNode){
            return new numberContext(visitUnaryOpNode((UnaryOpNode) node, context).number, context);
        } else if(node instanceof VarAccessNode){
            return new numberContext(visitVarAccessNode((VarAccessNode) node, context).number, context);
        } else if(node instanceof VarAssignNode){
            return new numberContext(visitVarAssignedNode((VarAssignNode) node, context).number, context);
        } 
        else {
            //System.out.println("not good bro");
            return null;
        }
    }

    public numberContext visitNumberNode(NumberNode node, Context context){
        Number res = new Number(node.token.value);
        res.setPosition(node.positionStart,node.positionEnd);
        return new numberContext(res, context);
    }

    public numberContext visitBinaryOpNode(BinOpNode node, Context context){
        //System.out.println("Visit binOp Node!");
        Number left = this.visit(node.leftNode, context).number;
        Number right = this.visit(node.rightNode, context).number;
        Number result;

        if(node.token.type.equals(Token.TT_PLUS)){
            result = left.addBy(right);
        } else if(node.token.type.equals(Token.TT_MINUS)){
            result = left.subtractBy(right);
        } else if(node.token.type.equals(Token.TT_MUL)){
            result = left.multiplyBy(right);
        } else if(node.token.type.equals(Token.TT_DIV)){
            result = left.divideBy(right);
        } else if(node.token.type.equals(Token.TT_POW)){
            result = left.powerBy(right);
        } else if(node.token.type.equals(Token.TT_EE)){
            result = left.getComparisonEe(right);
        } else if(node.token.type.equals(Token.TT_NE)){
           result = left.getComparisonNe(right);
        } else if(node.token.type.equals(Token.TT_LT)){
           result = left.getComparisonLt(right);
        } else if(node.token.type.equals(Token.TT_GT)){
            result = left.getComparisonGt(right);
        } else if(node.token.type.equals(Token.TT_LTE)){
           result = left.getComparisonLte(right);
        } else if(node.token.type.equals(Token.TT_GTE)){
            result = left.getComparisonGte(right);
        } else if(node.token.matches(Token.TT_KEYWORD, "AND")){
            result = left.andBy(right);
        } else if(node.token.matches(Token.TT_KEYWORD,"OR")){
            result = left.orBy(right);
        } 
        
        
        else {     
            throw new IllegalArgumentException("not good bro");
        }
        result.setPosition(node.positionStart, node.positionEnd);
        return new numberContext(result, context);
    }

    public numberContext visitUnaryOpNode(UnaryOpNode node, Context context){
        //System.out.println("Visit UnOp Node!");
        Number num = this.visit(node.node, context).number;

        if(node.opToken.type.equals(Token.TT_MINUS)){
            num = num.multiplyBy(new Number(-1));
        } else if (node.opToken.matches(Token.TT_KEYWORD,"NOT")){
            num = num.notted();
        }
        num.setPosition(node.positionStart, node.positionEnd);
        return new numberContext(num, context);
    }

    public numberContext visitVarAccessNode(VarAccessNode node, Context context){
        ParseResult res = new ParseResult();
        Object varName = node.varNameToken.value;
        Number value = context.symbolTableObject.symbols.get(varName);
        if(value == null) throw new IllegalArgumentException("Variable " + varName + " is not defined");
        //System.out.println(value);
        return new numberContext(value, context);
    }

    public numberContext visitVarAssignedNode(VarAssignNode node, Context context){
        ParseResult res = new ParseResult();
        Object varName = node.varNameToken.value;
        Number value = this.visit(node.valueNode, context).number;
        if(value == null) throw new IllegalArgumentException("Variable " + varName + " is not defined");
    
        context.symbolTableObject.set(varName.toString(), value);
        
        return new numberContext(value, context);
    }

}
