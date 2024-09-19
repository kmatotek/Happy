package Happy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.io.IOException;
import Token.*;
import Lexer.*;
import Parser.*;
import Operators.*;

public class HappyMain {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            // Infinite loop to read code from terminal1
            System.out.print("Happy > ");
            String input = reader.readLine();
            ASTNode result = run(input);
            System.out.println(result);
        }
    }
    public static ASTNode run(String text) {
        Lexer lexer = new Lexer(text);
        ArrayList<Token<?>> tokens = lexer.makeTokens();

        Parser parser = new Parser(tokens);
        ASTNode ast = parser.parse();
        
        return ast;
    }
}