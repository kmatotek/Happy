package Parser;
import Values.*;
//import DataStructures.*;

public class RTResult extends Value{
    private Value value;
    private Error error;
    private Value funcReturnValue;
    private boolean loopShouldContinue;
    private boolean loopShouldBreak;
    
    public RTResult(){
        this.reset();
    }

    public void reset(){
        this.value = null;
        this.error = null;
        this.funcReturnValue = null;
        this.loopShouldContinue = false;
        this.loopShouldBreak = false;
    }

    public Value getValue() {
        return value;
    }

    public Value getFuncReturnValue() {
        return funcReturnValue;
    }

    public boolean isLoopShouldContinue() {
        return loopShouldContinue;
    }

    public boolean isLoopShouldBreak() {
        return loopShouldBreak;
    }

    public Value register(RTResult res){
        this.error = res.error;
        this.funcReturnValue = res.funcReturnValue;
        this.loopShouldContinue = res.loopShouldContinue;
        this.loopShouldBreak = res.loopShouldBreak;
        return res.value;
    }

    public RTResult success(Value value){
        this.reset();
        this.value = value;
        return this;
    }

    public RTResult successReturn(Value value){
        this.reset();
        this.funcReturnValue = value;
        return this;
    }

    public RTResult successContinue(){
        this.reset();
        this.loopShouldContinue = true;
        return this;
    } 

    public RTResult successBreak(){
        this.reset();
        this.loopShouldBreak = true;
        return this;
    }

    public boolean shouldReturn(){
        return (this.error != null || this.funcReturnValue != null || this.loopShouldContinue || this.loopShouldBreak);
    }
}
