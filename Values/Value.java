package Values;

import Position.Position;
import Context.*;

public abstract class Value {
    public Object value;
    public Position positionStart;
    public Position positionEnd;
    public Context context;

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
    
    public void setContext(Context context){
        this.context = context;
    }

    public void setPosition(Position posStart, Position posEnd){
        this.positionStart = posStart;
        this.positionEnd = posEnd;
    }
}
