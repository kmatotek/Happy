package Happy;

import java.util.ArrayList;

import Interpreter.Interpreter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Token.*;
import Lexer.*;
import Parser.*;
import Operators.*;
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
            context.symbolTableObject.set("null",new Number(0));
            context.symbolTableObject.set("true",new Number(1));
            context.symbolTableObject.set("false",new Number(0));
            context.symbolTableObject.set("pi",new Number(Math.PI));
            context.symbolTableObject.set("hapy",new MyString(":)"));

            context.symbolTableObject.set("print",new BuiltInFunction("print"));
            context.symbolTableObject.set("fac", new BuiltInFunction("factorial"));
            context.symbolTableObject.set("length", new BuiltInFunction("length"));
            
            
            while(true){
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Happy > ");
                String input = reader.readLine();
                if(input.replaceAll(" ","").equals("")) continue;
                
                //System.out.println(context.symbolTableObject);
                Value result = run(input, globalSymbolTable, context);
                if(result instanceof MyList){
                    MyList listRes = (MyList) result;
                    if(listRes.elements.size() == 1){
                        System.out.println(listRes.elements.get(0));
                    }
                } else {
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
        ASTNode ast = parser.parse();

        // Run Program
        Interpreter interpreter = new Interpreter();

        Value res = interpreter.visit(ast, context).value;
        //globalSymbolTable.symbols = context.symbolTableObject.symbols;
        

        return res;
    }
}