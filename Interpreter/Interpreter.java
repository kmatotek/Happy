package Interpreter;

import Operators.*;
import Values.Number;
import Token.*;
import Context.*;
import DataStructures.*;
import Errors.InvalidSyntaxError;

import java.util.ArrayList;
import Values.*;


public class Interpreter {
    public Context Context = new Context("Program");
    
    public valueContext visit(ASTNode node, Context context){
        if(node instanceof NumberNode){
            return new valueContext(visitNumberNode((NumberNode)node, context).value, context);      
        } else if(node instanceof StringNode){ 
            
            return new valueContext(visitStringNode((StringNode) node, context).value, context);
        } else if(node instanceof BinOpNode){
            
            return new valueContext(visitBinaryOpNode((BinOpNode)node, context).value, context);
        } if(node instanceof UnaryOpNode){
            return new valueContext(visitUnaryOpNode((UnaryOpNode) node, context).value, context);
        } else if(node instanceof VarAccessNode){
            return new valueContext(visitVarAccessNode((VarAccessNode) node, context).value, context);
        } else if(node instanceof VarAssignNode){
            return new valueContext(visitVarAssignedNode((VarAssignNode) node, context).value, context);
        } else if(node instanceof IfNode){
            return new valueContext(visitIfNode((IfNode) node, context).value, context);
        } else if(node instanceof ForNode){
            return new valueContext(visitForNode((ForNode) node, context).value, context);
        } else if(node instanceof WhileNode){
            return new valueContext(visitWhileNode((WhileNode) node, context).value, context);
        } else if(node instanceof CallNode){
            return new valueContext(visitCallNode((CallNode) node, context).value, context);
        } else if(node instanceof FuncDefNode){
            return new valueContext(visitFuncDefNode((FuncDefNode) node, context).value, context);
        } else if(node instanceof ListNode){
            return new valueContext(visitListNode((ListNode) node, context).value, context);
        }
        else {
            System.out.println("no instance found");
            return null;
        }
    }

    public valueContext visitNumberNode(NumberNode node, Context context){
        Number res = new Number(node.token.value);
        res.setPosition(node.positionStart,node.positionEnd);
        return new valueContext(res, context);
    }

    public valueContext visitBinaryOpNode(BinOpNode node, Context context){
        //System.out.println("Visit binOp Node!");
        Value value1 = this.visit(node.leftNode, context).value;
        Value value2 = this.visit(node.rightNode, context).value;
        
        if(value1 instanceof MyString && value2 instanceof MyString){
            
            MyString left = (MyString) value1;
            MyString right = (MyString) value2;
            MyString res = null;
            if(node.token.type.equals(Token.TT_PLUS)){
                res = left.addedTo(right);
            } else {
                throw new InvalidSyntaxError(node.positionStart, node.positionEnd, "Invalid operator on Strings");
            }
            return new valueContext(res,context);
        } else if (value1 instanceof MyString && value2 instanceof Number){
            
            MyString left = (MyString) value1;
            Number right = (Number) value2;
            MyString res = null;
            if(node.token.type.equals(Token.TT_MUL)){
                int goTo = Number.toInt(right.value);
                StringBuilder sb = new StringBuilder();
                for(int i = 0; i < goTo; i++){
                    sb.append(left);
                }
                res = new MyString(sb.toString());
            } else if (node.token.type.equals(Token.TT_EXCLM)){
                int index = Number.toInt(right.value);
                res = new MyString(String.valueOf(left.s.charAt(index)));
            } else {
                throw new InvalidSyntaxError(node.positionStart, node.positionEnd, "Invalid operator on Strings");
            }
            return new valueContext(res,context);
        } else if (value1 instanceof MyList && value2 instanceof Number){
            MyList left = (MyList) value1;
            Number right = (Number) value2;
            if(node.token.type.equals(Token.TT_PLUS)){  
                MyList res = left.copy();
                res.elements.add(right);
                return new valueContext(res, context);

            } else if(node.token.type.equals(Token.TT_EXCLM)){
                
                int get = Number.toInt(right.value);
                if(get < 0 || get > left.elements.size()){
                    throw new InvalidSyntaxError(node.positionStart,node.positionEnd, "Index " + get + " out of bounds");
                }
                Number res = new Number(left.elements.get(get));
                return new valueContext(res,context);
               
            }


        } else if (value1 instanceof MyList && value2 instanceof MyList){
            
            if(node.token.type.equals(Token.TT_PLUS)){
                MyList left = (MyList) value1;
                MyList right = (MyList) value2;
                MyList res = left.copy();

                for(Value v : right.elements){
                    res.elements.add(v);
                }
            
                return new valueContext(res, context);
            } 
        }


    
        Number left = (Number) value1;
        //System.out.println(this.visit(node.leftNode, context).value);
        Number right = (Number) value2;
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
        } else if(node.token.matches(Token.TT_KEYWORD, "and")){
            result = left.andBy(right);
        } else if(node.token.matches(Token.TT_KEYWORD,"or")){
            result = left.orBy(right);
        } 
        
