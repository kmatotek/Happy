package Context;

import java.util.HashMap;
import SymbolTable.*;

public class Context {
    public String displayName;
    public HashMap<String,Number> parentSymbols = new HashMap<>();
    public HashMap<String,Number> symbolTable = new HashMap<>();
    public int parentEntryPos;
    public SymbolTable symbolTableObject = new SymbolTable();
    public Context parent;

    public Context(String displayName, HashMap<String,Number> parent, HashMap<String,Number> symbols){
        this.displayName = displayName;
        this.parentSymbols = parent;
        this.symbolTable = symbols;
    }

    public Context(String displayName){
        this.displayName = displayName;
        
    }

    public Context(String displayName, Context parent){
        this.displayName = displayName;
        this.parent = parent;
        
    }
    
}
