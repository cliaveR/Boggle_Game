package databaseClass;

import java.sql.*;

public class GameQueries {
    private static Connection connection = DbConnect.setConnection();
    public static int getLatestGameID(){
        String query = "Select gameID from game order by gameID limit 1";
        try (PreparedStatement ps = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet resultSet = ps.executeQuery();){
            if (resultSet.last()){
                return resultSet.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static boolean getGameExist(int gameId){
        String query = "Select gameID from game Where gameID =?";
        try (PreparedStatement ps = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);)
        {
            ps.setInt(1,gameId);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()){
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getWaitingTime(){
        String query = "Select waitingTime from settings";
        try (PreparedStatement ps = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
             ResultSet resultSet = ps.executeQuery();){
            if (resultSet.last()){
                return resultSet.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void recordRoundWinner(int roundID, int roundWinner){
        String query = "UPDATE rounds set roundWinnerID = ? where roundID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, roundWinner);
            preparedStatement.setInt(2, roundID);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void recordGame(){

    }
    public static int getRoundTime(){
        String query = "Select roundTime from settings";
        try (PreparedStatement ps = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
             ResultSet resultSet = ps.executeQuery();){
            if (resultSet.last()){
                return resultSet.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static synchronized int createGame(int playerCount){
        String query = "insert into game(playerCount, roundsPlayed) values (?, 0)";
        try (PreparedStatement ps = connection.prepareStatement(query)){
            ps.setInt(1, playerCount);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String createdGameID = "select gameID from game order by gameID desc limit 1";
        try (Statement s = connection.createStatement();
        ResultSet resultSet = s.executeQuery(createdGameID);){
            if (resultSet.next()){
                return resultSet.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}
