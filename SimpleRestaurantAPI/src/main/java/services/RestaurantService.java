package services;

import config.DBConnection;
import models.Item;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RestaurantService {

    /* Get a single item by itemId and table number */
    public static Item getASingleMenuItem (UUID itemId, int tableNo) {
        System.out.println("getASingleMenuItemService " + itemId + tableNo);
        Item item;
        try(Connection connection = DBConnection.getDbConnection.get()){
            String query = "select * from \"MenuItems\" where \"itemId\" = CAST(? AS UUID) and \"tableNo\" = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setObject(1, itemId);
                preparedStatement.setObject(2, tableNo);
                System.out.println("preparedStatement"  +preparedStatement);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if(resultSet.next()) {
                        UUID fromStringUUID = UUID.fromString(resultSet.getString("itemId"));

                        Date sqlDateCreatedAt = resultSet.getDate("createdAt");
                        LocalDateTime localDateTimeCreatedAt = sqlDateCreatedAt.toLocalDate().atStartOfDay();

                        Date sqlDateRemovedAt = resultSet.getDate("removedAt");
                        LocalDateTime localDateTimeRemovedAt  = sqlDateRemovedAt.toLocalDate().atStartOfDay();

                        return new Item(
                                fromStringUUID,
                                resultSet.getString("itemName"),
                                resultSet.getString("itemCookingTime"),
                                resultSet.getInt("tableNo"),
                                localDateTimeCreatedAt,
                                localDateTimeRemovedAt,
                                resultSet.getBoolean("isRemoved")
                        );
                    }
                }
            }
            return null;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    /* Add a single or multiple items for a single table */
    public static String AddItems(Item item) {
        System.out.println("AddItemsService ");
        try(Connection connection = DBConnection.getDbConnection.get()){
            String query = "INSERT INTO \"MenuItems\" " +
                    "(\"itemId\", \"itemName\",\"itemCookingTime\",\"tableNo\",\"createdAt\",\"removedAt\",\"isRemoved\")" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            System.out.println("query" + query);

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setObject(1, item.getItemId());
                preparedStatement.setObject(2, item.getItemName());
                preparedStatement.setObject(3, item.getItemCookingTime());
                preparedStatement.setObject(4, item.getTableNo());
                preparedStatement.setObject(5, item.getCreatedAt());
                preparedStatement.setObject(6, item.getRemovedAt());
                preparedStatement.setObject(7, item.getIsRemoved());

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Insertion successful. Rows affected: " + rowsAffected);
                    return "Insertion successful";
                } else {
                    System.out.println("Insertion failed.");
                    return "Insertion failed.";
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    /* Remove a single item for a table */
    public static String RemoveItem(UUID itemId, int tableNo) {
        System.out.println("RemoveItemService");
        try(Connection connection = DBConnection.getDbConnection.get()){
            String updateQuery = "UPDATE \"MenuItems\" SET \"isRemoved\" = ?,\"removedAt\" = ? WHERE \"tableNo\" = ? AND \"itemId\" = ?";

            System.out.println("updateQuery" + updateQuery);

            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setObject(1, true);
                preparedStatement.setObject(2, LocalDateTime.now());
                preparedStatement.setObject(3, tableNo);
                preparedStatement.setObject(4, itemId);
                System.out.println("preparedStatement" + preparedStatement);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Updated successful. Rows affected: " + rowsAffected);
                    return "Item Removed.";
                } else {
                    System.out.println("Updated failed.");
                    return "Item Removed Failed";
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    /* Query items from table for a given table number and items that are not removed */
    public static JSONArray QueryItemsService(int tableNo) {
        System.out.println("QueryItemsService");
        try(Connection connection = DBConnection.getDbConnection.get()){
            String query = "select * from \"MenuItems\" where \"tableNo\" = ? and \"isRemoved\" = ?";
            System.out.println("query" + query);

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setObject(1, tableNo);
                preparedStatement.setObject(2, false);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    ResultSetMetaData md = resultSet.getMetaData();
                    int numCols = md.getColumnCount();

                    List<String> colNames = IntStream.range(0, numCols)
                            .mapToObj(i -> {
                                try {
                                    return md.getColumnName(i + 1);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                    return "?";
                                }
                            })
                            .collect(Collectors.toList());

                    JSONArray result = new JSONArray();
                        while (resultSet.next()) {
                            JSONObject row = new JSONObject();
                            colNames.forEach(cn -> {
                                try {
                                    row.put(cn, resultSet.getObject(cn));
                                } catch (JSONException | SQLException e) {
                                    e.printStackTrace();
                                }
                            });
                            result.put(row);
                        }
                        return result;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

}