        else {     
            throw new IllegalArgumentException("not good bro");
        }
        result.setPosition(node.positionStart, node.positionEnd);
        return new valueContext(result, context);
    }

    public valueContext visitUnaryOpNode(UnaryOpNode node, Context context){
        //System.out.println("Visit UnOp Node!");
        Number num = (Number) this.visit(node.node, context).value;
        

        if(node.opToken.type.equals(Token.TT_MINUS)){
            num = num.multiplyBy(new Number(-1));
        } else if (node.opToken.matches(Token.TT_KEYWORD,"not")){
            num = num.notted();
        }
        num.setPosition(node.positionStart, node.positionEnd);
        return new valueContext(num, context);
    }

    public valueContext visitVarAccessNode(VarAccessNode node, Context context){
        //ParseResult res = new ParseResult();
        //System.out.println("Visit var access!");
        Object varName = node.varNameToken.value;
        Value value = context.symbolTableObject.symbols.get(varName);

        //if(value == null) System.out.println(HappyMain.globalSymbolTable.symbols.get();
        if(value == null) throw new IllegalArgumentException("Variable " + varName + " is not defined");
        //System.out.println(value);
        return new valueContext(value, context);
    }

    public valueContext visitVarAssignedNode(VarAssignNode node, Context context){
        //ParseResult res = new ParseResult();
        Object varName = node.varNameToken.value;
        Value value = this.visit(node.valueNode, context).value;
        if(value == null) throw new IllegalArgumentException("Variable " + varName + " is not defined");
    
        context.symbolTableObject.set(varName.toString(), value);
        
        return new valueContext(value, context);
    }

    public valueContext visitIfNode(IfNode node, Context context){
        //ParseResult res = new ParseResult();
        //for(List<ASTNode> list : node.cases){
        for(int i = 0; i < node.cases.size(); i++){
        
            ASTNode condition = node.cases.get(i).get(0);
            ASTNode expr = node.cases.get(i).get(1);

            //System.out.println(condition);

            Number conditionValue = (Number) this.visit(condition,context).value;
            if(Number.toInt(conditionValue.value) != 0){
                Value exprValue = this.visit(expr,context).value;
                return new valueContext(exprValue, context);
            } 
        }
        if(node.elseCase != null){
            Value elseValue = this.visit(node.elseCase,context).value;
            return new valueContext(elseValue, context);
        }
        return new valueContext(null,context);
    }

    public valueContext visitForNode(ForNode node, Context context){
        //ParseResult res = new ParseResult();
        ArrayList<Value> elements = new ArrayList<>();
        
        
        Number startValue = (Number) this.visit(node.startValueNode, context).value;
        Number endValue = (Number) this.visit(node.endValueNode, context).value;
        Number stepValue = null;
        
        

        if(node.stepValueNode != null){
            stepValue = (Number) this.visit(node.stepValueNode,context).value;
        } else {
            stepValue = new Number(1);
        }
        
        int i = Number.toInt(startValue.value);
        int step = Number.toInt(stepValue.value);
        
        if(step != 1){
            while(i < Number.toInt(endValue.value)){
                context.symbolTableObject.set(node.varNameToken.value.toString(), new Number(i));
                //System.out.println(context.symbolTableObject.toString());
                i += step;
                //System.out.println(i);
                elements.add(this.visit(node.bodyNode, context).value);
            }
        } else {
            
            while(i < Number.toInt(endValue.value)){
                
                //System.out.println(context.symbolTableObject.toString());
                context.symbolTableObject.set(node.varNameToken.value.toString(), new Number(i));
                i += 1;
                //System.out.println(i);
                elements.add(this.visit(node.bodyNode, context).value);
            }
        }
        
        MyList res = new MyList(elements);
        res.setContext(context);
        res.setPosition(node.posStart,node.posEnd);
        return new valueContext(res,context);
    }

    public valueContext visitWhileNode(WhileNode node, Context context){
        ArrayList<Value> elements = new ArrayList<>();

        while(true){
            Value condition = this.visit(node.conditionNode, context).value;
            if(Number.toInt(condition.value) == 0){
                break;
            }
            elements.add(this.visit(node.bodyNode, context).value);
        }

        MyList res = new MyList(elements);
        res.setContext(context);
        res.setPosition(node.posStart, node.posEnd);
        return new valueContext(res,context);
    }

    public valueContext visitFuncDefNode(FuncDefNode node, Context context){
        //System.out.println("Func def Node");
        String funcName = node.varNameTok.value.toString();
        ASTNode bodyNode = node.bodyNode;
        ArrayList<String> argNames = new ArrayList<>();

        for(Token<?> argName : node.argNameTokens){
            argNames.add(argName.value.toString());
        }
        Function funcValue = new Function(funcName, bodyNode, argNames);
    
        //System.out.println(argNames.toString());
        context.symbolTableObject.set(funcName,funcValue);
        System.out.println(context.symbolTableObject.symbols.toString());
        
        return new valueContext(funcValue, context);
    }

    public valueContext visitCallNode(CallNode node, Context context){
        //System.out.println("Call Node");
        ArrayList<Value> args = new ArrayList<>();
        if (this.visit(node.nodeToCall,context).value instanceof BuiltInFunction){
            BuiltInFunction valueToCall = (BuiltInFunction) this.visit(node.nodeToCall,context).value;
            //System.out.println(valueToCall.name);
            
            for(ASTNode argNode : node.argNodes){
                args.add(this.visit(argNode,context).value);
            }
        
            Value returnValue = valueToCall.execute(args,context);
            
            return new valueContext(returnValue,context);
        } else {
            Function valueToCall = (Function) this.visit(node.nodeToCall,context).value;
            
            for(ASTNode argNode : node.argNodes){
                args.add(this.visit(argNode,context).value);
            }
        
            Value returnValue = valueToCall.execute(args,context).value;
            return new valueContext(returnValue,context);
        }
    }

    public valueContext visitStringNode(StringNode node, Context context){
        
        MyString str = new MyString(node.token.value.toString());
        str.setContext(context);
        str.setPosition(node.positionStart,node.positionEnd);
        return new valueContext(str,context);
    }

    public valueContext visitListNode(ListNode node, Context context){
        ArrayList<Value> elements = new ArrayList<>();

        for(ASTNode n : node.elementNodes){
            elements.add(this.visit(n, context).value);
        }

        MyList res = new MyList(elements);
        res.setContext(context);
        res.setPosition(node.posStart,node.posEnd);

        return new valueContext(res, context);
    }

}
