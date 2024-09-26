package SymbolTable;

import java.util.HashMap;
import Values.Number;

public class SymbolTable {
    public HashMap<String,Number> symbols = new HashMap<>();
    public HashMap<String,Number> parentSymbols = new HashMap<>();
    
    public Number get(String name){
        Number value = symbols.getOrDefault(name,null);
        if(value == null && !parentSymbols.isEmpty()){
            return this.parentSymbols.get(name);
        }
        return value;
    }
 
    public void set(String name, Number value){
        this.symbols.put(name, value);
    }

    public void remove(String name){
        this.symbols.remove(name);
    }

    public String toString(){
        String ans = "";
        for(String s : this.symbols.keySet()){
            ans += "Variable: " + s + " Value: " + symbols.get(s);
        }
        return ans;
    }

}
