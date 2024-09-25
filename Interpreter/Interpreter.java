package Interpreter;


import Operators.*;
import Parser.ParseResult;
import Values.Number;
import Token.*;
import Context.*;

public class Interpreter {
    Context Context = new Context("Program");
    
    public Number visit(ASTNode node, Context context){
        if(node instanceof NumberNode){
            return visitNumberNode((NumberNode)node);
        } else if(node instanceof BinOpNode){
            return visitBinaryOpNode((BinOpNode)node);
        } if(node instanceof UnaryOpNode){
            return visitUnaryOpNode((UnaryOpNode) node);
        } else if(node instanceof VarAccessNode){
            return visitVarAccessNode((VarAccessNode) node, context);
        } else if(node instanceof VarAssignNode){
            return visitVarAssignedNode((VarAssignNode) node, context);
        } 
        else {
            //System.out.println("not good bro");
            return null;
        }
    }

    public Number visitNumberNode(NumberNode node){
        Number res = new Number(node.token.value);
        res.setPosition(node.positionStart,node.positionEnd);
        return res;
    }

    public Number visitBinaryOpNode(BinOpNode node){
        //System.out.println("Visit binOp Node!");
        Number left = this.visit(node.leftNode, new Context("Program"));
        Number right = this.visit(node.rightNode, new Context("Program"));
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
        } else {     
            throw new IllegalArgumentException("not good bro");
        }
        result.setPosition(node.positionStart, node.positionEnd);
        return result;
    }

    public Number visitUnaryOpNode(UnaryOpNode node){
        //System.out.println("Visit UnOp Node!");
        Number num = this.visit(node.node, new Context("Program"));

        if(node.opToken.type.equals(Token.TT_MINUS)){
            num = num.multiplyBy(new Number(-1));
        }
        num.setPosition(node.positionStart, node.positionEnd);
        return num;
    }

    public Number visitVarAccessNode(VarAccessNode node, Context context){
        ParseResult res = new ParseResult();
        Object varName = node.varNameToken.value;
        Object value = context.symbolTable.get(varName);
        if(value == null) throw new IllegalArgumentException("Variable " + varName + " is not defined");
        return new Number(value);
    }

    public Number visitVarAssignedNode(VarAssignNode node, Context context){
        ParseResult res = new ParseResult();
        Object varName = node.varNameToken.value;
        Object value = this.visit(node.valueNode, new Context("Program"));
        if(value == null) throw new IllegalArgumentException("Variable " + varName + " is not defined");
        System.out.println("Made it");
        //context.symbolTableObject.set(varName.toString(), value);
        return new Number(value);
    }

}
