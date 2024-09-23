package Interpreter;

import java.util.function.BinaryOperator;

import Operators.*;

public class Interpreter {
    
    public void visit(ASTNode node){
        if(node instanceof NumberNode){
            visitNumberNode((NumberNode)node);
        } else if(node instanceof BinOpNode){
            visitBinaryOpNode((BinOpNode)node);
        } if(node instanceof UnaryOpNode){
            visitUnaryOpNode((UnaryOpNode) node);
        } else {
            //System.out.println("not good bro");
        }
    }

    public void visitNumberNode(NumberNode node){
        System.out.println("Visit Number Node!");
    }

    public void visitBinaryOpNode(BinOpNode node){
        System.out.println("Visit binOp Node!");
        this.visit(node.leftNode);
        this.visit(node.rightNode);
    }

    public void visitUnaryOpNode(UnaryOpNode node){
        System.out.println("Visit UnOp Node!");
        this.visit(node.node);
    }

}
