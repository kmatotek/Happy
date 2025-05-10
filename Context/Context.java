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

    public Context getParent() {
        return parent;
    }

    public void setParent(Context parent) {
        this.parent = parent;
    }

    public SymbolTable getSymbolTableObject() {
        return symbolTableObject;
    }

    public void setSymbolTableObject(SymbolTable symbolTableObject) {
        this.symbolTableObject = symbolTableObject;
    }

    public int getParentEntryPos() {
        return parentEntryPos;
    }

    public void setParentEntryPos(int parentEntryPos) {
        this.parentEntryPos = parentEntryPos;
    }

    public HashMap<String, Number> getSymbolTable() {
        return symbolTable;
    }

    public void setSymbolTable(HashMap<String, Number> symbolTable) {
        this.symbolTable = symbolTable;
    }

    public HashMap<String, Number> getParentSymbols() {
        return parentSymbols;
    }

    public void setParentSymbols(HashMap<String, Number> parentSymbols) {
        this.parentSymbols = parentSymbols;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
