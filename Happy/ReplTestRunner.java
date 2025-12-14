package Happy;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ReplTestRunner {

    public static void main(String[] args) throws Exception {
        Path testRoot = Paths.get("tests");

        Files.walk(testRoot)
                .filter(p -> p.toString().endsWith(".happy"))
                .forEach(testFile -> {
                    try {
                        Path outFile = Paths.get(
                                testFile.toString().replace(".happy", ".out")
                        );

                        List<String> lines = Files.readAllLines(testFile);
                        String expected = Files.readString(outFile).trim();

                        Process process = new ProcessBuilder(
                                "java",
                                "-cp", "out/production/Happy",
                                "Happy.HappyMain"
                        )
                                .redirectErrorStream(true)
                                .start();

                        //  Write input line by line
                        try (BufferedWriter writer = new BufferedWriter(
                                new OutputStreamWriter(process.getOutputStream()))) {

                            for (String line : lines) {
                                writer.write(line);
                                writer.newLine();
                                writer.flush(); // simulate pressing ENTER
                            }
                        }

                        // Wait for REPL to exit cleanly
                        if (!process.waitFor(3, TimeUnit.SECONDS)) {
                            process.destroyForcibly();
                            throw new RuntimeException("Test timed out: " + testFile);
                        }

                        // Read all output
                        String output = new String(
                                process.getInputStream().readAllBytes()
                        );

                        // Normalize
                        output = output
                                .replaceAll("Happy > ?", "")
                                .replaceAll("\\r\\n", "\n")
                                .trim();

                        if (!output.equals(expected)) {
                            System.err.println("\n❌ FAILED: " + testFile);
                            System.err.println("Expected:");
                            System.err.println(expected);
                            System.err.println("Got:");
                            System.err.println(output);
                            System.exit(1);
                        } else {
                            System.out.println("✅ PASSED: " + testFile);
                        }

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        System.out.println("\n🎉 All Happy REPL tests passed");
    }
}
