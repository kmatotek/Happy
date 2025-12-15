package Happy;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Interpreter.Interpreter;
import Token.*;
import Lexer.*;
import Parser.*;
import Values.Number;
import SymbolTable.*;
import Context.*;
import Values.*;

public class HappyMain {

    public static SymbolTable globalSymbolTable = new SymbolTable();

    public static void main(String[] args) throws IOException {

        Context context = new Context("Program");

        context.getSymbolTableObject().set("null", new Number(0));
        context.getSymbolTableObject().set("true", new Number(1));
        context.getSymbolTableObject().set("false", new Number(0));
        context.getSymbolTableObject().set("pi", new Number(Math.PI));
        context.getSymbolTableObject().set("happy", new MyString(":)"));

        context.getSymbolTableObject().set("print", new BuiltInFunction("print"));
        context.getSymbolTableObject().set("fac", new BuiltInFunction("factorial"));
        context.getSymbolTableObject().set("length", new BuiltInFunction("length"));

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.print("Happy > ");
            String input = reader.readLine();

            if (input == null) {
                break; // EOF
            }

            if (input.replaceAll(" ", "").isEmpty()) {
                continue;
            }

            Value result = run(input, globalSymbolTable, context);

            if (result instanceof MyList listRes) {
                if (listRes.getElements().size() == 1) {
                    System.out.println(listRes.getElements().get(0));
                } else {
                    System.out.println(result);
                }
            } else {
                System.out.println(result);
            }
        }
    }

    public static Value run(String text, SymbolTable globalSymbolTable, Context context) {

        Lexer lexer = new Lexer(text);
        ArrayList<Token<?>> tokens = lexer.makeTokens();

        Parser parser = new Parser(tokens);
        ParseResult ast = parser.parse();
        if (ast.getError() != null) return null;

        Interpreter interpreter = new Interpreter();
        return interpreter.visit(ast.getNode(), context).getValue();
    }
}
