package controllers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class Helper {

    /* Success Response */
    public static void sendSuccessResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    /* Error Response */
    public static void sendErrorResponse(HttpExchange exchange, int statusCode) {
        try{
            exchange.sendResponseHeaders(statusCode,0);
            exchange.close();
        }catch (IOException error){
            error.printStackTrace();
        }
    }
}
