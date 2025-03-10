package Server_Java;

import BoggleApp.*;
import databaseClass.*;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import universalModels.Game;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

class PlayerImplementation extends PlayerServantPOA {
    private static List<Game> onGoingGames = new ArrayList<>();
    private static List<Integer> waitingPlayers = new ArrayList<>();
    private static Map<Integer, Integer> playerGameMap = new ConcurrentHashMap<>();
    private static Map<Integer, char[]> roundLettersMap = new ConcurrentHashMap<>();
    private static Map<Integer, ArrayList<String>> playerWordLists = new ConcurrentHashMap<>();
    private static Map<Integer, Integer> playerScores = new ConcurrentHashMap<>();
    private static Map<Integer, Map<Integer, List<String>>> roundPlayerMap = new ConcurrentHashMap<>();
    private static Map<Integer, Map<Integer, Integer>> gameRoundWinnerMap = new ConcurrentHashMap<>();
    private static int runningWaitTime = SettingsQueries.getWaitingTime();
    int gameHolder;

    public char[] createRandomLetters() {
        System.out.println("The user has used the createRandomLetters() method");
        char[] consonants = {'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'y', 'z'};
        char[] vowels = {'a', 'e', 'i', 'o', 'u'};
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            char letter = (i < 13) ? consonants[random.nextInt(consonants.length)] : vowels[random.nextInt(vowels.length)];
            stringBuilder.append(letter);
        }
        return stringBuilder.toString().toCharArray();
    }
    @Override
    public int logInToGame(String username, String password) throws NoUserFoundException {
        boolean success = PlayerQueries.loginToBoggle(username, password);
        if (success) {
            return PlayerQueries.getPlayerID(username);
        }
        return 0;
    }
    @Override
    public Player[] getLeaderBoard() {
        return PlayerQueries.viewLeaderboard();
    }
    @Override
    public int getWaitingTime() {
        return runningWaitTime;
    }
    public static void resetStartGameFields() {
        waitingPlayers.clear();
    }
    @Override
    public int startGame(int playerID) {
        Thread joinThread = null;

        if (waitingPlayers.isEmpty()) {
            System.out.println("Currently No players");
            System.out.println("Adding player: Player " + playerID);
            waitingPlayers.add(playerID);
            runningWaitTime = SettingsQueries.getWaitingTime();
            joinThread = new Thread(() -> {
                System.out.println("Waiting time countdown RUNNING...");
                while (runningWaitTime > 0) {
                    try {
                        Thread.sleep(1000);
                        runningWaitTime--;
                        System.out.println("Running time: " + runningWaitTime);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            joinThread.start();
        } else {
            waitingPlayers.add(playerID);
            System.out.println("Adding player: Player " + playerID);
            joinThread = new Thread(() -> {
                while (runningWaitTime > 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            joinThread.start();
        }

        Thread checkPlayersThread = new Thread(() -> {
            System.out.println("Check if game proceeds");
            if (waitingPlayers.size() >= 2) {
                System.out.println("Game proceeded");
                if (playerGameMap.containsKey(playerID)) {
                    gameHolder = playerGameMap.get(playerID);
                    System.out.println("Game ID from map: " + gameHolder);
                } else {
                    int gamePlayerCount = waitingPlayers.size();
                    System.out.println("Game playerCount: " + gamePlayerCount);
                    gameHolder = GameQueries.createGame(gamePlayerCount);
                    for (int player : waitingPlayers) {
                        playerGameMap.put(player, gameHolder);
                        int roundTime = SettingsQueries.getRoundTime();
                        System.out.println("ROUND TIME FROM SQL: " + roundTime);
                        onGoingGames.add(new Game(gameHolder, gamePlayerCount, roundTime, new ArrayList<>()));
                        playerWordLists.put(player, new ArrayList<>());
                        playerScores.put(player, 0); // Initialize the score for each player
                    }
                    System.out.println("Game ID: " + gameHolder);
                }
            } else {
                gameHolder = 0;
            }
        });

        try {
            joinThread.join();
            checkPlayersThread.start();
            Thread.sleep(2000);
            resetStartGameFields();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return gameHolder;
    }
    @Override
    public int getRoundTime(int gameID) {
        System.out.println("FETCHING ROUND TIME OF GAME " + gameID);
        for (Game game: onGoingGames){
            System.out.println("Round time for game " + game.getGameId() + ": " + game.getRoundTime());
            if (game.getGameId() == gameID){
                int roundTime = game.getRoundTime();
                System.out.println("Running round time: " + roundTime);
                return roundTime;
            }
        }
        return 0;
    }
    @Override
    public int startRound(int gameID, int round) {
        for (int i = 0; i < onGoingGames.size();i++){
            if (onGoingGames.get(i).gameId == gameID){
                List<Integer> roundList = onGoingGames.get(i).getRoundList();
                if (round<=roundList.size()){
                    return roundList.get(round-1);
                }else {
                    int roundID =  PlayerQueries.createRound(gameID);
                    onGoingGames.get(i).getRoundList().add(roundID);
                    roundLettersMap.put(roundID, createRandomLetters());
                    return roundID;
                }
            }
        }
        return 0;
    }
    @Override
    public char[] getLetterSet(int gameID, int roundID) {
        return roundLettersMap.get(roundID);
    }
    @Override
    public boolean checkWord(String word, int playerID, int roundID) {
        System.out.println("CHECKING WORD FOR PALYER  " + playerID);
        System.out.println("CHECKING WORD \"" + word + "\"");

        //CHECK WORD LENGTH
        if (word.length() < 4 || word.length() > 20) {
            System.out.println("LENGTH INSUFFICIENT...");
            return false;
        }

        if (!roundPlayerMap.containsKey(roundID)){
            System.out.println("//NO ROUND RECORD NO PLAYER RECORD");
            HashMap<Integer, List<String>> playerRecord = new HashMap<>();
            //Create a player record
            playerRecord.put(playerID, new ArrayList<>());
            //Insert round record and player record
            roundPlayerMap.put(roundID, playerRecord);

            //TODO check word if it matches the letters given in the round
            if (matchesRoundLetters(word, roundID)){
                // TODO check word in database
                if (WordQueries.doesWordExist(word)){
                    roundPlayerMap.get(roundID).get(playerID).add(word);
                    System.out.println("Player words: ");
                    for (String s: roundPlayerMap.get(roundID).get(playerID)){
                        System.out.println(s);
                    }
                    return true;
                }
                System.out.println("WORD DOES NOT EXIST IN DATABASE ...[1]");
                return false;
            }
            System.out.println("WORD DOES NOT MATCH ROUND LETTERS...[1]");
            return false;
        }else {
            if (!roundPlayerMap.get(roundID).containsKey(playerID)){
                System.out.println("//HAS ROUND RECORD BUT NO PLAYER RECORD");
                HashMap<Integer, List<String>> newPlayerRecord = new HashMap<>();
                newPlayerRecord.put(playerID, new ArrayList<>());
                roundPlayerMap.get(roundID).put(playerID, new ArrayList<>());
                //TODO check word if it matches the letters given in the round
                if (matchesRoundLetters(word, roundID)){
                    // TODO check word in database
                    if (WordQueries.doesWordExist(word)){
                        roundPlayerMap.get(roundID).get(playerID).add(word);
                        System.out.println("Player words: ");
                        for (String s: roundPlayerMap.get(roundID).get(playerID)){
                            System.out.println(s);
                        }
                        return true;
                    }
                    System.out.println("WORD DOES NOT EXIST IN DATABASE ...[2]");
                    return false;
                }
                System.out.println("WORD DOES NOT MATCH ROUND LETTERS...[2]");
                return false;
            }else {
                //HAS ROUND RECORD AND HAS PLAYER RECORD
                //TODO check word if already entered
                if (wordAlreadyEnteredByPlayer(word, playerID, roundID)){
                    System.out.println("WORD ALREADY ENTERED BY PLAYER ...[3]");
                    return false;
                }else {
                    //TODO check word if it matches the letters given in the round
                    if (matchesRoundLetters(word, roundID)){
                        // TODO check word in database
                        if (WordQueries.doesWordExist(word)){
                            roundPlayerMap.get(roundID).get(playerID).add(word);
                            System.out.println("Player words: ");
                            for (String s: roundPlayerMap.get(roundID).get(playerID)){
                                System.out.println(s);
                            }
                            return true;
                        }
                        System.out.println("WORD DOES NOT EXIST IN DATABASE ...[3]");
                        return false;
                    }
                    System.out.println("WORD DOES NOT MATCH ROUND LETTERS...[3]");
                    return false;
                }
            }
        }
    }
    private boolean matchesRoundLetters(String word, int roundID){
        char[] letterSequence = roundLettersMap.get(roundID).clone();
        StringBuilder foundCharacters = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            char inputWordToken = word.charAt(i);
            for (int j = 0; j < letterSequence.length; j++) {
                if (inputWordToken == letterSequence[j]){
                    foundCharacters.append(letterSequence[j]);
                    letterSequence[j] = ' ';
                    break;
                }
            }
        }
        return word.equalsIgnoreCase(foundCharacters.toString());
    }
    private boolean wordAlreadyEnteredByPlayer(String word, int playerID, int roundID) {
        // Convert the word to lower case to ensure case-insensitivity
        String lowerCaseWord = word.toLowerCase();

        //Get the word list of the player
        List<String> playerWordList = roundPlayerMap.get(roundID).get(playerID);

        return playerWordList.contains(word);
    }
    @Override
    public String getRoundWinner(int roundID) {
        Map<Integer, Integer> playerScoreMap = new ConcurrentHashMap<>();
        Map<Integer, List<String>> playersInRound = roundPlayerMap.get(roundID);
        System.out.println("$$   Get list of players in the round...");
        //Get list of players in the round
        Set<Integer> playerList =  playersInRound.keySet();
        for (int player: playerList){
            //Iterate through each player
            //Iterate through all players in the round
            List<String> uniqueWordsOfPlayer = playersInRound.get(player);
            for (int playerIteration: playerList){
                if (!(playerIteration==player)){
                    System.out.println("$$   Comparing word list of player " + player + " to wordlist of player " + playerIteration);
                    uniqueWordsOfPlayer = compareWords(playersInRound,uniqueWordsOfPlayer, playerIteration);
                }
            }
            System.out.println("$$ Putting unique words of player " + player);
            playerScoreMap.put(player, countPlayerRoundScore(uniqueWordsOfPlayer));
        }

        System.out.println("$$  Getting the highest score");
        //Get the highest score
        int highestScore = 0;
        int tempHighestScore = 0;
        for (int playerID: playerScoreMap.keySet()){
            if (playerScoreMap.get(playerID)>0){
                tempHighestScore = playerScoreMap.get(playerID);
            }
            if(tempHighestScore>highestScore){
                highestScore = tempHighestScore;
            }
        }

        //Check if highest score is a tie
        int numOfHSHolder = 0;
        for (int player: playerScoreMap.keySet()){
            if (highestScore==playerScoreMap.get(player)){
                numOfHSHolder++;
            }
        }
        System.out.println("Highest score is "+ highestScore);

        if (highestScore>0){
            if (numOfHSHolder>=2){
                System.out.println("There are no highest score holders");
                HashMap<Integer, Integer> roundWinnerMap = new HashMap<>();
                roundWinnerMap.put(roundID, 0);
                int gameID = 0;
                for (Game g: onGoingGames){
                    for (int round: g.roundList){
                        if (round == roundID) {
                            gameID = g.gameId;
                            break;
                        }
                    }
                }
                if (gameRoundWinnerMap.get(gameID)!=null){
                    if (!(gameRoundWinnerMap.get(gameID).containsKey(roundID))){
                        gameRoundWinnerMap.put(gameID, roundWinnerMap);
                        GameQueries.recordRoundWinner(roundID, 0);
                        System.out.println("Setting round winner in database to 0");
                    }
                }else {
                    gameRoundWinnerMap.put(gameID, roundWinnerMap);
                    System.out.println("Setting round winner in database to " + 0);
                    GameQueries.recordRoundWinner(roundID, 0);
                }

                return "none";
            }else {
                for (int p: playerScoreMap.keySet()){
                    if (highestScore == playerScoreMap.get(p)){
                        HashMap<Integer, Integer> roundWinnerMap = new HashMap<>();
                        roundWinnerMap.put(roundID, p);
                        int gameID = playerGameMap.get(p);
                        String username =  PlayerQueries.getPlayerUsername(p);
                        System.out.println("Highest score holder is " + username);

                        if (gameRoundWinnerMap.get(gameID)!=null) {
                            if (!(gameRoundWinnerMap.get(gameID).containsKey(roundID))) {
                                gameRoundWinnerMap.put(gameID, roundWinnerMap);
                                System.out.println("Setting round winner in database to " + p);
                                GameQueries.recordRoundWinner(roundID, p);
                            }
                        }else {
                            gameRoundWinnerMap.put(gameID, roundWinnerMap);
                            System.out.println("Setting round winner in database to " + p);
                            GameQueries.recordRoundWinner(roundID, p);
                        }
                        return username;
                    }
                }
            }
        }
        return "none";
    }
    private int countPlayerRoundScore(List<String> words){
        int score = 0;
        if (words.isEmpty()){
            return 0;
        }

        for (String s : words){
            score = score + s.length();
        }
        return score;
    }
    private List<String> compareWords(Map<Integer, List<String>> playerWordListMap,  List<String> playerWords, int playerComparatorID){
        List<String> playerComparator = playerWordListMap.get(playerComparatorID);

        List<String> uniqueWords = new ArrayList<>();
        boolean isUnique ;
        for (String s: playerWords){
            isUnique = true;
            for (String sCom: playerComparator){
                if (s.equalsIgnoreCase(sCom)){
                    isUnique = false;
                    break;
                }
            }
            if (isUnique){
                uniqueWords.add(s);
            }
        }
        return uniqueWords;
    }

    @Override
    public String getGameWinner(int gameID) {
        String winner = PlayerQueries.getGameWinner(gameID);
        if (winner!=null){
            return winner;
        }
        return "none";
    }



    public static void countPlayerScore(String word, int playerID) {
        int score = playerScores.getOrDefault(playerID, 0);
        score += word.length();
        playerScores.put(playerID, score);
    }
}


public class BoggleServer {
    public static void main(String[] args) {
        try {
            String[] args2 = {"-ORBInitialPort", "5151", "-ORBInitialHost", "10.135.135.194"};
            ORB orb = ORB.init(args2, null);

            System.out.println(orb);
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            PlayerImplementation helloImpl = new PlayerImplementation();

            Object ref = rootpoa.servant_to_reference(helloImpl);
            PlayerServant href = PlayerServantHelper.narrow(ref);

            Object objRef = orb.resolve_initial_references("NameService");

            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            NameComponent[] path = ncRef.to_name("Players");
            ncRef.rebind(path, href);
            System.out.println("Setting Database...");
            DbConnect.setConnection();
            System.out.println("HelloServer ready and waiting ...");

            orb.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("HelloServer Exiting...");
    }
}
