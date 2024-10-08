package Interpreter;


import Operators.*;
import Parser.ParseResult;
import Values.Number;
import Token.*;
import Context.*;
import DataStructures.numberContext;
import Happy.*;
import java.util.List;

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
        } else if(node instanceof IfNode){
            return new numberContext(visitIfNode((IfNode) node, context).number, context);
        } else if(node instanceof ForNode){
            return new numberContext(visitForNode((ForNode) node, context).number, context);
        } else if(node instanceof WhileNode){
            return new numberContext(visitWhileNode((WhileNode) node, context).number, context);
        }
        else {
            System.out.println("no instance found");
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

        //if(value == null) System.out.println(HappyMain.globalSymbolTable.symbols.get();
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

    public numberContext visitIfNode(IfNode node, Context context){
        ParseResult res = new ParseResult();
        //for(List<ASTNode> list : node.cases){
        for(int i = 0; i < node.cases.size(); i++){
        
            ASTNode condition = node.cases.get(i).get(0);
            ASTNode expr = node.cases.get(i).get(1);

            //System.out.println(condition);

            Number conditionValue = this.visit(condition,context).number;
            if(conditionValue.toInt(conditionValue.value) != 0){
                Number exprValue = this.visit(expr,context).number;
                return new numberContext(exprValue, context);
            } 
        }
        if(node.elseCase != null){
            Number elseValue = this.visit(node.elseCase,context).number;
            return new numberContext(elseValue, context);
        }
        return new numberContext(null,context);
    }

    public numberContext visitForNode(ForNode node, Context context){
        ParseResult res = new ParseResult();
        
        Number startValue = this.visit(node.startValueNode, context).number;
        Number endValue = this.visit(node.endValueNode, context).number;
        Number stepValue = null;
        

        if(node.stepValueNode != null){
            stepValue = this.visit(node.stepValueNode,context).number;
        } else {
            stepValue = new Number(1);
        }
        int i = Number.toInt(startValue.value);
        System.out.println(node.varNameToken.toString());
        if(Number.toInt(stepValue.value) > 0){
            while(i < Number.toInt(endValue.value)){
                context.symbolTableObject.set(node.varNameToken.value.toString(), new Number(i));
                //System.out.println(context.symbolTableObject.toString());
                i += Number.toInt(stepValue.value);
                //System.out.println(i);
                this.visit(node.bodyNode, context);

            }
        } else {
            while(i > Number.toInt(endValue.value)){
                //System.out.println(stepValue.value);
                //System.out.println(context.symbolTableObject.toString());
                context.symbolTableObject.set(node.varNameToken.value.toString(), new Number(i));
                i += Number.toInt(stepValue.value);
                System.out.println(i);
                this.visit(node.bodyNode, context);

            }
        }
       
        return new numberContext(null,context);
    }

    public numberContext visitWhileNode(WhileNode node, Context context){
        while(true){
            Number condition = this.visit(node.conditionNode, context).number;
            if(Number.toInt(condition.value) == 0){
                break;
            }
            this.visit(node.bodyNode, context);
        }
        return new numberContext(null,context);
    }

}
