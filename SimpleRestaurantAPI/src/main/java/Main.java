import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import controllers.RestaurantController;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.function.BiConsumer;


public class Main {
    public static void main(String[] args) throws IOException {
        int port = 8081;
        HttpServer server = HttpServer.create(new InetSocketAddress(port),0);

        server.createContext("/api/get-item/", createHandler((exchange, _ignored) -> {
            RestaurantController.handleGetItems.accept(exchange);
        }));

        server.createContext("/api/add-item", createHandler((exchange, requestBody) -> {
            RestaurantController.handleAddItems.accept(exchange,requestBody);
        }));

        server.createContext("/api/remove-item/", createHandler((exchange, _ignored) -> {
            RestaurantController.handleRemoveItems.accept(exchange);
        }));

        server.createContext("/api/query-items/", createHandler((exchange, _ignored) -> {
            RestaurantController.handleQueryItems.accept(exchange);
        }));

        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port " + port);
    }

    private interface RequestHandler extends BiConsumer<HttpExchange,String>{}

    private static HttpHandler createHandler(RequestHandler handler) {
        return exchange -> {
            try{
                String requestBody = new String(exchange.getRequestBody().readAllBytes());
                handler.accept(exchange,requestBody);
            }catch (IOException error){
                error.printStackTrace();
            }
        };
    }
}