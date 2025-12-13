package Happy;

import java.util.ArrayList;

import Interpreter.Interpreter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Token.*;
import Lexer.*;
import Parser.*;
//import Operators.*;
//import Interpreter.*;
import Values.Number;
import SymbolTable.*;
import Context.*;
import Values.*;

public class HappyMain {
    public static SymbolTable globalSymbolTable = new SymbolTable();
    public static void main(String[] args) throws IOException {
            // Infinite loop to read code from terminal1
            Context context = new Context("Program");
            //SymbolTable globalSymbolTable = new SymbolTable();
            context.getSymbolTableObject().set("null",new Number(0));
            context.getSymbolTableObject().set("true",new Number(1));
            context.getSymbolTableObject().set("false",new Number(0));
            context.getSymbolTableObject().set("pi",new Number(Math.PI));
            context.getSymbolTableObject().set("hapy",new MyString(":)"));

            context.getSymbolTableObject().set("print",new BuiltInFunction("print"));
            context.getSymbolTableObject().set("fac", new BuiltInFunction("factorial"));
            context.getSymbolTableObject().set("length", new BuiltInFunction("length"));
            
            
            while(true){
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Happy > ");
                String input = reader.readLine();
                if(input.replaceAll(" ", "").isEmpty()) continue;
                
                //System.out.println(context.symbolTableObject);
                Value result = run(input, globalSymbolTable, context);
                if(result instanceof MyList listRes){
                    if(listRes.elements.size() == 1){
                        System.out.println(listRes.elements.get(0));
                    } else {
                        System.out.println(result);
                    }
                }else {
                    System.out.println(result);
                }
            }    
    }

    public static Value run(String text, SymbolTable globalSymbolTable, Context context) {
        // Make tokens
        Lexer lexer = new Lexer(text);
        ArrayList<Token<?>> tokens = lexer.makeTokens();
        //System.out.println(tokens);
        
        // Generate AST
        Parser parser = new Parser(tokens);
        ParseResult ast = parser.parse();
        if(ast.getError() != null) return null;

        // Run Program
        Interpreter interpreter = new Interpreter();

        Value res = interpreter.visit(ast.getNode(), context).getValue();
        //globalSymbolTable.symbols = context.symbolTableObject.symbols;

        return res;
    }
}