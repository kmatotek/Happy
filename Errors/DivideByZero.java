package Errors;


public class DivideByZero extends RuntimeException {
    public DivideByZero(){
        super("Divide by zero error");
    }
}
