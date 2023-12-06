package controllers;

import com.sun.net.httpserver.HttpExchange;
import models.Item;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static services.RestaurantService.getASingleMenuItem;

public class RestaurantController {

    /* Handler for the "/api/query-items/{tableNumber}" endpoint */
     public static Consumer<HttpExchange> handleQueryItems = ((exchange) -> {

        if("GET".equals(exchange.getRequestMethod())){
            String[] pathSegments = exchange.getRequestURI().getPath().split("/");

            if(pathSegments.length > 3) {
                int tableNumber = Integer.parseInt(pathSegments[3]);
                String response = "Items for the table " + tableNumber + ": [item1, item2, item3]";
                try {
                    UUID fromStringUUID = UUID.fromString("002050ee-1dda-40ac-833d-c3de536287b3");
                    Item item = getASingleMenuItem(fromStringUUID,1);

                    String jsonResponse = String.format(
                            "{\"itemId\":\"%s\",\"itemName\":\"%s\",\"itemCookingTime\":\"%s\",\"tableNo\":%d,\"createdAt\":\"%s\",\"removedAt\":\"%s\",\"isRemoved\":%b}",
                            item.getItemId(),
                            item.getItemName(),
                            item.getItemCookingTime(),
                            item.getTableNo(),
                            item.getCreatedAt(),
                            item.getRemovedAt(),
                            item.getIsRemoved()
                    );
                    sendSuccessResponse(exchange, 200, jsonResponse);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                sendErrorResponse(exchange, 400);
            }
        }else {
            sendErrorResponse(exchange, 405);
        }
    });


    /* Handler for the "/api/add-item" endpoint*/
    private static BiConsumer<HttpExchange, Item> handleAddOrder = (exchange, requestBody) -> {
        if ("POST".equals(exchange.getRequestMethod())) {

        } else {
            sendErrorResponse(exchange, 405); // Method Not Allowed
        }
    };


    private static void sendSuccessResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static void sendErrorResponse(HttpExchange exchange, int statusCode) {
        try{
            exchange.sendResponseHeaders(statusCode,0);
            exchange.close();
        }catch (IOException error){
            error.printStackTrace();
        }
    }
}
