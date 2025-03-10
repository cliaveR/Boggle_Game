import sys
from omniORB import CORBA
import CosNaming
import BoggleApp
from GameClient import GameClient

def main():
    # Initialize the ORB
    orb = CORBA.ORB_init(sys.argv, CORBA.ORB_ID)

    # Obtain a reference to the root naming context
    try:
        objRef = orb.resolve_initial_references("NameService")
        rootContext = objRef._narrow(CosNaming.NamingContext)
        if rootContext is None:
            raise Exception("Failed to find the root naming context")
    except Exception as e:
        print(e)
        sys.exit(1)

    # Resolve the object reference in the naming context
    name = [CosNaming.NameComponent("Players", "")]
    try:
        objRef = rootContext.resolve(name)
    except CosNaming.NamingContext.NotFound:
        print("No Players found")
        sys.exit(1)

    # Narrow the object to the Boggle PlayerServant
    stub = objRef._narrow(BoggleApp.PlayerServant)

    # Create an instance of GameClient and pass the stub
    game_client = GameClient(stub)
    game_client.run()

if __name__ == "__main__":
    main()