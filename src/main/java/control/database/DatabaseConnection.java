package control.database;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
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

    public static String GAME_TABLE_NAME = "games";

    public static String GAME_MAX_PLAYERS_FIELD = "max-players";

    public static String GAME_STATE_FIELD = "state";

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
        CollectionReference collection = database.collection(PLAYER_TABLE_NAME);
        ApiFuture<QuerySnapshot> future = collection.whereEqualTo(UID_PLAYER_FIELD, player.getUID()).get();
        QuerySnapshot snapshot = future.get();

        for (QueryDocumentSnapshot doc : snapshot) {
            ApiFuture<WriteResult> deleteFuture = doc.getReference().delete();
            deleteFuture.get();
        }
    }

    public static void createGame(int maxPlayers) throws Exception{
        DocumentReference doc = database.collection(GAME_TABLE_NAME).document();
        String gameId = doc.getId();
        HashMap<String,Object> newGame = new HashMap<>();
        newGame.put(GAME_ID_FIELD,gameId);
        newGame.put(GAME_STATE_FIELD,0);
        newGame.put(GAME_MAX_PLAYERS_FIELD,maxPlayers);
        ApiFuture<WriteResult> future = doc.set(newGame);
        future.get();
    }

    public static void removeGame(String gameId){

    }

}
