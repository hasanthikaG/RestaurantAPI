package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Supplier;

public class DBConnection {
    public static Supplier<Connection> getDbConnection = () -> {
        try {
            String url = System.getenv("DB_URL");
            String username = System.getenv("DB_USERNAME");
            String password = System.getenv("DB_PASSWORD");

            Class.forName("org.postgresql.Driver");
            Connection connection =  DriverManager.getConnection(url,username,password);
            System.out.println("Connection" + connection);

            if(connection != null) {
                System.out.println("Connected to the PostgresSQL server successfully.");
            }else {
                System.out.println("DB Connection is null. Check the console for more details.");
            }
            return connection;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };

}
