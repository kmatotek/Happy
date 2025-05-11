package Values;

import Context.*;
import Errors.InvalidSyntaxError;
import Parser.RTResult;
import SymbolTable.SymbolTable;
import java.util.ArrayList;

public class BaseFunction extends Value {
    public String name;

    public BaseFunction(String name){
        this.name = name;
    }

    public Context generateNewContext(){
        Context newContext = new Context(this.name, this.context);
        newContext.setSymbolTableObject(new SymbolTable(newContext.getParent().getSymbolTableObject()));
        return newContext;
    }

    public RTResult checkArgs(ArrayList<String> argNames, ArrayList<Value> args){
        RTResult res = new RTResult();

        if(argNames.size() != args.size()){
            throw new InvalidSyntaxError(positionStart, positionEnd, "Illegal amount of arguments");
        }
        return res.success(null);
    }

    public void populateArgs(ArrayList<String> argNames, ArrayList<Value> args, Context context){
        for(int i = 0; i < args.size(); i++){
            String argName = argNames.get(i);
            Value argValue = args.get(i);
            context.getSymbolTableObject().set(argName,argValue);
        }
    }

    public RTResult checkAndPopulateArgs(ArrayList<String> argNames, ArrayList<Value> args, Context context){
        RTResult res = new RTResult();
        res.register(this.checkArgs(argNames, args));
        if(res.shouldReturn()) return res;
        this.populateArgs(argNames, args, context);
        return res.success(null);
    }
}
