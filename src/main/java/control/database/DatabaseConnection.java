package control.database;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import io.opencensus.metrics.LongCumulative;
import model.game.Corporation;
import model.game.Player;
import model.game.Board;
import model.game.Cell;
import model.tools.Point;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

public class DatabaseConnection {
    private static final Firestore database = FirestoreClient.getFirestore();

    private static final String UID_PLAYER_FIELD = "uid";
    private static final String PSEUDO_PLAYER_FIELD = "pseudo";
    private static final String GAME_ID_FIELD = "game-id";
    private static final String STOCKS_TABLE_NAME = "stocks";
    private static final String PLAYER_CASH_FIELD = "cash";
    private static final String PLAYER_NET_FIELD = "net";
    private static final String PLAYER_TABLE_NAME = "players";
    private static final String GAME_TABLE_NAME = "games";
    private static final String GAME_MAX_PLAYERS_FIELD = "max-players";
    private static final String GAME_STATE_FIELD = "state";
    private static final String CORPORATION_FIELD = "corporation";
    private static final String PLACED_CELLS_TABLE_NAME = "placed-cells";
    private static final String X_POSITION_FIELD = "x-position";
    private static final String Y_POSITION_FIELD = "y-position";
    private static final String CURRENT_PLAYER_TABLE = "current-player";
    private static final String UID_FIELD = "uid";
    private static final String CASH_FIELD = "cash";
    private static final String NET_FIELD = "net";

    public static void addPlayer(String gameId, Player player) throws Exception {
        HashMap<String, Object> newPlayer = new HashMap<>();
        newPlayer.put(PSEUDO_PLAYER_FIELD, player.getPseudo());
        newPlayer.put(UID_PLAYER_FIELD, player.getUID());
        newPlayer.put(GAME_ID_FIELD, gameId);
        newPlayer.put(PLAYER_CASH_FIELD, player.getCash());
        newPlayer.put(PLAYER_NET_FIELD, player.getNet());
        DocumentReference docRef = database.collection(PLAYER_TABLE_NAME).document();
        ApiFuture<WriteResult> future = docRef.set(newPlayer);
        future.get();
    }

    public static void removePlayer(Player player) throws Exception {
        CollectionReference collection = database.collection(PLAYER_TABLE_NAME);
        ApiFuture<QuerySnapshot> future = collection.whereEqualTo(UID_PLAYER_FIELD, player.getUID()).get();
        QuerySnapshot snapshot = future.get();

        for (QueryDocumentSnapshot doc : snapshot) {
            ApiFuture<WriteResult> deleteFuture = doc.getReference().delete();
            deleteFuture.get();
        }
    }

    public static String createGame(int maxPlayers) throws Exception {
        DocumentReference doc = database.collection(GAME_TABLE_NAME).document();
        String gameId = doc.getId();
        HashMap<String, Object> newGame = new HashMap<>();
        newGame.put(GAME_ID_FIELD, gameId);
        newGame.put(GAME_STATE_FIELD, 0);
        newGame.put(GAME_MAX_PLAYERS_FIELD, maxPlayers);
        ApiFuture<WriteResult> future = doc.set(newGame);
        future.get();
        return gameId;
    }

    public static void removeGame(String gameId) throws Exception {
        CollectionReference gameTable = database.collection(GAME_TABLE_NAME);
        ApiFuture<QuerySnapshot> future = gameTable.whereEqualTo(GAME_ID_FIELD, gameId).get();
        QuerySnapshot gameToRemove = future.get();
        for (QueryDocumentSnapshot doc : gameToRemove) {
            ApiFuture<WriteResult> deleteFuture = doc.getReference().delete();
            deleteFuture.get();
        }
    }

    public static void setCash(int newCash, Player player, String gameId) throws Exception {
        set(newCash, player, gameId, PLAYER_CASH_FIELD);

    }

    public static void setNet(int newNet, Player player, String gameId) throws Exception {
        set(newNet, player, gameId, PLAYER_NET_FIELD);
    }

    private static void set(int newNet, Player player, String gameId, @Nonnull String playerNetField)
            throws InterruptedException, java.util.concurrent.ExecutionException {
        CollectionReference collection = database.collection(PLAYER_TABLE_NAME);
        ApiFuture<QuerySnapshot> future = collection
                .whereEqualTo(GAME_ID_FIELD, gameId)
                .whereEqualTo(UID_PLAYER_FIELD, player.getUID()).get();
        QuerySnapshot snapshot = future.get();
        for (QueryDocumentSnapshot doc : snapshot) {
            doc.getReference().update(playerNetField, newNet).get();
        }
    }

