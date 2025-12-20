package Web;

import com.sun.net.httpserver.HttpServer;

import Happy.HappyMain;
import Context.Context;
import SymbolTable.SymbolTable;
import Values.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HappyServer {

    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(
                System.getenv().getOrDefault("PORT", "8080")
        );

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // Serve static files (HTML, CSS, JS)
        server.createContext("/", exchange -> {
            if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            String requestPath = exchange.getRequestURI().getPath();
            if (requestPath.equals("/")) {
                requestPath = "/index.html";
            }

            Path filePath = Paths.get("Web", requestPath);

            if (!Files.exists(filePath) || Files.isDirectory(filePath)) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }

            String contentType = getContentType(filePath.toString());
            exchange.getResponseHeaders().add("Content-Type", contentType);

            byte[] fileBytes = Files.readAllBytes(filePath);
            exchange.sendResponseHeaders(200, fileBytes.length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(fileBytes);
            }
        });

        // Run Happy code
        server.createContext("/run", exchange -> {
            if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            String code = new String(exchange.getRequestBody().readAllBytes());

            // Fresh state per request
            SymbolTable symbolTable = new SymbolTable();
            Context context = new Context("Program");

            // Built-in values
            context.getSymbolTableObject().set("null", new Values.Number(0));
            context.getSymbolTableObject().set("true", new Values.Number(1));
            context.getSymbolTableObject().set("false", new Values.Number(0));
            context.getSymbolTableObject().set("pi", new Values.Number(Math.PI));
            context.getSymbolTableObject().set("happy", new MyString(":)"));

            // Built-in functions
            context.getSymbolTableObject().set("print", new BuiltInFunction("print"));
            context.getSymbolTableObject().set("fac", new BuiltInFunction("factorial"));
            context.getSymbolTableObject().set("length", new BuiltInFunction("length"));

            Value result = HappyMain.run(code, symbolTable, context);

            String response = (result == null) ? "Error" : result.toString();
            byte[] responseBytes = response.getBytes();

            exchange.sendResponseHeaders(200, responseBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        });

        server.start();
        System.out.println("Happy server running at http://localhost:" + port);
    }

    private static String getContentType(String path) {
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        return "application/octet-stream";
    }
}
