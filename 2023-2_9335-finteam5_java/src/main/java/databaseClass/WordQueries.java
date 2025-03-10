package databaseClass;

import universalModels.Word;

import java.util.Optional;

import java.sql.*;
import java.util.Scanner;

public class WordQueries {
    private static Connection connection = DbConnect.setConnection();

    public static boolean doesWordExist(String word) {
        String query = "SELECT words FROM word WHERE words = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, word);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

}