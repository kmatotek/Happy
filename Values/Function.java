package Values;

import Operators.*;
import Parser.RTResult;
import SymbolTable.SymbolTable;
import java.util.ArrayList;
import Interpreter.*;
import Context.*;
//import DataStructures.*;

public class Function extends BaseFunction {
    public Context context;
    public String name;
    public ASTNode bodyNode;
    public SymbolTable symbolTable;
    public boolean shouldAutoReturn;
    public ArrayList<Number> argNodes = new ArrayList<>();
    public ArrayList<String> argNames = new ArrayList<>();

    public Function(String name, ASTNode bodyNode, ArrayList<Number> argNodes, ArrayList<String> argNames){
        super(name);
        this.name = name;
        this.bodyNode = bodyNode;
        this.argNodes = argNodes;
        this.argNames = argNames;
    }

    public Function(String name, ASTNode bodyNode, ArrayList<String> argNames, boolean shouldAutoReturn){
        super(name);
        this.name = name;
        this.bodyNode = bodyNode;
        this.argNames = argNames;
        this.shouldAutoReturn = shouldAutoReturn;
    }

    public RTResult execute(ArrayList<Value> args, Context context){
        Interpreter interpreter = new Interpreter();
        RTResult res = new RTResult();
        
        // Add errors for too little and too many args
        res.register(this.checkAndPopulateArgs(this.argNames, args, context));
        if(res.shouldReturn()) return res;
        
        
        Value value = res.register(interpreter.visit(this.bodyNode, context));
        if(res.shouldReturn() && res.funcReturnValue == null) return res;


        Value retValue = null;

        if(this.shouldAutoReturn){
            retValue = value;
        } else if (res.funcReturnValue != null){
            retValue = res.funcReturnValue;
        }
        return res.success(retValue);
    }

    public String toString(){
        return "func: " + this.name + ". args: " + this.argNames.toString();
    }
}
