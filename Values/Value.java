package Values;

import Position.Position;
import Context.*;

public abstract class Value {
    private Object value;
    private Position positionStart;
    private Position positionEnd;
    private Context context;

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Position getPositionStart() {
        return positionStart;
    }

    public void setPositionStart(Position positionStart) {
        this.positionStart = positionStart;
    }

    public Position getPositionEnd() {
        return positionEnd;
    }

    public void setPositionEnd(Position positionEnd) {
        this.positionEnd = positionEnd;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context){
        this.context = context;
    }

    public void setPosition(Position posStart, Position posEnd){
        this.positionStart = posStart;
        this.positionEnd = posEnd;
    }
}
