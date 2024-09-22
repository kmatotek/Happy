package Values;

import Position.*;

public class Number {
    private int value;
    Position posStart;
    Position posEnd;
    
    public Number(int value){
        this.value = value;
        this.setPosition(posStart, posEnd);
    }

    public void setPosition(Position posStart, Position posEnd){
        this.posStart = posStart;
        this.posEnd = posEnd;
    }

    public Number addBy(Number other){
        return new Number(this.value + other.value);
    }

    public Number subtractBy(Number other){
        return new Number(this.value - other.value);
    }

    public Number multiplyBy(Number other){
        return new Number(this.value * other.value);
    }

    public Number divideBy(Number other){
        return new Number(this.value / other.value);
    }

    public String toString(){
        return String.valueOf(this.value);
    }
}
