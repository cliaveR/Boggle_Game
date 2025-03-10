import time
from threading import Thread
import GameClient
import InGameLobby  # Assuming you have the InGameController class defined

class LobbyController:
    def __init__(self):
        self.playerServant = None
        self.playerID = None
        self.waitingTime = 0
        self.gameId = 0
        self.waitingLoop = True
        self.game_waiting = 5

    def initialize(self, stub, pId):
        self.playerServant = stub
        self.playerID = pId
        self.displayLeaderboard()

    def startGameButton(self):
        gameStartThread = Thread(target=self.start_game)
        gameStartThread.start()

        waitThread = Thread(target=self.wait_for_game)
        waitThread.start()

        gameCheckThread = Thread(target=self.check_game_result, args=(gameStartThread,))
        gameCheckThread.start()

    def start_game(self):
        self.gameId = self.playerServant.startGame(self.playerID)
        print("Game ID:", self.gameId)

        if self.gameId > 0:
            self.wait_game_5_seconds()
            in_game_controller = InGameLobby.InGameController()
            in_game_controller.initialize(self.playerServant, self.playerID, self.gameId)

    def wait_for_game(self):
        while self.waitingLoop:
            time.sleep(1)  # Fetch waiting time every second
            self.waitingTime = self.playerServant.getWaitingTime()
            print("Waiting time: T-", self.waitingTime)

            if self.waitingTime < 1:
                self.waitingLoop = False  # Stop the waiting loop  # Start the countdown of 5 seconds

    def wait_game_5_seconds(self):
        for i in range(self.game_waiting, 0, -1):
            time.sleep(1)
            print(f"Starting game in: {i} seconds")

        # Start the game after the countdown finishes

    def check_game_result(self, gameStartThread):
        try:
            gameStartThread.join()  # Wait for server communication to finish
            if self.gameId > 0:
                print("Game received:", self.gameId)
            else:
                print("Failed to start game:", self.gameId)
                self.show_menu()
        except Exception as e:
            print(e)

    def displayLeaderboard(self):
        leaderboardDetails = self.playerServant.getLeaderBoard()
        for p in leaderboardDetails:
            print(f"{p.username}\t\t\t\t\t{p.points}")

    def show_menu(self):
        while True:
            print("\nMenu:")
            print("1. Start Game")
            print("2. Go Back to GameClient")

            choice = input("Enter your choice: ")

            if choice == "1":
                self.startGameButton()
                break
            elif choice == "2":
                client = GameClient.GameClient(self.playerServant)
                client.run()
            else:
                print("Invalid choice. Please try again.")

# Example usage
# lobby_controller = LobbyController()
# lobby_controller.initialize(stub, player_id)
# lobby_controller.show_menu()
