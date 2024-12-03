package DataStructures;
import Values.Number;
import Context.*;

public class NumberContext {
    public Number number;
    public Context context;

    public NumberContext(Number num, Context context){
        this.number = num;
        this.context = context;
    }
}