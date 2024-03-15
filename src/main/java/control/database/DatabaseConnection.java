package control.database;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import model.Player;

import java.util.HashMap;

public class DatabaseConnection {
    private static final Firestore database = FirestoreClient.getFirestore();

    public static String UID_PLAYER_FIELD = "uid";
    public static String PSEUDO_PLAYER_FIELD = "pseudo";

    public static String GAME_ID_FIELD = "game-id";

    public static String PLAYER_CASH_FIELD = "cash";
    public static String PLAYER_NET_FIELD = "net";

    public static String PLAYER_TABLE_NAME = "players";

    public static void addPlayer(String gameId, Player player) throws Exception {
        HashMap<String, Object> newPlayer = new HashMap<>();
        newPlayer.put(PSEUDO_PLAYER_FIELD, player.getPseudo());
        newPlayer.put(UID_PLAYER_FIELD, player.getUID());
        newPlayer.put(GAME_ID_FIELD, gameId);
        newPlayer.put(PLAYER_CASH_FIELD, player.getCash());
        newPlayer.put(PLAYER_NET_FIELD,player.getNet());
        DocumentReference docRef = database.collection(PLAYER_TABLE_NAME).document();
        ApiFuture<WriteResult> future = docRef.set(newPlayer);
        future.get();
    }

    public static void removePlayer(Player player) throws Exception{

    }

}
