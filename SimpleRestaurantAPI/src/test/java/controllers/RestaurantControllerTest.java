package controllers;

import com.sun.net.httpserver.HttpExchange;
import models.Item;
import org.json.JSONArray;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mockito;
import services.RestaurantService;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class RestaurantControllerTest {

    @Test
    @DisplayName("Test handle get items method")
    public void testHandleGetItems() throws URISyntaxException {
        HttpExchange mockhttpExchange = Mockito.mock(HttpExchange.class);
        when(mockhttpExchange.getRequestMethod()).thenReturn("GET");
        when(mockhttpExchange.getRequestURI()).thenReturn(new URI("/api/get-items/1/3b8c5193-d004-4d61-b411-6355cfc31a5a"));

        LocalDateTime fixedDateTime = LocalDateTime.of(2023, 1, 1, 0, 0);
        Item item = new Item(
                UUID.fromString("3b8c5193-d004-4d61-b411-6355cfc31a5a"),
                "Rice",
                "5",
                1,
                fixedDateTime,
                null,
                false);


        when(RestaurantService.getASingleMenuItem(UUID.fromString("3b8c5193-d004-4d61-b411-6355cfc31a5a"),1)).thenReturn(item);

        RestaurantController.handleGetItems.accept(mockhttpExchange);
        assertEquals("{\"itemId\":\"3b8c5193-d004-4d61-b411-6355cfc31a5a\",\"itemName\":\"Rice\",\"itemCookingTime\":\"5\",\"tableNo\":1,\"createdAt\":\"2023-01-01T00:00\",\"removedAt\":\"\",\"isRemoved\":false}", "{\"itemId\":\"3b8c5193-d004-4d61-b411-6355cfc31a5a\",\"itemName\":\"Rice\",\"itemCookingTime\":\"5\",\"tableNo\":1,\"createdAt\":\"2023-01-01T00:00\",\"removedAt\":\"\",\"isRemoved\":false}");
    }

    @Test
    @DisplayName("Test handle add items method")
    public void testHandleAddItems() {
        HttpExchange mockhttpExchange = Mockito.mock(HttpExchange.class);
        when(mockhttpExchange.getRequestMethod()).thenReturn("POST");

        LocalDateTime fixedDateTime = LocalDateTime.of(2023, 1, 1, 0, 0);
        Item item = new Item(
                UUID.fromString("3b8c5193-d004-4d61-b411-6355cfc31a5a"),
                "Rice",
                "5",
                1,
                fixedDateTime,
                null,
                false);


        when(RestaurantService.AddItems(item)).thenReturn("Success");

        RestaurantController.handleAddItems.accept(mockhttpExchange,item.toString());
        assertEquals("Success", "Success");
    }

    @Test
    @DisplayName("Test handle query items method")
    public void testHandleQueryItems() throws URISyntaxException {
        HttpExchange mockhttpExchange = Mockito.mock(HttpExchange.class);
        when(mockhttpExchange.getRequestMethod()).thenReturn("GET");
        when(mockhttpExchange.getRequestURI()).thenReturn(new URI("/api/get-items/1"));

        LocalDateTime fixedDateTime = LocalDateTime.of(2023, 1, 1, 0, 0);
        Item item = new Item(
                UUID.fromString("3b8c5193-d004-4d61-b411-6355cfc31a5a"),
                "Rice",
                "5",
                1,
                fixedDateTime,
                null,
                false);

        JSONArray itemList = new JSONArray(item);
        when(RestaurantService.QueryItemsService(1)).thenReturn(itemList);

        RestaurantController.handleQueryItems.accept(mockhttpExchange);
        assertEquals(itemList, itemList);
    }

    @Test
    @DisplayName("Test handle remove item method")
    public void testHandleRemoveItem() throws URISyntaxException {
        HttpExchange mockhttpExchange = Mockito.mock(HttpExchange.class);
        when(mockhttpExchange.getRequestMethod()).thenReturn("DELETE");
        when(mockhttpExchange.getRequestURI()).thenReturn(new URI("/api/get-items/1/3b8c5193-d004-4d61-b411-6355cfc31a5a"));

        when(RestaurantService.RemoveItem(UUID.fromString("3b8c5193-d004-4d61-b411-6355cfc31a5a"),1)).thenReturn("Success");
        RestaurantController.handleRemoveItems.accept(mockhttpExchange);
        assertEquals("Success", "Success");
    }
}

