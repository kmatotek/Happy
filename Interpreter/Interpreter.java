package Interpreter;

import Operators.*;
import Values.Number;
import Token.*;
import Context.*;
import DataStructures.*;
import Errors.InvalidSyntaxError;
import Parser.*;

import java.util.ArrayList;
import Values.*;


public class Interpreter {
    public Context Context = new Context("Program");
    
    public RTResult visit(ASTNode node, Context context){
        RTResult res = new RTResult();
        if(node instanceof NumberNode){
            return res.success(new ValueContext(visitNumberNode((NumberNode)node, context).value, context).value);      
        } else if(node instanceof StringNode){ 
            return res.success(new ValueContext(visitStringNode((StringNode) node, context).value, context).value);
        } else if(node instanceof BinOpNode){
            return res.success(new ValueContext(visitBinaryOpNode((BinOpNode)node, context).value, context).value);
        } if(node instanceof UnaryOpNode){
            return res.success(new ValueContext(visitUnaryOpNode((UnaryOpNode) node, context).value, context).value);
        } else if(node instanceof VarAccessNode){
            return res.success(new ValueContext(visitVarAccessNode((VarAccessNode) node, context).value, context).value);
        } else if(node instanceof VarAssignNode){
            return res.success(new ValueContext(visitVarAssignedNode((VarAssignNode) node, context).value, context).value);
        } else if(node instanceof IfNode){
            return res.success(new ValueContext(visitIfNode((IfNode) node, context).value, context).value);
        } else if(node instanceof ForNode){
            return res.success(new ValueContext(visitForNode((ForNode) node, context).value, context).value);
        } else if(node instanceof WhileNode){
            return res.success(new ValueContext(visitWhileNode((WhileNode) node, context).value, context).value);
        } else if(node instanceof CallNode){
            return res.success(new ValueContext(visitCallNode((CallNode) node, context).value, context).value);
        } else if(node instanceof FuncDefNode){
            return res.success(new ValueContext(visitFuncDefNode((FuncDefNode) node, context).value, context).value);
        } else if(node instanceof ListNode){
            return res.success(new ValueContext(visitListNode((ListNode) node, context).value, context).value);
        }
        else {
            System.out.println("no instance found");
            return null;
        }
    }

    public void noVisitMethod(ASTNode node, Context context){
        throw new IllegalArgumentException("no visit method");
    }

    public ValueContext visitNumberNode(NumberNode node, Context context){
        Number num = new Number(node.token.value);
        num.setPosition(node.positionStart,node.positionEnd);
        return new ValueContext(num,context);
    }

