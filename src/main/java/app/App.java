package app;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import control.auth.FirebaseClient;
import control.database.DatabaseConnection;
import control.network.*;
import model.Player;

import javax.imageio.IIOException;

public class App {
    public static void main(String[] args) throws Exception {
        /*System.out.println("Hello bitches");
        Server.main(args);
        Client.main(args);*/
        FirebaseClient.initialize();



        Player player = new Player();
        player.setCash(34);
        player.setNet(4564);
        player.setPseudo("ARthir");
        player.setUID("gtfcdtrdxfdr");
        DatabaseConnection.addPlayer("nouveau joueur ATHURRRRR",player);
        DatabaseConnection.createGame(26);
        DatabaseConnection.removeGame("hUdqrlxIUbDP0IBI45Oa");
        DatabaseConnection.removePlayer(player);




    }
}
