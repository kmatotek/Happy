package Values;

public class Number {
    

    public Number(Object obj){
        if(obj instanceof Integer){
            System.out.println("INT");
        } else if (obj instanceof Double){
            System.out.println("Double");
        }
    }
}
