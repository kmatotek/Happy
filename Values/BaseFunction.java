package Values;

import Context.*;
import Errors.InvalidSyntaxError;
import SymbolTable.SymbolTable;
import java.util.ArrayList;

public class BaseFunction extends Value {
    public String name;

    public BaseFunction(String name){
        this.name = name;
    }

    public Context generateNewContext(){
        Context newContext = new Context(this.name, this.context);
        newContext.symbolTableObject = new SymbolTable(newContext.parent.symbolTableObject);
        return newContext;
    }

    public void checkArgs(ArrayList<String> argNames, ArrayList<Value> args){
        if(argNames.size() != args.size()){
            throw new InvalidSyntaxError(positionStart, positionEnd, "Illegal amount of arguments");
        }
    }

    public void populateArgs(ArrayList<String> argNames, ArrayList<Value> args, Context context){
        for(int i = 0; i < args.size(); i++){
            String argName = argNames.get(i);
            Value argValue = args.get(i);
            context.symbolTableObject.set(argName,argValue);
        }
    }

    public void checkAndPopulateArgs(ArrayList<String> argNames, ArrayList<Value> args, Context context){
        checkArgs(argNames, args);
        populateArgs(argNames, args, context);
    }
}
