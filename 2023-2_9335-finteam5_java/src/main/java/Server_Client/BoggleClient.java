package Server_Client;

import BoggleApp.PlayerServant;
import BoggleApp.PlayerServantHelper;
import Runner.LoginControllerRunner;
import javafx.stage.Stage;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

public class BoggleClient
{
    public static PlayerServant stub;
    public static void main(String[] args) {
        try {
            // create and initialize the ORB
            String[] args2 = {"-ORBInitialPort", "5151", "-ORBInitialHost", "10.135.135.194"};

            ORB orb = ORB.init(args2, null);
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            stub = PlayerServantHelper.narrow(ncRef.resolve_str("Players"));
            LoginControllerRunner.setStub(stub);

            new Thread(() -> LoginControllerRunner.launch(LoginControllerRunner.class)).start();
            System.out.println("Running Client");

        } catch (InvalidName | NotFound | org.omg.CosNaming.NamingContextPackage.InvalidName | CannotProceed invalidName) {
            invalidName.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
