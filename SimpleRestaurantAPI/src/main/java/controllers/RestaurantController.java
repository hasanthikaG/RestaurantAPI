package controllers;

import com.sun.net.httpserver.HttpExchange;
import models.Item;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static controllers.Helper.sendErrorResponse;
import static controllers.Helper.sendSuccessResponse;
import static controllers.ItemValidator.*;
import static services.RestaurantService.*;

public class RestaurantController {

    /* Handler for the "/api/get-items/{tableNumber}/{itemId}" endpoint */
     public static Consumer<HttpExchange> handleGetItems = ((exchange) -> {
        if("GET".equals(exchange.getRequestMethod())){
            String[] pathSegments = exchange.getRequestURI().getPath().split("/");

            if(pathSegments.length > 4) {
                int tableNumber = Integer.parseInt(pathSegments[3]);
                String itemId = pathSegments[4];
                UUID fromStringUUID = UUID.fromString(itemId);

                Item item = getASingleMenuItem(fromStringUUID,tableNumber);
                System.out.println("item" + item);

                Optional.ofNullable(item)
                            .ifPresentOrElse(res -> {
                                        try {
                                          String jsonResponse = String.format(
                                                    "{\"itemId\":\"%s\",\"itemName\":\"%s\",\"itemCookingTime\":\"%s\",\"tableNo\":%d,\"createdAt\":\"%s\",\"removedAt\":\"%s\",\"isRemoved\":%b}",
                                                  res.getItemId(),
                                                  res.getItemName(),
                                                  res.getItemCookingTime(),
                                                  res.getTableNo(),
                                                  res.getCreatedAt(),
                                                  res.getRemovedAt(),
                                                  res.getIsRemoved()
                                          );
                                            sendSuccessResponse(exchange, 200, jsonResponse);
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    },
                                    () -> {
                                        try {
                                            sendSuccessResponse(exchange, 200, "NO_DATA");
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    });

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
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsnObj = jsonArray.getJSONObject(i);
                    Random random = new Random();
                    String randomNumber = String.valueOf(random.nextInt(11) + 5);

                    Item itmObj = new Item(
                            UUID.randomUUID(),
                            jsnObj.getString("itemName"),
                            randomNumber,
                            jsnObj.getInt("tableNo"),
                            LocalDateTime.now(),
                        null,
                        false
                    );
                    ItemValidator.ValidationResult result = isItemNameValid()
                            .and(isTableNumberExist())
                            .and(isTableNumberValid()).apply(itmObj);
                    System.out.println(result);

                    if(result != ValidationResult.SUCCESS){
                        sendSuccessResponse(exchange, 200, result.name());
                    }
                    AddItems(itmObj);
                }

                sendSuccessResponse(exchange, 200, "Success");
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

                int tableNumber = Integer.parseInt(pathSegments[3]);
                String itemId = pathSegments[4];
                UUID fromStringUUID = UUID.fromString(itemId);

                String result = RemoveItem(fromStringUUID, tableNumber);
                Optional.ofNullable(result)
                            .ifPresentOrElse(res -> {
                                        try {
                                            sendSuccessResponse(exchange, 200, res);
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    },
                                    () -> {
                                        try {
                                            sendSuccessResponse(exchange, 200, "NO_DATA");
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    });

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
                JSONArray itemList = QueryItemsService(tableNumber);

                Optional.ofNullable(itemList)
                            .ifPresentOrElse(res -> {
                                        try {
                                            sendSuccessResponse(exchange, 200, res.toString());
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    },
                                    () -> {
                                        try {
                                            sendSuccessResponse(exchange, 200, "NO_DATA");
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    });

            } else {
                sendErrorResponse(exchange, 400);
            }
        }else {
            sendErrorResponse(exchange, 405);
        }
    }));

}
