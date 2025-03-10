package universalModels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Game {
    public int gameId;
    public int playerCount;
    public int roundTime;
    public List<Integer> roundList;
    public int gameWinnerPid;

    public Game(int gameId, int playerCount, int roundTime, List<Integer> roundList) {
        this.gameId = gameId;
        this.playerCount = playerCount;
        this.roundList = roundList;
        this.roundTime = roundTime;
    }

    public Game(){

    }
    public Game(int gameId, int playerCount, ArrayList<Integer> roundsPlayed, int gameWinnerPid) {
        this.gameId = gameId;
        this.playerCount = playerCount;
        this.roundList = roundsPlayed;
        this.gameWinnerPid = gameWinnerPid;
    }

    public int getRoundTime() {
        return roundTime;
    }

    public void setRoundTime(int roundTime) {
        this.roundTime = roundTime;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public int getGameWinnerPid() {
        return gameWinnerPid;
    }

    public List<Integer> getRoundList() {
        return roundList;
    }

    public void setRoundList(List<Integer> roundList) {
        this.roundList = roundList;
    }

    public void setGameWinnerPid(int gameWinnerPid) {
        this.gameWinnerPid = gameWinnerPid;
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameId=" + gameId +
                ", playerCount=" + playerCount +
                ", gameWinnerPid=" + gameWinnerPid +
                '}';
    }
}
