package universalModels;

import BoggleApp.Player;
import databaseClass.SettingsQueries;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    public static List<Player> waitingPlayers = new ArrayList<>();
    public static boolean sufficientPlayers = false;
    private static int waitingTime;
    public static void addPlayer(Player player){
        if (waitingPlayers.isEmpty()){
            waitingTime = SettingsQueries.getWaitingTime();
        }
        Player newPlayer = new Player();
        newPlayer.username = player.username;
        newPlayer.points = 0;
        waitingPlayers.add(newPlayer);

        try {
            while(waitingTime>0){
                Thread.sleep(1000);
                waitingTime--;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        sufficientPlayers = waitingPlayers.size()>1;
    }

    public static void resetFields() {
        waitingPlayers.removeAll(waitingPlayers);
        sufficientPlayers =false;
    }
}