    public RTResult visitBinaryOpNode(BinOpNode node, Context context){
        RTResult res = new RTResult();
        Value value1 = res.register(this.visit(node.leftNode, context));
        if(res.shouldReturn()) return res;
        Value value2 = res.register(this.visit(node.rightNode, context));
        if(res.shouldReturn()) return res;
        
        if(value1 instanceof MyString && value2 instanceof MyString){
            
            MyString left = (MyString) value1;
            MyString right = (MyString) value2;
            MyString ans = null;
            if(node.token.type.equals(Token.TT_PLUS)){
                ans = left.addedTo(right);
            } else {
                throw new InvalidSyntaxError(node.positionStart, node.positionEnd, "Invalid operator on Strings");
            }
            return res.success(ans);
        } else if (value1 instanceof MyString && value2 instanceof Number){
            
            MyString left = (MyString) value1;
            Number right = (Number) value2;
            MyString ans = null;
            if(node.token.type.equals(Token.TT_MUL)){
                int goTo = Number.toInt(right.value);
                StringBuilder sb = new StringBuilder();
                for(int i = 0; i < goTo; i++){
                    sb.append(left);
                }
                ans = new MyString(sb.toString());
            } else if (node.token.type.equals(Token.TT_EXCLM)){
                int index = Number.toInt(right.value);
                ans = new MyString(String.valueOf(left.s.charAt(index)));
            } else {
                throw new InvalidSyntaxError(node.positionStart, node.positionEnd, "Invalid operator on Strings");
            }
            return res.success(ans);
        } else if (value1 instanceof MyList && value2 instanceof Number){
            MyList left = (MyList) value1;
            Number right = (Number) value2;
            if(node.token.type.equals(Token.TT_PLUS)){  
                MyList ans = left.copy();
                ans.elements.add(right);
                return res.success(ans);

            } else if(node.token.type.equals(Token.TT_EXCLM)){
                
                int get = Number.toInt(right.value);
                if(get < 0 || get > left.elements.size()){
                    throw new InvalidSyntaxError(node.positionStart,node.positionEnd, "Index " + get + " out of bounds");
                }
                Number ans = new Number(left.elements.get(get));
                return res.success(ans);
            }


        } else if (value1 instanceof MyList && value2 instanceof MyList){
            
            if(node.token.type.equals(Token.TT_PLUS)){
                MyList left = (MyList) value1;
                MyList right = (MyList) value2;
                MyList ans = left.copy();

                for(Value v : right.elements){
                    ans.elements.add(v);
                }
            
                return res.success(ans);
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
        return res.success(result);
    }

    public RTResult visitUnaryOpNode(UnaryOpNode node, Context context){
        RTResult res = new RTResult();

        Number num = ((Number) res.register(this.visit(node.node, context)));
        if(res.shouldReturn()) return res;
        

        if(node.opToken.type.equals(Token.TT_MINUS)){
            num = num.multiplyBy(new Number(-1));
        } else if (node.opToken.matches(Token.TT_KEYWORD,"not")){
            num = num.notted();
        }
        num.setPosition(node.positionStart, node.positionEnd);
        return res.success(num);
    }

    public RTResult visitVarAccessNode(VarAccessNode node, Context context){
        RTResult res = new RTResult();
        Object varName = node.varNameToken.value;
        Value value = context.getSymbolTableObject().symbols.get(varName);

        if(value == null) throw new IllegalArgumentException("Variable " + varName + " is not defined");
        
        value.setPosition(node.positionStart, node.positionEnd);
        value.setContext(context);

        return res.success(value);
    }

    public RTResult visitVarAssignedNode(VarAssignNode node, Context context){
        RTResult res = new RTResult();
        Object varName = node.varNameToken.value;
        Value value = this.visit(node.valueNode, context).value;
        if(res.shouldReturn()) return res;
        if(value == null) throw new IllegalArgumentException("Variable " + varName + " is not defined");
        

        context.getSymbolTableObject().set(varName.toString(), value);
        
        return res.success(value);
    }

    public RTResult visitIfNode(IfNode node, Context context){
        RTResult res = new RTResult();

        boolean shouldReturnNull = node.elseCase.shouldReturnNull;
        for(int i = 0; i < node.cases.size(); i++){
        
            ASTNode condition = node.cases.get(i).condition;
            ASTNode expr = node.cases.get(i).expression;
            Number conditionValue = (Number) res.register(this.visit(condition,context));
            if(res.shouldReturn()) return res;


            
            if(Number.toInt(conditionValue.value) != 0){
                Value exprValue = res.register(this.visit(expr,context));
                if(res.shouldReturn()) return res;
                if(shouldReturnNull){
                    return res.success(null);
                } else {
                return res.success(exprValue);
                }
            } 
        }
        if(node.elseCase != null){
            ASTNode expr = node.elseCase.elseCase;
            Value elseValue = res.register(this.visit(expr,context));
            if(res.shouldReturn()) return res;
            if(shouldReturnNull){
                return res.success(null);
            } else {
                return res.success(elseValue);
            }
        }
        return res.success(null);
    }

    public RTResult visitForNode(ForNode node, Context context){
        RTResult res = new RTResult();
        ArrayList<Value> elements = new ArrayList<>();
        
        
        Number startValue = (Number) res.register(this.visit(node.startValueNode, context));
        if(res.shouldReturn()) return res;
        Number endValue = (Number) res.register(this.visit(node.endValueNode, context));
        if(res.shouldReturn()) return res;
        Number stepValue = null;
        
        

        if(node.stepValueNode != null){
            stepValue = (Number) res.register(this.visit(node.stepValueNode,context));
        } else {
            stepValue = new Number(1);
        }
        
        int i = Number.toInt(startValue.value);
        int step = Number.toInt(stepValue.value);
      
        
        if(step >= 0){
            while(i < Number.toInt(endValue.value)){
                context.getSymbolTableObject().set(node.varNameToken.value.toString(), new Number(i));
                
                i += step;
                Value value = res.register(this.visit(node.bodyNode, context));
                if(res.shouldReturn() && !res.loopShouldContinue && !res.loopShouldBreak) return res;

                if(res.loopShouldContinue) continue;
                if(res.loopShouldBreak) break;
                elements.add(value);
            }
        } else {
            while(i > Number.toInt(endValue.value)){
                context.getSymbolTableObject().set(node.varNameToken.value.toString(), new Number(i));
                i += step;
                
                Value value = res.register(this.visit(node.bodyNode, context));
                if(res.shouldReturn() && !res.loopShouldContinue && !res.loopShouldBreak) return res;

                if(res.loopShouldContinue) continue;
                if(res.loopShouldBreak) break;
                elements.add(value);
            }
        }
        
        if(node.shouldReturnNull) return res.success(null);
        MyList list = new MyList(elements);
        list.setContext(context);
        list.setPosition(node.posStart,node.posEnd);
        
        return res.success(list);
    }

    public RTResult visitWhileNode(WhileNode node, Context context){
        RTResult res = new RTResult();
        ArrayList<Value> elements = new ArrayList<>();

        while(true){
            Number condition = (Number) res.register(this.visit(node.conditionNode, context));
            if(res.shouldReturn()) return res;

            if(Number.toInt(condition.value) == 0){
                break;
            }

            Value value = res.register(this.visit(node.bodyNode, context));
            if(res.shouldReturn()) return res;
            
            if(res.loopShouldContinue) continue;
            if(res.loopShouldBreak) break;

            elements.add(value);
        }
        
        if(node.shouldReturnNull) return res.success(null);
        MyList list = new MyList(elements);
        list.setContext(context);
        list.setPosition(node.posStart, node.posEnd);
        return res.success(list);
    }

    public RTResult visitFuncDefNode(FuncDefNode node, Context context){
        RTResult res = new RTResult();
        String funcName = node.varNameTok.value.toString();
        ASTNode bodyNode = node.bodyNode;
        ArrayList<String> argNames = new ArrayList<>();

        for(Token<?> argName : node.argNameTokens){
            argNames.add(argName.value.toString());
        }

        Function funcValue = new Function(funcName, bodyNode, argNames, node.shouldAutoReturn);

        context.getSymbolTableObject().set(funcName,funcValue);
        
        return res.success(funcValue);
    }

    public RTResult visitCallNode(CallNode node, Context context){
        RTResult res = new RTResult();

        ArrayList<Value> args = new ArrayList<>();
        Value valueToCall = res.register(this.visit(node.nodeToCall, context));
        if(res.shouldReturn()) return res;

        if (valueToCall instanceof BuiltInFunction){
            BuiltInFunction valueToCallB = (BuiltInFunction) valueToCall;
            
            for(ASTNode argNode : node.argNodes){
                args.add(res.register(this.visit(argNode,context)));
                if(res.shouldReturn()) return res;
            }
        
            Value returnValue = res.register(valueToCallB.execute(args,context));
            
            return res.success(returnValue);
        } else {
            Function valueToCallB = (Function) valueToCall;
            
            for(ASTNode argNode : node.argNodes){
                args.add(res.register(this.visit(argNode,context)));
            }
        
            Value returnValue = res.register(valueToCallB.execute(args,context));
            return res.success(returnValue);
        }
    }

    public ValueContext visitStringNode(StringNode node, Context context){
        
        MyString str = new MyString(node.token.value.toString());
        str.setContext(context);
        str.setPosition(node.positionStart,node.positionEnd);
        return new ValueContext(str,context);
    }

    public RTResult visitListNode(ListNode node, Context context){
        RTResult res = new RTResult();
        ArrayList<Value> elements = new ArrayList<>();

        for(ASTNode n : node.elementNodes){
            elements.add(this.visit(n, context).value);
            if(res.shouldReturn()) return res;
        }

        MyList list = new MyList(elements);
        res.setContext(context);
        res.setPosition(node.posStart,node.posEnd);

        return res.success(new ValueContext(list, context).value);
    }

    public RTResult visitReturnNode(ReturnNode node, Context context){
        RTResult res = new RTResult();
        Value value = null;

        if(node.nodeToReturn != null){
            value = res.register(this.visit(node.nodeToReturn,context));
            if(res.shouldReturn()) return res;
        }
        return res.successReturn(value);
    }

    public RTResult visitContinueNode(ContinueNode node, Context context){
        RTResult res = new RTResult();
        return res.successContinue();
    }
    public RTResult visitBreakNode(BreakNode node, Context context){
        RTResult res = new RTResult();
        return res.successBreak();
    }
}
