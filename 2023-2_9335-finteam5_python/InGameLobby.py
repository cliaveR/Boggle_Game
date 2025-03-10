import time
import threading
import GameLobby
import keyboard

class InGameController:
    def __init__(self):
        self.playerServant = None
        self.playerID = None
        self.gameID = None
        self.playerScore = 0
        self.wordListUser = []
        self.runningRoundTime = 30  # Assuming each round lasts for 30 seconds
        self.roundOnGoing = True
        self.roundID = None
        self.roundWinner = None
        self.gameWinner = None
        self.currentRound = 1
        self.nameWins = {}
        self.listOfPlayers = []  # List to store all players
        self.roundPlayers = []  # List to store players in the current round

    def initialize(self, player_servant, pID, gameId):
        self.playerServant = player_servant
        self.playerID = pID
        self.gameID = gameId

        # Add the player to the list of players
        self.add_player(pID)

        if self.gameWinner is None:
            self.start_game()

    def add_player(self, player_id):
        if player_id not in self.listOfPlayers:
            self.listOfPlayers.append(player_id)
            print(f"Player {player_id} added to listOfPlayers.")

    def start_game(self):
        while self.currentRound <= 3:
            self.start_round(self.gameID, self.currentRound)
            self.currentRound += 1

        self.end_game()

    def start_round(self, gameId, currentRound):
        self.roundOnGoing = True
        self.runningRoundTime = self.playerServant.getRoundTime(gameId)  # Reset the round time for each round
        self.wordListUser = []  # Reset word list for each round
        self.roundID = self.playerServant.startRound(gameId, currentRound)
        print("Round ID:", self.roundID)

        letters = self.playerServant.getLetterSet(gameId, self.roundID)
        print("Letters:", letters)
        print("The countdown is shown every 10 seconds")

        if not letters or len(letters) < 20:
            raise Exception("Failed to retrieve letters for the grid.")

        self.display_letters(letters)

        # Add the current player to roundPlayers
        self.roundPlayers.append(self.playerID)

        # Start countdown and round game threads with round-specific time
        count_down_thread = threading.Thread(target=self.count_down_thread_function)
        round_game_thread = threading.Thread(target=self.round_game_thread_function)

        count_down_thread.start()
        round_game_thread.start()

        count_down_thread.join()
        round_game_thread.join()

        print("Time's up! Ending round...")
        self.roundOnGoing = False
        print(f"Getting the round ID winner: {self.roundID}")
        self.roundWinner = self.playerServant.getRoundWinner(self.roundID)
        print(self.roundWinner)
        if self.roundWinner.lower() == "none":
            print("There are no winners in the previous round")
        else:
            print("Winner of the previous round is", self.roundWinner)

        # Check if all players have played this round
        if set(self.listOfPlayers) == set(self.roundPlayers):
            print("All players have played this round.")
            self.roundPlayers = []  # Reset roundPlayers for the next round
        else:
            print("Not all players have played this round yet. Continuing the round...")

    def display_letters(self, letters):
        print("Displaying letters on the grid:")
        for row in range(5):
            for col in range(4):
                print(letters[row * 4 + col], end=" ")
            print()

    def count_down_thread_function(self):
        while self.runningRoundTime > 0 and self.roundOnGoing:
            time.sleep(1)
            self.runningRoundTime -= 1
            if self.runningRoundTime % 10 == 0:
                print("Remaining round time:", self.runningRoundTime)
            if self.runningRoundTime <= 0:
                self.roundOnGoing = False
                print("Finished the round time")

    def round_game_thread_function(self):
        while self.runningRoundTime > 0 and self.roundOnGoing:
            if not self.roundOnGoing:
                break
            if keyboard.is_pressed('enter'):  # Check if the Enter key is being held down
                user_input = input()  # Get the input
                user_input = user_input.strip()  # Clean and process the input
                print(user_input)  # Print the input for testing
                self.wordListUser.append(user_input)  # Add the input to the word list
                success = self.playerServant.checkWord(user_input, self.playerID, self.roundID)
                if success:
                    print(f"\nWord '{user_input}' added successfully.")
                else:
                    print(f"\nWord '{user_input}' is invalid.")
                time.sleep(0.1)  # Sleep briefly to avoid capturing multiple inputs
                continue  # Continue to allow for new input
            time.sleep(0.1)  # Sleep to avoid high CPU usage

    def end_game(self):
        self.gameWinner = self.playerServant.getGameWinner(self.gameID)
        print("Game winner:", self.gameWinner)
        if self.gameWinner.lower() == "none":
            print("There are no overall game winners.")
        else:
            print(self.gameWinner, "has won the game.")

        self.return_to_lobby()

    def return_to_lobby(self):
        wait = 5
        while wait > 0:
            time.sleep(1)
            print("Returning to lobby...", wait)
            wait -= 1
        game_lobby = GameLobby.LobbyController()
        game_lobby.initialize(self.playerServant, self.playerID)
        game_lobby.show_menu()
