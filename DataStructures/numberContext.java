package DataStructures;
import Values.Number;
import Context.*;

public class NumberContext {
    private Number number;
    private Context context;

    public NumberContext(Number num, Context context){
        this.number = num;
        this.context = context;
    }

    public Number getNumber() {
        return number;
    }

    public void setNumber(Number number) {
        this.number = number;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}