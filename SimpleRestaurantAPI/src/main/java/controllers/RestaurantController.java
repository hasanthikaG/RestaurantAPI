package controllers;

import com.sun.net.httpserver.HttpExchange;
import models.Item;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static controllers.Helper.sendErrorResponse;
import static controllers.Helper.sendSuccessResponse;
import static services.RestaurantService.*;

public class RestaurantController {

    /* Handler for the "/api/get-items/{tableNumber}/{itemId}" endpoint */
     public static Consumer<HttpExchange> handleGetItems = ((exchange) -> {
        if("GET".equals(exchange.getRequestMethod())){
            String[] pathSegments = exchange.getRequestURI().getPath().split("/");

            if(pathSegments.length > 4) {
                int tableNumber = Integer.parseInt(pathSegments[3]);
                String itemId = pathSegments[4];
                try {
                    UUID fromStringUUID = UUID.fromString(itemId);
                    Item item = getASingleMenuItem(fromStringUUID,tableNumber);
                    System.out.println("item" + item);
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
    public static BiConsumer<HttpExchange, String> handleAddItems = (exchange, item) -> {
        System.out.println("handleAddItems ");

        if ("POST".equals(exchange.getRequestMethod())) {
            try {
                JSONArray jsonArray = new JSONArray(item);
                System.out.println("jsonArray" + jsonArray);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsnObj = jsonArray.getJSONObject(i);

                    Random random = new Random();
                    String randomNumber = new String(String.valueOf(random.nextInt(11) + 5));

                    Item itmObj = new Item(
                            UUID.randomUUID(),
                            jsnObj.getString("itemName"),
                            randomNumber,
                            jsnObj.getInt("tableNo"),
                            LocalDateTime.now(),
                        null,
                        false
                    );
                    String result = AddItems(itmObj);
                }
                sendSuccessResponse(exchange, 200, "result");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            sendErrorResponse(exchange, 405);
        }
    };

    /* Handler for the "/api/remove-item/{tableNumber}/{itemId}" endpoint */
    public static Consumer<HttpExchange> handleRemoveItems = ((exchange) -> {
        if("DELETE".equals(exchange.getRequestMethod())){
            String[] pathSegments = exchange.getRequestURI().getPath().split("/");

            if(pathSegments.length > 4) {
                try {
                    int tableNumber = Integer.parseInt(pathSegments[3]);
                    String itemId = pathSegments[4];
                    UUID fromStringUUID = UUID.fromString(itemId);
                    String result = RemoveItem(fromStringUUID, tableNumber);
                    sendSuccessResponse(exchange, 200, result);
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

    /* Handler for the "/api/query-item/{tableNumber}" endpoint */
    public static Consumer<HttpExchange> handleQueryItems = ((exchange -> {
        if("GET".equals(exchange.getRequestMethod())){
            String[] pathSegments = exchange.getRequestURI().getPath().split("/");

            if(pathSegments.length > 3) {
                int tableNumber = Integer.parseInt(pathSegments[3]);
                try {
                    JSONArray itemList = QueryItemsService(tableNumber);
                    sendSuccessResponse(exchange, 200, itemList.toString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                sendErrorResponse(exchange, 400);
            }
        }else {
            sendErrorResponse(exchange, 405);
        }
    }));

}
