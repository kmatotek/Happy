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
            Context context = new Context("Program");
            SymbolTable globalSymbolTable = new SymbolTable();
                globalSymbolTable.set("null",new Number(0));
            while(true){
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Happy > ");
                String input = reader.readLine();
                
  
                Number result = run(input, globalSymbolTable, context);

               System.out.println(globalSymbolTable.symbols);
               System.out.println(result);
            }    
    }

    public static Number run(String text, SymbolTable globalSymbolTable, Context context) {
        // Make tokens
        Lexer lexer = new Lexer(text);
        ArrayList<Token<?>> tokens = lexer.makeTokens();
        //System.out.println(tokens);
        
        // Generate AST
        Parser parser = new Parser(tokens);
        ASTNode ast = parser.parse();

        // Run Program
        Interpreter interpreter = new Interpreter();

        Number res = interpreter.visit(ast, context).number;
        globalSymbolTable.symbols = context.symbolTableObject.symbols;
        

        return res;
    }
}