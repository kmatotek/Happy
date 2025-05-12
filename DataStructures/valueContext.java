package DataStructures;

import Values.Value;
import Context.*;

public class ValueContext {
    private Context context;
    private Value value;

    public ValueContext(Value value, Context context){
        this.value = value;
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }
}
