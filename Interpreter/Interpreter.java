package Interpreter;

import Operators.*;
 

public class Interpreter {

    public void visit(ASTNode astNode){
        if(astNode instanceof BinOpNode){
            visitBinOpNode((BinOpNode) astNode);
        } else if(astNode instanceof NumberNode){
            visitNumberNode((NumberNode) astNode);
        } else if(astNode instanceof UnaryOpNode){
            visitUnaryOpNode((UnaryOpNode) astNode);
        }
        else System.out.println("NO NODE FOUND!");
    }
    
    public void visitBinOpNode(BinOpNode binOpNode){
        System.out.println("Found binOpNode!");
        this.visit(binOpNode.leftNode);
        this.visit(binOpNode.rightNode);
    }

    public void visitNumberNode(NumberNode numberNode){
        System.out.println("Found NumberOpNode!");
    }

    public void visitUnaryOpNode(UnaryOpNode unNode){
        System.out.println("Found unOp nodoe!");
        this.visit(unNode.node);
    }
}
