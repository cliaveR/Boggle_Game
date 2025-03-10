package databaseClass;
import BoggleApp.NoUserFoundException;
import universalModels.Player;

import java.awt.*;
import  java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerQueries {
    private static Connection connection = DbConnect.setConnection();

    public static boolean validateLogin(String username, String password) throws NoUserFoundException {
        String query = "SELECT playerUsername, playerPassword from player where playerUsername = ?";
        ResultSet rs;
        String usernameFromDB;
        String passwordFromDB;

        try {
            PreparedStatement ps = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                usernameFromDB = rs.getString(1);
                passwordFromDB = rs.getString(2);
            } else {
                throw new NoUserFoundException("No user found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println(username + usernameFromDB);
        System.out.println(password + passwordFromDB);
        boolean result = usernameFromDB.equals(username) && passwordFromDB.equals(password);
        System.out.println("Search result: " + result);
        return result;
    }

    public static void addNewAccount(Player player) {
        String query = "INSERT INTO player ( playerUsername, playerPassword) VALUES ( ?, ?)";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {

            String username = player.getUsername();
            String password = player.getPassword();

            // Check if username is not null or empty
            if (username != null && !username.isEmpty()) {

                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.executeUpdate();
            } else {
                // Handle case where username is null or empty
                System.out.println("Error: Username is null or empty");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Player getUserInfoByUsername(String username) {
        String query = "SELECT * FROM player WHERE playerUsername = ? ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Player(resultSet.getInt("playerID"), resultSet.getString("playerUsername"),
                            resultSet.getString("playerPassword"), resultSet.getInt("points"));
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getAllPlayerNames() {
        String query = "SELECT playerUsername FROM player";
        List<String> playerNames = new ArrayList<>();

        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            while (resultSet.next()) {
                String playerName = resultSet.getString("playerUsername");
                playerNames.add(playerName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return playerNames;
    }



    /**
     * A boolean method that detects if a username is used already by an another player
     *
     * @param username
     * @return
     * @throws SQLException
     */
    public static boolean doesUsernameExist(String username) throws SQLException {
        String query = "SELECT COUNT(*) FROM player WHERE playerUsername = ?";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            return count > 0;

        }
    }


    public static BoggleApp.Player[] viewLeaderboard() {
        String query = "SELECT playerUsername, points FROM player ORDER BY points DESC";
        BoggleApp.Player[] playerList;

        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.last();
            int rows = resultSet.getRow();
            resultSet.beforeFirst();

            playerList = new BoggleApp.Player[rows];
            System.out.println("Number of rows: " + playerList.length);
            int currentIndex = 0;
            while (resultSet.next()) {
                BoggleApp.Player player = new BoggleApp.Player();
                player.pid = 0;
                player.username = resultSet.getString("playerUsername");
                player.points = resultSet.getInt("points");
                playerList[currentIndex] = player;
                currentIndex++;
            }
            resultSet.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (BoggleApp.Player p: playerList){
            System.out.println(p);
        }
        return playerList;
    }


    public static int findPlayerId(String playerUsername) {
        String sql = "SELECT playerId FROM player where playerUsername =?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, playerUsername);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("playerID");
            } else {
                throw new RuntimeException("No playerId found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void editUsername(String oldUsername, String newUsername) {
        String query = "UPDATE player SET playerUsername = ? WHERE playerUsername = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, newUsername);
            preparedStatement.setString(2, oldUsername);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void editPassword(String username, String newPassword) {
        String query = "UPDATE player SET playerPassword = ? WHERE playerUsername = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public static boolean ifWordExist(String playerWordList)
    {

        String sql = "SELECT * From words where word =?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setString(1,playerWordList);
            ResultSet resultSet =  preparedStatement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deletePlayer(String username) {
        try {
            // Check if the username exists in the database
            if (PlayerQueries.doesUsernameExist(username)) {
                // Delete the player from the database
                String query = "DELETE FROM player WHERE playerUsername = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, username);
                    int rowsDeleted = preparedStatement.executeUpdate();
                    if (rowsDeleted > 0) {
                        System.out.println("Player " + username + " deleted successfully.");
                    } else {
                        System.out.println("Failed to delete player " + username + ".");
                    }
                }
            } else {
                System.out.println("Player " + username + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean loginToBoggle(String username, String password){
        String sql = "SELECT playerUsername, playerPassword FROM player WHERE playerUsername = ? AND playerPassword = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1,username);

            preparedStatement.setString(2,password);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public static String getHighestPlayerScoreID(int roundID)
    {
        String sql ="SELECT playerID,MAX(playerscore) AS max_score FROM `playerrounds` " +
                "WHERE roundID = ? ORDER BY max_score DESC;";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, roundID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                int playerId = resultSet.getInt("playerID");
                return getPlayerUsername(playerId);
            }
        return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getPlayerUsername(int playerID){

        String sql = "SELECT playerUsername FROM player WHERE playerID = ? ";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, playerID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getString("playerUsername");
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized int createRound(int gameId){
        String sql ="INSERT INTO rounds (gameID,roundWinnerID) VALUES (?,0)";
        String lastIDQuery = "SELECT roundID FROM rounds ORDER BY roundID DESC LIMIT 1";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            preparedStatement.setInt(1, gameId);
            int rowsInserted = preparedStatement.executeUpdate();
            ResultSet resultSet = statement.executeQuery(lastIDQuery);
            if (rowsInserted > 0) {
                System.out.println("round created successfully!");
            } else {
                System.out.println("Failed to create round.");
            }

            resultSet.next();
            return resultSet.getInt(1);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public static String getGameWinner(int gameId)
    {
        String sql ="SELECT gameID, roundWinnerID, COUNT(*) AS winnerCount\n" +
                "FROM rounds\n" +
                "WHERE gameID = ? AND roundWinnerID != 0"+
                " GROUP BY roundWinnerID\n" +
                "ORDER BY winnerCount DESC;";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, gameId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getInt(3)==3){
                    int playerId = resultSet.getInt("roundWinnerID");
                    return getPlayerUsername(playerId);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static int getPlayerID(String username){
        String query = "Select playerID From player where playerUsername = ?";
        try (PreparedStatement ps = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);)
        {
            ps.setString(1, username);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.last()){
                return resultSet.getInt(1);
            }else {
                return 0;
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
