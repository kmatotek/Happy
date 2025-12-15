package Web;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import Happy.HappyMain;
import Context.Context;
import SymbolTable.SymbolTable;
import Values.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HappyServer {

    public static void main(String[] args) throws IOException {
        int port = 8080;

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);


        // HTML frontend
        server.createContext("/", exchange -> {
            if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            byte[] html = Files.readAllBytes(Paths.get("Web/index.html"));
            exchange.sendResponseHeaders(200, html.length);

            OutputStream os = exchange.getResponseBody();
            os.write(html);
            os.close();
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

            // Happy's values
            context.getSymbolTableObject().set("null", new Values.Number(0));
            context.getSymbolTableObject().set("true", new Values.Number(1));
            context.getSymbolTableObject().set("false", new Values.Number(0));
            context.getSymbolTableObject().set("pi", new Values.Number(Math.PI));
            context.getSymbolTableObject().set("happy", new MyString(":)"));

            // Happy's functions
            context.getSymbolTableObject().set("print", new BuiltInFunction("print"));
            context.getSymbolTableObject().set("fac", new BuiltInFunction("factorial"));
            context.getSymbolTableObject().set("length", new BuiltInFunction("length"));

            Value result = HappyMain.run(code, symbolTable, context);

            String response = (result == null) ? "Error" : result.toString();
            byte[] responseBytes = response.getBytes();

            exchange.sendResponseHeaders(200, responseBytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(responseBytes);
            os.close();
        });

        server.start();
        System.out.println("Happy server running at http://localhost:" + port);
    }
}
