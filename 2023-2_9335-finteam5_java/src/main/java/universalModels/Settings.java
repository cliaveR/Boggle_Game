package universalModels;

public class Settings {
    public int waitingTime;
    public int roundTime;

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getRoundTime() {
        return roundTime;
    }

    public void setRoundTime(int roundTime) {
        this.roundTime = roundTime;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "waitingTime=" + waitingTime +
                ", roundTime=" + roundTime +
                '}';
    }

}
