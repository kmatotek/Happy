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

public class HappyMain {
    public static void main(String[] args) throws IOException {
            // Infinite loop to read code from terminal1
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Happy > ");
            String input = reader.readLine();

            ASTNode result = run(input);
            System.out.println(result.toString());
    }

    public static ASTNode run(String text) {

        // Generate tokens
        Lexer lexer = new Lexer(text);
        ArrayList<Token<?>> tokens = lexer.makeTokens();

        // Generate AST
        Parser parser = new Parser(tokens);
        ASTNode ast = parser.parse();

        // Run program
        Interpreter interpreter = new Interpreter();
        interpreter.visit(ast);
        
        return ast;
    }
}