package Happy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.io.IOException;
import Token.*;
import Lexer.*;

public class HappyMain {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            // Infinite loop to read code from terminal1
            System.out.print("Happy > ");
            String input = reader.readLine();
            ArrayList<Token<?>> result = run("happy.java", input);
            System.out.println(result);
        }
    }
    public static ArrayList<Token<?>> run(String fileName, String text) {
        Lexer lexer = new Lexer(text);
        return lexer.makeTokens();
    }
}