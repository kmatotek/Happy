import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;

public class happyMain {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.print("Happy > ");
            String input = reader.readLine();
            //ArrayList<happy.Token<?>> result = happy.run("happy.java", input); // No need to extend happy

            //System.out.println(result);
        }
    }
}
