package SymbolTable;

import java.util.HashMap;
import Values.*;

public class SymbolTable {
    public HashMap<String,Value> symbols = new HashMap<>();
    public HashMap<String,Value> parentSymbols = new HashMap<>();
    public SymbolTable parent;

    public SymbolTable(SymbolTable parent){
        this.parent = parent;
    }

    public SymbolTable(){
    }
    
    public Value get(String name){
        Value value = symbols.getOrDefault(name,null);
        if(value == null && !parentSymbols.isEmpty()){
            return this.parentSymbols.get(name);
        }
        return value;
    }
 
    public void set(String name, Value value){
        this.symbols.put(name, value);
    }

    public void remove(String name){
        this.symbols.remove(name);
    }

    public String toString(){
        String ans = "";
        for(String s : this.symbols.keySet()){
            ans += "Variable: " + s + " Value: " + symbols.get(s) + " ";
        }
        return ans;
    }

}
