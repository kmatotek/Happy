package DataStructures;

import Values.Value;
import Context.*;

public class valueContext {
    public Value value;
    public Context context;

    public valueContext(Value value, Context context){
        this.value = value;
        this.context = context;
    }
}
