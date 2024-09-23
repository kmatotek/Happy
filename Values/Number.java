package Values;

import Position.*;

public class Number {
    private Object value;
    public Position positionStart;
    public Position positionEnd;

    public Number(Object obj) {
        if (obj instanceof Integer || obj instanceof Double || obj instanceof Float || obj instanceof Long) {
            this.value = obj;
            this.setPosition(positionStart, positionEnd);
        } else {
            throw new IllegalArgumentException("Unsupported type for Number class.");
        }
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

    private int toInt(Object value) {
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

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
