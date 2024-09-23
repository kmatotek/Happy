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

public class HappyMain {
    public static void main(String[] args) throws IOException {
            // Infinite loop to read code from terminal1
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Happy > ");
            String input = reader.readLine();

           

            Number result = run(input);
            System.out.println(result.toString());
    }

    public static Number run(String text) {
        // Make tokens
        Lexer lexer = new Lexer(text);
        ArrayList<Token<?>> tokens = lexer.makeTokens();

        // Generate AST
        Parser parser = new Parser(tokens);
        ASTNode ast = parser.parse();

        // Run Program
        Interpreter interpreter = new Interpreter();
        Number res = interpreter.visit(ast);

        
        return res;
    }
}