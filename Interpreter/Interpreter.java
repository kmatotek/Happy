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
    private Context Context = new Context("Program");

    public Context getContext() {
        return Context;
    }

    public void setContext(Context context) {
        Context = context;
    }

    // This is the dispatcher, calls the appropriate methods depending on the type of `ASTNode`
    // Each visit returns a `Value` wrapped in `RTResult`
    public RTResult visit(ASTNode node, Context context){
        RTResult res = new RTResult();
        if(node instanceof NumberNode){
            return res.success(new ValueContext(visitNumberNode((NumberNode)node, context).getValue(), context).getValue());
        } else if(node instanceof StringNode){ 
            return res.success(new ValueContext(visitStringNode((StringNode) node, context).getValue(), context).getValue());
        } else if(node instanceof BinOpNode){
            return res.success(new ValueContext(visitBinaryOpNode((BinOpNode)node, context).getValue(), context).getValue());
        } if(node instanceof UnaryOpNode){
            return res.success(new ValueContext(visitUnaryOpNode((UnaryOpNode) node, context).getValue(), context).getValue());
        } else if(node instanceof VarAccessNode){
            return res.success(new ValueContext(visitVarAccessNode((VarAccessNode) node, context).getValue(), context).getValue());
        } else if(node instanceof VarAssignNode){
            return res.success(new ValueContext(visitVarAssignedNode((VarAssignNode) node, context).getValue(), context).getValue());
        } else if(node instanceof IfNode){
            return res.success(new ValueContext(visitIfNode((IfNode) node, context).getValue(), context).getValue());
        } else if(node instanceof ForNode){
            return res.success(new ValueContext(visitForNode((ForNode) node, context).getValue(), context).getValue());
        } else if(node instanceof WhileNode){
            return res.success(new ValueContext(visitWhileNode((WhileNode) node, context).getValue(), context).getValue());
        } else if(node instanceof CallNode){
            return res.success(new ValueContext(visitCallNode((CallNode) node, context).getValue(), context).getValue());
        } else if(node instanceof FuncDefNode){
            return res.success(new ValueContext(visitFuncDefNode((FuncDefNode) node, context).getValue(), context).getValue());
        } else if(node instanceof ListNode){
            return res.success(new ValueContext(visitListNode((ListNode) node, context).getValue(), context).getValue());
        }
        else {
            System.out.println("no instance found");
            return null;
        }
    }

    private void noVisitMethod(ASTNode node, Context context){
        throw new IllegalArgumentException("no visit method");
    }

    private ValueContext visitNumberNode(NumberNode node, Context context){
        Number num = new Number(node.getToken().getValue());
        num.setPosition(node.getPositionStart(),node.getPositionEnd());
        return new ValueContext(num,context);
    }

    private RTResult visitBinaryOpNode(BinOpNode node, Context context){
        RTResult res = new RTResult();

        // Evaluate left node
        Value value1 = res.register(this.visit(node.getLeftNode(), context));
        if(res.shouldReturn()) return res;

        // Evaluate right node
        Value value2 = res.register(this.visit(node.getRightNode(), context));
        if(res.shouldReturn()) return res;

        // Dispatch based on types
        if(value1 instanceof MyString && value2 instanceof MyString){
            
            MyString left = (MyString) value1;
            MyString right = (MyString) value2;
            MyString ans = null;
            if(node.getToken().getType().equals(Token.TT_PLUS)){
                // "hi" + "bye"
                ans = left.addedTo(right);
            } else {
                throw new InvalidSyntaxError(node.getPositionStart(), node.getPositionEnd(), "Invalid operator on Strings");
            }
            return res.success(ans);
        } else if (value1 instanceof MyString && value2 instanceof Number){
            // "hello" * 3
            
            MyString left = (MyString) value1;
            Number right = (Number) value2;
            MyString ans = null;
            if(node.getToken().getType().equals(Token.TT_MUL)){
                int goTo = Number.toInt(right.getValue());
                StringBuilder sb = new StringBuilder();
                for(int i = 0; i < goTo; i++){
                    sb.append(left);
                }
                ans = new MyString(sb.toString());
            } else if (node.getToken().getType().equals(Token.TT_EXCLM)){
                int index = Number.toInt(right.getValue());
                ans = new MyString(String.valueOf(left.getS().charAt(index)));
            } else {
                throw new InvalidSyntaxError(node.getPositionStart(), node.getPositionEnd(), "Invalid operator on Strings");
            }
            return res.success(ans);
        } else if (value1 instanceof MyList && value2 instanceof Number){
            MyList left = (MyList) value1;
            Number right = (Number) value2;
            if(node.getToken().getType().equals(Token.TT_PLUS)){
                // [1,2] + 3
                MyList ans = left.copy();
                ans.getElements().add(right);
                return res.success(ans);

            } else if(node.getToken().getType().equals(Token.TT_EXCLM)){
                // [1,2] ! 0
                
                int get = Number.toInt(right.getValue());
                if(get < 0 || get > left.getElements().size()){
                    throw new InvalidSyntaxError(node.getPositionStart(), node.getPositionEnd(), "Index " + get + " out of bounds");
                }
                Number ans = new Number(left.getElements().get(get));
                return res.success(ans);
            }

        } else if (value1 instanceof MyList && value2 instanceof MyList){
            
            if(node.getToken().getType().equals(Token.TT_PLUS)){
                // [1,2,3] + [4,5]
                MyList left = (MyList) value1;
                MyList right = (MyList) value2;
                MyList ans = left.copy();

                for(Value v : right.getElements()){
                    ans.getElements().add(v);
                }
            
                return res.success(ans);
            } 
        }

        // Lastly, if we've got here, both values are of type `Number`
        Number left = (Number) value1;
        Number right = (Number) value2;
        Number result;

        if(node.getToken().getType().equals(Token.TT_PLUS)){
            result = left.addBy(right);
        } else if(node.getToken().getType().equals(Token.TT_MINUS)){
            result = left.subtractBy(right);
        } else if(node.getToken().getType().equals(Token.TT_MUL)){
            result = left.multiplyBy(right);
        } else if(node.getToken().getType().equals(Token.TT_DIV)){
            result = left.divideBy(right);
        } else if(node.getToken().getType().equals(Token.TT_POW)){
            result = left.powerBy(right);
        } else if(node.getToken().getType().equals(Token.TT_EE)){
            result = left.getComparisonEe(right);
        } else if(node.getToken().getType().equals(Token.TT_NE)){
           result = left.getComparisonNe(right);
        } else if(node.getToken().getType().equals(Token.TT_LT)){
           result = left.getComparisonLt(right);
        } else if(node.getToken().getType().equals(Token.TT_GT)){
            result = left.getComparisonGt(right);
        } else if(node.getToken().getType().equals(Token.TT_LTE)){
           result = left.getComparisonLte(right);
        } else if(node.getToken().getType().equals(Token.TT_GTE)){
            result = left.getComparisonGte(right);
        } else if(node.getToken().matches(Token.TT_KEYWORD, "and")){
            result = left.andBy(right);
        } else if(node.getToken().matches(Token.TT_KEYWORD,"or")){
            result = left.orBy(right);
        } else {
            throw new IllegalArgumentException("not good bro");
        }
        result.setPosition(node.getPositionStart(), node.getPositionEnd());
        return res.success(result);
    }

    private RTResult visitUnaryOpNode(UnaryOpNode node, Context context){
        RTResult res = new RTResult();

        Number num = ((Number) res.register(this.visit(node.getNode(), context)));
        if(res.shouldReturn()) return res;

        if(node.getOpToken().getType().equals(Token.TT_MINUS)){
            // -4
            num = num.multiplyBy(new Number(-1));
        } else if (node.getOpToken().matches(Token.TT_KEYWORD,"not")){
            // not 1
            num = num.notted();
        }
        num.setPosition(node.getPositionStart(), node.getPositionEnd());
        return res.success(num);
    }

    private RTResult visitVarAccessNode(VarAccessNode node, Context context){
        RTResult res = new RTResult();
        Object varName = node.getVarNameToken().getValue();

        // Lookup variable name in symbol table
        Value value = context.getSymbolTableObject().getSymbols().get(varName);

        if(value == null) throw new IllegalArgumentException("Variable " + varName + " is not defined");

        // Attach context and position
        value.setPosition(node.getPositionStart(), node.getPositionEnd());
        value.setContext(context);

        return res.success(value);
    }

    private RTResult visitVarAssignedNode(VarAssignNode node, Context context){
        RTResult res = new RTResult();
        Object varName = node.getVarNameToken().getValue();

        // Evaluate right-hand side
        Value value = this.visit(node.getValueNode(), context).getValue();

        if(res.shouldReturn()) return res;
        if(value == null) throw new IllegalArgumentException("Variable " + varName + " is not defined");

        // Store result in symbol table
        context.getSymbolTableObject().set(varName.toString(), value);
        
        return res.success(value);
    }

    // Conditionals are expressions, not statements (by design choice)
    // ex. var isTrue = if 1 then 1 else 0
    private RTResult visitIfNode(IfNode node, Context context){
        RTResult res = new RTResult();

        boolean shouldReturnNull = node.getElseCase().isShouldReturnNull();

        // Evaluate conditions in order
        for(int i = 0; i < node.getCases().size(); i++){
            ASTNode condition = node.getCases().get(i).getCondition();
            ASTNode expr = node.getCases().get(i).getExpression();
            Number conditionValue = (Number) res.register(this.visit(condition,context));
            if(res.shouldReturn()) return res;

            if(Number.toInt(conditionValue.getValue()) != 0){
                Value exprValue = res.register(this.visit(expr,context));
                if(res.shouldReturn()) return res;
                if(shouldReturnNull){
                    return res.success(null);
                } else {
                return res.success(exprValue);
                }
            } 
        }
        if(node.getElseCase() != null){
            ASTNode expr = node.getElseCase().getElseCase();
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

    private RTResult visitForNode(ForNode node, Context context){
        RTResult res = new RTResult();
        ArrayList<Value> elements = new ArrayList<>();
        
        Number startValue = (Number) res.register(this.visit(node.getStartValueNode(), context));
        if(res.shouldReturn()) return res;
        Number endValue = (Number) res.register(this.visit(node.getEndValueNode(), context));
        if(res.shouldReturn()) return res;
        Number stepValue = null;

        if(node.getStepValueNode() != null){
            stepValue = (Number) res.register(this.visit(node.getStepValueNode(), context));
        } else {
            stepValue = new Number(1);
        }
        
        int i = Number.toInt(startValue.getValue());
        int step = Number.toInt(stepValue.getValue());

        if(step >= 0){
            // Positive step
            while(i < Number.toInt(endValue.getValue())){
                context.getSymbolTableObject().set(node.getVarNameToken().getValue().toString(), new Number(i));
                
                i += step;
                Value value = res.register(this.visit(node.getBodyNode(), context));
                if(res.shouldReturn() && !res.isLoopShouldContinue() && !res.isLoopShouldBreak()) return res;

                if(res.isLoopShouldContinue()) continue;
                if(res.isLoopShouldBreak()) break;
                elements.add(value);
            }
        } else {
            // Negative step value
            while(i > Number.toInt(endValue.getValue())){
                context.getSymbolTableObject().set(node.getVarNameToken().getValue().toString(), new Number(i));
                i += step;
                
                Value value = res.register(this.visit(node.getBodyNode(), context));
                if(res.shouldReturn() && !res.isLoopShouldContinue() && !res.isLoopShouldBreak()) return res;

                if(res.isLoopShouldContinue()) continue;
                if(res.isLoopShouldBreak()) break;
                elements.add(value);
            }
        }
        
        if(node.isShouldReturnNull()) return res.success(null);
        MyList list = new MyList(elements);
        list.setContext(context);
        list.setPosition(node.getPositionStart(),node.getPositionEnd());
        
        return res.success(list);
    }

    private RTResult visitWhileNode(WhileNode node, Context context){
        RTResult res = new RTResult();
        ArrayList<Value> elements = new ArrayList<>();

        while(true){
            Number condition = (Number) res.register(this.visit(node.getConditionNode(), context));
            if(res.shouldReturn()) return res;

            // Evaluate condition each iteration
            if(Number.toInt(condition.getValue()) == 0){
                break;
            }

            Value value = res.register(this.visit(node.getBodyNode(), context));
            if(res.shouldReturn()) return res;
            
            if(res.isLoopShouldContinue()) continue;
            if(res.isLoopShouldBreak()) break;

            elements.add(value);
        }
        
        if(node.isShouldReturnNull()) return res.success(null);
        MyList list = new MyList(elements);
        list.setContext(context);
        list.setPosition(node.getPositionStart(), node.getPositionEnd());
        return res.success(list);
    }

    private RTResult visitFuncDefNode(FuncDefNode node, Context context){
        RTResult res = new RTResult();
        String funcName = node.getVarNameTok().getValue().toString();
        ASTNode bodyNode = node.getBodyNode();
        ArrayList<String> argNames = new ArrayList<>();

        for(Token<?> argName : node.getArgNameTokens()){
            argNames.add(argName.getValue().toString());
        }

        Function funcValue = new Function(funcName, bodyNode, argNames, node.isShouldAutoReturn());

        // Store function in current context
        context.getSymbolTableObject().set(funcName,funcValue);
        
        return res.success(funcValue);
    }

    private RTResult visitCallNode(CallNode node, Context context){
        RTResult res = new RTResult();

        ArrayList<Value> args = new ArrayList<>();
        Value valueToCall = res.register(this.visit(node.getNodeToCall(), context));
        if(res.shouldReturn()) return res;

        if (valueToCall instanceof BuiltInFunction){
            // Built-in function
            BuiltInFunction valueToCallB = (BuiltInFunction) valueToCall;

            // Populate args
            for(ASTNode argNode : node.getArgNodes()){
                args.add(res.register(this.visit(argNode, context)));
                if(res.shouldReturn()) return res;
            }
        
            Value returnValue = res.register(valueToCallB.execute(args,context));
            
            return res.success(returnValue);
        } else {
            // User-designed function
            Function valueToCallB = (Function) valueToCall;

            // Populate args
            for(ASTNode argNode : node.getArgNodes()){
                args.add(res.register(this.visit(argNode,context)));
            }
        
            Value returnValue = res.register(valueToCallB.execute(args,context));
            return res.success(returnValue);
        }
    }

    private ValueContext visitStringNode(StringNode node, Context context){
        MyString str = new MyString(node.getToken().getValue().toString());
        str.setContext(context);
        str.setPosition(node.getPositionStart(),node.getPositionEnd());
        return new ValueContext(str,context);
    }

    private RTResult visitListNode(ListNode node, Context context){
        RTResult res = new RTResult();
        ArrayList<Value> elements = new ArrayList<>();

        for(ASTNode n : node.getElementNodes()){
            elements.add(this.visit(n, context).getValue());
            if(res.shouldReturn()) return res;
        }

        MyList list = new MyList(elements);

        res.setContext(context);
        res.setPosition(node.getPositionStart(), node.getPositionEnd());

        return res.success(new ValueContext(list, context).getValue());
    }

    // Remaining methods are all under construction!
    private RTResult visitReturnNode(ReturnNode node, Context context){
        RTResult res = new RTResult();
        Value value = null;

        if(node.getNodeToReturn() != null){
            value = res.register(this.visit(node.getNodeToReturn(),context));
            if(res.shouldReturn()) return res;
        }
        return res.successReturn(value);
    }

    private RTResult visitContinueNode(ContinueNode node, Context context){
        RTResult res = new RTResult();
        return res.successContinue();
    }
    private RTResult visitBreakNode(BreakNode node, Context context){
        RTResult res = new RTResult();
        return res.successBreak();
    }
}
