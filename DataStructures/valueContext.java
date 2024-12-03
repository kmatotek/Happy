package DataStructures;

import Values.Value;
import Context.*;

public class ValueContext {
    public Value value;
    public Context context;

    public ValueContext(Value value, Context context){
        this.value = value;
        this.context = context;
    }
}
