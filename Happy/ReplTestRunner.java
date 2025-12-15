package Happy;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class ReplTestRunner {

    public static void main(String[] args) throws Exception {
        Path testRoot = Paths.get("tests");
        boolean allPassed = true;

        try (Stream<Path> paths = Files.walk(testRoot)) {
            for (Path testFile : (Iterable<Path>) paths
                    .filter(p -> p.toString().endsWith(".happy"))
                    .sorted()   // deterministic order
                    ::iterator) {

                try {
                    Path outFile = Paths.get(
                            testFile.toString().replace(".happy", ".out")
                    );

                    if (!Files.exists(outFile)) {
                        throw new RuntimeException(
                                "Missing expected output file: " + outFile
                        );
                    }

                    List<String> lines = Files.readAllLines(testFile);
                    String expected = Files.readString(outFile).trim();

                    Process process = new ProcessBuilder(
                            "java",
                            "-cp", System.getProperty("java.class.path"),
                            "Happy.HappyMain"
                    )
                            .redirectErrorStream(true)
                            .start();

                    // Get input line-by-line
                    try (BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(process.getOutputStream()))) {

                        for (String line : lines) {
                            writer.write(line);
                            writer.newLine();
                            writer.flush();
                        }
                    }

                    // Wait to exit
                    if (!process.waitFor(3, TimeUnit.SECONDS)) {
                        process.destroyForcibly();
                        throw new RuntimeException("Test timed out");
                    }

                    // Read all output
                    String output = new String(
                            process.getInputStream().readAllBytes()
                    );

                    // Normalize output
                    output = output
                            .replaceAll("Happy > ?", "")
                            .replaceAll("\\r\\n", "\n")
                            .trim();

                    if (!output.equals(expected)) {
                        allPassed = false;
                        System.err.println("\n✗ failed :( " + testFile);
                        System.err.println("Expected:");
                        System.err.println(expected);
                        System.err.println("Got:");
                        System.err.println(output);
                    } else {
                        System.out.println("✔ passed! " + testFile);
                    }

                } catch (Exception e) {
                    allPassed = false;
                    System.err.println("\n💥 ERROR: " + testFile);
                    e.printStackTrace();
                }
            }
        }

        if (!allPassed) {
            System.err.println("\n Some tests failed.");
            System.exit(1);
        }

        System.out.println("\n🎉 All tests passed!");
    }
}
