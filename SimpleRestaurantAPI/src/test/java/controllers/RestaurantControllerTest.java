package controllers;

import com.sun.net.httpserver.HttpExchange;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mockito;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class RestaurantControllerTest {
    @Test
    @DisplayName("Test handle items method")
    public void testHandleAddItems() throws IOException {
        RestaurantController yourClass = new RestaurantController();
        HttpExchange mockExchange = Mockito.mock(HttpExchange.class);
        when(mockExchange.getRequestMethod()).thenReturn("POST");

        String jsonInput = "[{\"itemName\":\"SampleItem\",\"tableNo\":1}]";

        yourClass.handleAddItems.accept(mockExchange, jsonInput);

        verify(mockExchange, times(1)).getRequestMethod();
        verify(mockExchange, times(1)).getResponseBody();
        verify(mockExchange, times(1)).sendResponseHeaders(eq(200), anyLong());

    }
}
