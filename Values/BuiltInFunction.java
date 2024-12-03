package Values;

import Context.Context;
import Errors.InvalidSyntaxError;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import Parser.*;

public class BuiltInFunction extends BaseFunction {
    public String name;

    public BuiltInFunction(String name) {
        super(name);  // Call the superclass constructor
        this.name = name;  // Store the function name
    }

    public RTResult execute(ArrayList<Value> args, Context context) {
         // Generate a new execution context
        RTResult res = new RTResult();
        try {
            // Retrieve the method by name; assuming it takes an ArrayList<Value>
            
            Method method = this.getClass().getDeclaredMethod(this.name, Context.class); // Method should accept a Context object

            method.setAccessible(true);
            
            // Check and populate arguments in the new context
            ArrayList<String> argNames = getArgumentNames(method); // Get the expected argument names
            res.register(this.checkAndPopulateArgs(argNames, args, context)); // Check and populate arguments
            if(res.shouldReturn()) return res;

            // Invoke the method with populated args
            
            return res.success((Value) method.invoke(this, context));
             // Pass the context as the argument
        } catch (NoSuchMethodException e) {
            noVisitMethod(); // Handle method not found
        } catch (Exception e) {
            e.printStackTrace(); // Handle any other exceptions
        }
        return res.success(new Number(0));
    }

    // Method to retrieve argument names; in Java, you may need to define these manually
    private ArrayList<String> getArgumentNames(Method method) {
        // Placeholder for argument names; manually define for now
        if (method.getName().equals("print")) {
            return new ArrayList<>(Arrays.asList("value"));
        } else if (method.getName().equals("factorial")) {
            return new ArrayList<>(Arrays.asList("value"));
        } else if (method.getName().equals("length")) {
            return new ArrayList<>(Arrays.asList("value"));
        }
        return new ArrayList<>();
    }

    public void noVisitMethod() {
        throw new InvalidSyntaxError(this.positionStart, this.positionEnd, "Method not defined"); // Handle method not found
    }

    public Value print(Context context) {
        Value v = context.symbolTableObject.get("value");
        return v;
    }

    public Value factorial(Context context) {
        Number num = (Number) context.symbolTableObject.get("value");
        int res = 1;
        for(int i = 1; i <= Number.toInt(num.value); i++){
            res = res * i;
        }
        return new Number(res);
    }

    public Value length(Context context){
        Value v = context.symbolTableObject.get("value");
        if (v instanceof MyString){
            MyString s = (MyString) v;
            return new Number(s.s.length());
        } else if (v instanceof MyList){
            MyList lis = (MyList) v;
            return new Number(lis.elements.size());
        } else {
            throw new IllegalArgumentException("Invalid use of 'LENGTH' method");
        }
    }
  
}
