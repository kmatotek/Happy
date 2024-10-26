package Values;

import Operators.*;
import SymbolTable.SymbolTable;
import java.util.ArrayList;
import Interpreter.*;
import Context.*;
import DataStructures.*;

public class Function extends BaseFunction {
    public Context context;
    public String name;
    public ASTNode bodyNode;
    public SymbolTable symbolTable;
    public ArrayList<Number> argNodes = new ArrayList<>();
    public ArrayList<String> argNames = new ArrayList<>();

    public Function(String name, ASTNode bodyNode, ArrayList<Number> argNodes, ArrayList<String> argNames){
        super(name);
        this.name = name;
        this.bodyNode = bodyNode;
        this.argNodes = argNodes;
        this.argNames = argNames;
    }

    public Function(String name, ASTNode bodyNode, ArrayList<String> argNames){
        super(name);
        this.name = name;
        this.bodyNode = bodyNode;
        this.argNames = argNames;
    }

    public valueContext execute(ArrayList<Value> args, Context context){
        Interpreter interpreter = new Interpreter();
       
        
        // Add errors for too little and too many args
        this.checkAndPopulateArgs(this.argNames, args, context);
        
        Value value = interpreter.visit(this.bodyNode, context).value;
        return new valueContext(value,context);
    }

    public String toString(){
        return "func: " + this.name + ". args: " + this.argNames.toString();
    }
}
