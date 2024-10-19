package Values;

import java.util.ArrayList;


public class MyList extends Value {
    public ArrayList<Value> elements;

    public MyList(ArrayList<Value> elements){
        this.elements = elements;
    }

    public MyList addedTo(Value other){
        MyList newList = this.copy();
        newList.elements.add(other);
        return newList;
    }

    public MyList copy(){
        MyList copy = new MyList(this.elements);
        copy.setContext(this.context);
        return copy;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(int i = 0; i < this.elements.size(); i++){
            sb.append(this.elements.get(i).toString());
            if(i != this.elements.size() -1){
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
