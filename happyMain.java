import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class happy {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            // Infinite loop to read code from terminal
            System.out.print("Happy > ");
            String input = reader.readLine();
            System.out.println(input);
        }
    }
}
