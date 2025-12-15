package Values;

public class MyString extends Value {

    private String s;

    public  MyString(String s){
        this.s = s;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public MyString addedTo(MyString other){
        return new MyString(this.s + other.s);
    }

    public MyString multipliedBy(Number other){

        return new MyString(this.s);
    }

    public MyString copy(){
        MyString copy = new MyString(this.s);
        copy.setContext(this.context);
        return copy;
    }

    public String toString(){
        return this.s;
    }
}
