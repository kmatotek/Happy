package Happy;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Token.*;
import Lexer.*;
import Parser.*;
import Operators.*;
import Interpreter.*;
import Values.Number;
import SymbolTable.*;
import Context.*;

public class HappyMain {
    
    public static void main(String[] args) throws IOException {
            // Infinite loop to read code from terminal1
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Happy > ");
            String input = reader.readLine();
            SymbolTable globalSymbolTable = new SymbolTable();
            globalSymbolTable.set("null",new Number(0));
            

           

            Number result = run(input, globalSymbolTable);
            
            //System.out.println(result);
           
    }

    public static Number run(String text, SymbolTable globalSymbolTable) {
        // Make tokens
        Lexer lexer = new Lexer(text);
        ArrayList<Token<?>> tokens = lexer.makeTokens();
        
        // Generate AST
        Parser parser = new Parser(tokens);
        ASTNode ast = parser.parse();

        // Run Program
        Interpreter interpreter = new Interpreter();
        Context context = new Context("Program");

        Number res = interpreter.visit(ast, context).number;
        globalSymbolTable = context.symbolTableObject;
       // System.out.println(context.symbolTableObject.toString());
        
        //System.out.println(globalSymbolTable.symbols);
        
        return res;
    }
}