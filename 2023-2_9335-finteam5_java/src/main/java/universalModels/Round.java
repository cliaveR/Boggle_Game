package universalModels;

public class Round {
    public int roundId;
    public int gameId;
    public int winnerPID;

    public Round(){

    }

    public Round(int roundId, int gameId, int winnerPID){
        this.roundId = roundId;
        this.gameId = gameId;
        this.winnerPID = winnerPID;
    }

    public int getRoundId() {
        return roundId;
    }

    public void setRoundId(int roundId) {
        this.roundId = roundId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getWinnerPID() {
        return winnerPID;
    }

    public void setWinnerPID(int winnerPID) {
        this.winnerPID = winnerPID;
    }

    @Override
    public String toString() {
        return "Round{" +
                "roundId=" + roundId +
                ", gameId=" + gameId +
                ", winnerPID=" + winnerPID +
                '}';
    }
}
