package universalModels;

import java.util.List;

public class Player {
    public int pid;
    private String username;
    private String password;
    private int points;

    public Player(int pid, String username, String password, int points) {
        this.pid = pid;
        this.username = username;
        this.password = password;
        this.points = points;
    }

    public Player(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Player() {
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
