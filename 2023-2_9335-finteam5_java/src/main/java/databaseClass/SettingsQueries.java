package databaseClass;

import universalModels.Settings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class SettingsQueries {
    private static Connection connection = DbConnect.setConnection();
    private static String UPDATE_WAITING_TIME_SQL = "UPDATE settings SET waitingTime = ?;";
    private static String UPDATE_ROUND_TIME_SQL = "UPDATE settings SET roundTime =?; ";


    private static void executeUpdate(String sql, String tableName, String columnName, int num) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(String.format(sql, tableName, columnName))) {
            preparedStatement.setInt(1, num);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateWaitingTime(int waitingTime) {
        executeUpdate(UPDATE_WAITING_TIME_SQL, "settings", "waitingTime", waitingTime);
    }

    public static void updateRoundTime(int roundTime) {
        executeUpdate(UPDATE_ROUND_TIME_SQL, "settings", "roundTime", roundTime);
    }

    public static int getWaitingTime() {
        String query = "SELECT waitingTime FROM settings";
        try (PreparedStatement ps = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
             ResultSet resultSet = ps.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getRoundTime() {
        String query = "SELECT roundTime FROM settings";
        try (PreparedStatement ps = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
             ResultSet resultSet = ps.executeQuery();) {

            if (resultSet.last()) {
                return resultSet.getInt(1);
            }
            return 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
//runner tester
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        boolean exit = false;
//        while (!exit) {
//            System.out.println("1. Update waiting time");
//            System.out.println("2. Update round time");
//            System.out.println("3. Get waiting time");
//            System.out.println("4. Get round time");
//            System.out.println("5. Exit");
//            System.out.print("Enter your choice: ");
//            int choice = scanner.nextInt();
//            switch (choice) {
//                case 1:
//                    System.out.print("Enter new waiting time: ");
//                    int waitingTime = scanner.nextInt();
//                    SettingsQueries.updateWaitingTime(waitingTime);
//                    System.out.println("Waiting time updated successfully.");
//                    break;
//                case 2:
//                    System.out.print("Enter new round time: ");
//                    int roundTime = scanner.nextInt();
//                    SettingsQueries.updateRoundTime(roundTime);
//                    System.out.println("Round time updated successfully.");
//                    break;
//                case 3:
//                    int waitingTimeFromDb = SettingsQueries.getWaitingTime();
//                    System.out.println("Waiting time: " + waitingTimeFromDb);
//                    break;
//                case 4:
//                    int roundTimeFromDb = SettingsQueries.getRoundTime();
//                    System.out.println("Round time: " + roundTimeFromDb);
//                    break;
//                case 5:
//                    exit = true;
//                    break;
//                default:
//                    System.out.println("Invalid choice. Please try again.");
//            }
//        }
//        scanner.close();
//    }
}


