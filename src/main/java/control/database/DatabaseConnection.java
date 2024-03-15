package control.database;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import model.Player;

public class DatabaseConnection {
    private static final Firestore database = FirestoreClient.getFirestore();

    public void addPlayer(String gameId, Player player){

    }


}
