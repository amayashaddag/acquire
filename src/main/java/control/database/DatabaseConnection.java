package control.database;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import model.game.Corporation;
import model.game.Player;
import model.game.Board;
import model.game.Cell;
import model.tools.PlayerAnalytics;
import model.tools.Point;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseConnection {
    private static final Firestore database = FirestoreClient.getFirestore();

    public static String UID_PLAYER_FIELD = "uid";
    public static String PSEUDO_PLAYER_FIELD = "pseudo";

    public static String GAME_ID_FIELD = "game-id";

    public static String STOCKS_TABLE_NAME = "stocks";

    public static String PLAYER_CASH_FIELD = "cash";
    public static String PLAYER_NET_FIELD = "net";

    public static String PLAYER_TABLE_NAME = "players";

    public static String GAME_TABLE_NAME = "games";

    public static String GAME_MAX_PLAYERS_FIELD = "max-players";

    public static String GAME_STATE_FIELD = "state";

    public static String CORPORATION_FIELD = "corporation";


    public static String PLACED_CELLS_TABLE_NAME = "placed-cells";

    public static String X_POSITION_FIELD = "x-position";
    public static String Y_POSITION_FIELD = "y-position";

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

    public static void removeGame(String gameId) throws Exception{
        CollectionReference gameTable = database.collection(GAME_TABLE_NAME);
        ApiFuture<QuerySnapshot> future = gameTable.whereEqualTo(GAME_ID_FIELD,gameId).get();
        QuerySnapshot gameToRemove = future.get();
        for (QueryDocumentSnapshot doc : gameToRemove) {
            ApiFuture<WriteResult> deleteFuture = doc.getReference().delete();
            deleteFuture.get();
        }
    }
    public static void updateCash(int newCash,Player player,String gameId) throws Exception{
        update(newCash, player, gameId, PLAYER_CASH_FIELD);

    }

    public static void updateNet(int newNet,Player player,String gameId) throws Exception {
        update(newNet, player, gameId, PLAYER_NET_FIELD);
    }

    private static void update(int newNet, Player player, String gameId, String playerNetField) throws InterruptedException, java.util.concurrent.ExecutionException {
        CollectionReference collection = database.collection(PLAYER_TABLE_NAME);
        ApiFuture<QuerySnapshot> future = collection
                .whereEqualTo(GAME_ID_FIELD, gameId)
                .whereEqualTo(UID_PLAYER_FIELD, player.getUID()).get();
        QuerySnapshot snapshot = future.get();
        for (QueryDocumentSnapshot doc : snapshot) {
            WriteResult docToUpdate = doc.getReference().update(playerNetField, newNet).get();
        }
    }

    public static Map<model.game.Corporation, Point> getNewPlacedCells(String gameId, Board currentBoard) throws Exception {
        Map<Corporation, Point> newPlacedCells = new HashMap<>();
        ApiFuture<QuerySnapshot> future = database.collection(PLACED_CELLS_TABLE_NAME)
                .whereEqualTo(GAME_ID_FIELD, gameId)
                .get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documents) {
            String corp = (String) doc.get(CORPORATION_FIELD);
            model.game.Corporation c = null;
            if (corp != null && !corp.isEmpty()) {
                c = Corporation.getCorporationFromName(corp);
            }
            Long x = (Long) doc.get(X_POSITION_FIELD);
            Long y = (Long) doc.get(Y_POSITION_FIELD);

            if (x == null || y == null) {
                throw new Exception();
            }

            Point cellPosition = new Point(x.intValue(), y.intValue());
            Cell oldPlacedCell = currentBoard.getCell(cellPosition);

            if (oldPlacedCell.getCorporation() == null || !oldPlacedCell.getCorporation().equals(c)) {
                newPlacedCells.put(c, cellPosition);
            }
        }

        return newPlacedCells;
    }

    public static void setStocks(Player player, String gameId) throws Exception{
            CollectionReference collection = database.collection(STOCKS_TABLE_NAME);
            ApiFuture<QuerySnapshot> future = collection
                    .whereEqualTo(GAME_ID_FIELD,gameId)
                    .whereEqualTo(UID_PLAYER_FIELD,player.getUID())
                    .get();
            QuerySnapshot snapshot = future.get();
            for (QueryDocumentSnapshot doc : snapshot) {
                for (Map.Entry<Corporation,Integer> mapentry : player.getEarnedStocks().entrySet()){
                    //WriteResult docToUpdate = doc.getReference().
                }
            }
    }

    public static PlayerAnalytics getPlayerAnalytics(String uid) {
        // TODO : Should implement this one
        return null;
    }

    public static List<PlayerAnalytics> getRanking() {
        // TODO : Implement
        return null;
    }

    public static Map<String, Integer> getAvailableGames() {
        // TODO : Implement
        return null;
    }
}
