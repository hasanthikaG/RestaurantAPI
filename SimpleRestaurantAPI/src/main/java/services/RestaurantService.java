package services;

import config.DBConnection;
import models.Item;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.UUID;

public class RestaurantService {

    public static Item getASingleMenuItem (UUID itemId, int tableNo) {
        System.out.println("getASingleMenuItem " + itemId + tableNo);
        Item item;
        try(Connection connection = DBConnection.getDbConnection.get()){
            String query = "select * from \"MenuItems\" where \"itemId\" = CAST(? AS UUID) and \"tableNo\" = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setObject(1, itemId);
                preparedStatement.setObject(2, tableNo);

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

}
