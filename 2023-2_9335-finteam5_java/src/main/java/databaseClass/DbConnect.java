package databaseClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnect {
    private static Connection connection;
    public static Connection setConnection(){
        try {
            connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/boggle", "root", "" );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}
