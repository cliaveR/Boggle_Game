# GameClient.py
import ClientTools
import GameLobby

class GameClient:
    def __init__(self, player_servant):
        self.stub = player_servant

    def run(self):
        run = False
        while not run:
            clientTitle = ClientTools.TitleScreen()
            print(clientTitle.printTitle())

            print("Please enter your username and password to login")
            nameResult = input("Username: ")
            passwordResult = input("Password: ")

            result_pId = self.stub.logInToGame(nameResult.strip(), passwordResult.strip())
            if result_pId >= 1:
                game_lobby = GameLobby.LobbyController()
                game_lobby.initialize(self.stub,result_pId)
                game_lobby.show_menu()
                run = True
            else:
                print("No account found, please try again.")
