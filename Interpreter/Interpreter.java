package Interpreter;


import Operators.*;
import Values.Number;
import Token.*;

public class Interpreter {
    
    public Number visit(ASTNode node){
        if(node instanceof NumberNode){
            return visitNumberNode((NumberNode)node);
        } else if(node instanceof BinOpNode){
            return visitBinaryOpNode((BinOpNode)node);
        } if(node instanceof UnaryOpNode){
            return visitUnaryOpNode((UnaryOpNode) node);
        } else {
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
        Number left = this.visit(node.leftNode);
        Number right = this.visit(node.rightNode);
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
        Number num = this.visit(node.node);

        if(node.opToken.type.equals(Token.TT_MINUS)){
            num = num.multiplyBy(new Number(-1));
        }
        num.setPosition(node.positionStart, node.positionEnd);
        return num;
    }

}