    public static Map<Point, Corporation> getNewPlacedCells(String gameId, Board currentBoard) throws Exception {
        Map<Point, Corporation> newPlacedCells = new HashMap<>();
        ApiFuture<QuerySnapshot> future = database.collection(PLACED_CELLS_TABLE_NAME)
                .whereEqualTo(GAME_ID_FIELD, gameId)
                .get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documents) {
            String corp = (String) doc.get(CORPORATION_FIELD);
            model.game.Corporation c = null;
            if (corp != null) {
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
                newPlacedCells.put(cellPosition, c);
            }
        }

        return newPlacedCells;
    }

    public static void setStocks(Player player, String gameId) throws Exception {
        CollectionReference collection = database.collection(STOCKS_TABLE_NAME);
        ApiFuture<QuerySnapshot> future = collection
                .whereEqualTo(GAME_ID_FIELD, gameId)
                .whereEqualTo(UID_PLAYER_FIELD, player.getUID())
                .get();
        QuerySnapshot snapshot = future.get();
        for (QueryDocumentSnapshot doc : snapshot) {
            for (Map.Entry<Corporation, Integer> mapEntry : player.getEarnedStocks().entrySet()) {
                // WriteResult docToUpdate = doc.getReference().
            }
        }
    }

    public static String getCurrentPlayer(String gameId) throws Exception {
        ApiFuture<QuerySnapshot> future = database.collection(CURRENT_PLAYER_TABLE)
                .whereEqualTo(GAME_ID_FIELD, gameId)
                .get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        if (documents.isEmpty()) {
            throw new Exception();
        }

        QueryDocumentSnapshot currentPlayerDoc = documents.get(0);
        String uid = (String) currentPlayerDoc.get(UID_FIELD);

        if (uid == null) {
            throw new Exception();
        }

        return uid;
    }

    public static void setCurrentPlayer(String gameId, String uid) throws Exception {
        ApiFuture<QuerySnapshot> future = database.collection(CURRENT_PLAYER_TABLE)
                .whereEqualTo(GAME_ID_FIELD, gameId).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        if (!documents.isEmpty()) {
            QueryDocumentSnapshot doc = documents.get(0);
            doc.getReference().update(UID_FIELD, uid).get();
        } else {
            createNewCurrentPlayer(gameId, uid);
        }
    }

    private static void createNewCurrentPlayer(String gameId, String uid) throws Exception {
        DocumentReference doc = database.collection(CURRENT_PLAYER_TABLE).document();
        Map<String, Object> newCurrentPlayer = new HashMap<>();
        newCurrentPlayer.put(GAME_ID_FIELD, gameId);
        newCurrentPlayer.put(UID_FIELD, uid);
        ApiFuture<WriteResult> future = doc.set(newCurrentPlayer);
        future.get();
    }

    public static void setNewPlacedCells(Map<Point, Corporation> newPlacedCells, String gameId)
            throws Exception {
        for (Map.Entry<Point, Corporation> cell : newPlacedCells.entrySet()) {
            Point cellPosition = cell.getKey();
            Corporation cellCorporation = cell.getValue();
            Integer x = cellPosition.getX(), y = cellPosition.getY();
            DocumentReference doc = database.collection(PLACED_CELLS_TABLE_NAME).document();
            Map<String, Object> newPlacedCell = new HashMap<>();
            newPlacedCell.put(CORPORATION_FIELD, cellCorporation == null ? null : cellCorporation.toString());
            newPlacedCell.put(GAME_ID_FIELD, gameId);
            newPlacedCell.put(X_POSITION_FIELD, x);
            newPlacedCell.put(Y_POSITION_FIELD, y);
            ApiFuture<WriteResult> future = doc.set(newPlacedCell);
            future.get();
        }
    }

    public static Map<String, int[]> getPlayersCashNet(String gameId) throws Exception {
        Map<String, int[]> playersCashNet = new HashMap<>();
        ApiFuture<QuerySnapshot> future = database.collection(PLAYER_TABLE_NAME)
                .whereEqualTo(GAME_ID_FIELD, gameId).get();

        List<QueryDocumentSnapshot> players = future.get().getDocuments();
        for (QueryDocumentSnapshot p : players) {
            String uid = (String) p.get(UID_FIELD);
            Long cash = (Long) p.get(CASH_FIELD);
            Long net = (Long) p.get(NET_FIELD);

            if (cash == null || net == null) {
                throw new NullPointerException();
            }

            int[] income = {cash.intValue(), net.intValue()};

            playersCashNet.put(uid, income);
        }

        return playersCashNet;
    }
}