package Values;

import Errors.DivideByZero;
import Position.*;

public class Number extends Value {
    public Object value;
    public Position positionStart;
    public Position positionEnd;

    public Number(Object obj) {
        this.value = obj;
    }

    public void setPosition(Position posStart, Position posEnd){
        this.positionStart = posStart;
        this.positionEnd = posEnd;
    }

    public Number addBy(Number other) {
        Object result = performAddition(this.value, other.value);
        return new Number(result);
    }

    public Number subtractBy(Number other) {
        Object result = performSubtraction(this.value, other.value);
        return new Number(result);
    }

    public Number multiplyBy(Number other) {
        Object result = performMultiplication(this.value, other.value);
        return new Number(result);
    }

    public Number divideBy(Number other) {
        Object result = performDivision(this.value, other.value);
        return new Number(result);
    }

    public Number powerBy(Number other) {
        Object result = performPower(this.value, other.value);
        return new Number(result);
    }


    private Object performAddition(Object a, Object b) {
        if (a instanceof Double || b instanceof Double) {
            return toDouble(a) + toDouble(b);
        } else if (a instanceof Float || b instanceof Float) {
            return toFloat(a) + toFloat(b);
        } else if (a instanceof Integer || b instanceof Integer) {
            return toInt(a) + toInt(b);
        } else if (a instanceof Long || b instanceof Long) {
            return toLong(a) + toLong(b);
        } else {
            throw new IllegalArgumentException("Unsupported types for addition.");
        }
    }


    private Object performSubtraction(Object a, Object b) {
        if (a instanceof Double || b instanceof Double) {
            return toDouble(a) - toDouble(b);
        } else if (a instanceof Float || b instanceof Float) {
            return toFloat(a) - toFloat(b);
        } else if (a instanceof Integer || b instanceof Integer) {
            return toInt(a) - toInt(b);
        } else if (a instanceof Long || b instanceof Long) {
            return toLong(a) - toLong(b);
        } else {
            throw new IllegalArgumentException("Unsupported types for addition.");
        }
    }

    private Object performMultiplication(Object a, Object b) {
        if (a instanceof Double || b instanceof Double) {
            return toDouble(a) * toDouble(b);
        } else if (a instanceof Float || b instanceof Float) {
            return toFloat(a) * toFloat(b);
        } else if (a instanceof Integer || b instanceof Integer) {
            return toInt(a) * toInt(b);
        } else if (a instanceof Long || b instanceof Long) {
            return toLong(a) * toLong(b);
        } else {
            throw new IllegalArgumentException("Unsupported types for addition.");
        }
    }

    private Object performDivision(Object a, Object b) {
        if(toDouble(b) == 0){
            throw new DivideByZero();
        }
        if (a instanceof Double || b instanceof Double) {
            return toDouble(a) / toDouble(b);
        } else if (a instanceof Float || b instanceof Float) {
            return toFloat(a) / toFloat(b);
        } else if (a instanceof Integer || b instanceof Integer) {
            return toInt(a) / toInt(b);
        } else if (a instanceof Long || b instanceof Long) {
            return toLong(a) / toLong(b);
        } else {
            throw new IllegalArgumentException("Unsupported types for addition.");
        }
    }

    private Object performPower(Object a, Object b) {
        if(toDouble(b) == 0){
            throw new DivideByZero();
        }
        if (a instanceof Double || b instanceof Double) {
            return Math.pow(toDouble(a),toDouble(b));
        } else if (a instanceof Float || b instanceof Float) {
            return Math.pow(toFloat(a),toFloat(b));
        } else if (a instanceof Integer || b instanceof Integer) {
            return Math.pow(toInt(a),toInt(b));
        } else if (a instanceof Long || b instanceof Long) {
            return Math.pow(toLong(a),toLong(b));
        } else {
            throw new IllegalArgumentException("Unsupported types for addition.");
        }
    }

    private double toDouble(Object value) {
        if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof Integer) {
            return ((Integer) value).doubleValue();
        } else if (value instanceof Float) {
            return ((Float) value).doubleValue();
        } else if (value instanceof Long) {
            return ((Long) value).doubleValue();
        } else {
            throw new IllegalArgumentException("Cannot convert to double.");
        }
    }

    private float toFloat(Object value) {
        if (value instanceof Float) {
            return (Float) value;
        } else if (value instanceof Integer) {
            return ((Integer) value).floatValue();
        } else if (value instanceof Double) {
            return ((Double) value).floatValue();
        } else if (value instanceof Long) {
            return ((Long) value).floatValue();
        } else {
            throw new IllegalArgumentException("Cannot convert to float.");
        }
    }

    public static int toInt(Object value) {
        
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Double) {
            return ((Double) value).intValue();
        } else if (value instanceof Float) {
            return ((Float) value).intValue();
        } else if (value instanceof Long) {
            return ((Long) value).intValue();
        } else {
            throw new IllegalArgumentException("Cannot convert to int.");
        }
    }

    private long toLong(Object value) {
        if (value instanceof Long) {
            return (Long) value;
        } else if (value instanceof Integer) {
            return ((Integer) value).longValue();
        } else if (value instanceof Double) {
            return ((Double) value).longValue();
        } else if (value instanceof Float) {
            return ((Float) value).longValue();
        } else {
            throw new IllegalArgumentException("Cannot convert to long.");
        }
    }

    public Number getComparisonEe(Number other){
        if(this.toDouble(this.value) == other.toDouble(other.value)) return new Number(1);
        else return new Number(0);
    }

  
    public Number notted(){
        if(toInt(this.value) == 1) return new Number(0);
        else return new Number(1);
    }

    public Number orBy(Number other){
        if(this.value == new Number(1).value || other.value == new Number(1).value) return new Number(1);
        else return new Number(0);
    }

    
    public Number getComparisonNe(Number other){
        if(this.toDouble(this.value) != other.toDouble(other.value)) return new Number(1);
        else return new Number(0);
    }

    public Number getComparisonLt(Number other){
        if(this.toDouble(this.value) < other.toDouble(other.value)) return new Number(1);
        else return new Number(0);
    }

    public Number getComparisonGt(Number other){
        if(this.toDouble(this.value) > other.toDouble(other.value)) return new Number(1);
        else return new Number(0);
    }

    public Number getComparisonLte(Number other){
        if(this.toDouble(this.value) <= other.toDouble(other.value)) return new Number(1);
        else return new Number(0);
    }

    public Number getComparisonGte(Number other){
        if(this.toDouble(this.value) >= this.toDouble(other.value)) return new Number(1);
        else return new Number(0);
    }

    
    public Number andBy(Number other){
        double left = toDouble(this.value);
        double right = toDouble(other.value);
        if(left == 0 || right == 0) return new Number(0);
        if(left == right) return new Number(1);
        else return new Number(0);
    }




    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
