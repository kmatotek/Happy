package Values;

import Operators.*;
import Parser.RTResult;
import SymbolTable.SymbolTable;
import java.util.ArrayList;
import Interpreter.*;
import Context.*;
//import DataStructures.*;

public class Function extends BaseFunction {
    private Context context;
    private String name;
    private ASTNode bodyNode;
    private SymbolTable symbolTable;
    private boolean shouldAutoReturn;
    private ArrayList<Number> argNodes = new ArrayList<>();
    private ArrayList<String> argNames = new ArrayList<>();

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
        if(res.shouldReturn() && res.getFuncReturnValue() == null) return res;


        Value retValue = null;

        if(this.shouldAutoReturn){
            retValue = value;
        } else if (res.getFuncReturnValue() != null){
            retValue = res.getFuncReturnValue();
        }
        return res.success(retValue);
    }

    public String toString(){
        return "func: " + this.name + ". args: " + this.argNames.toString();
    }
}
