package control.database;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteBatch;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

import control.game.GameController;
import model.game.Board;
import model.game.Cell;
import model.game.Corporation;
import model.game.Player;
import model.tools.Point;

public class GameDatabaseConnection {
    private static final Firestore database = FirestoreClient.getFirestore();

    private static final String UID_PLAYER_FIELD = "uid";
    private static final String PSEUDO_PLAYER_FIELD = "pseudo";
    private static final String GAME_ID_FIELD = "game-id";
    private static final String GAME_STATE_FIELD = "state";
    private static final String STOCKS_AMOUNT_FIELD = "amount";
    private static final String PLAYER_CASH_FIELD = "cash";
    private static final String PLAYER_NET_FIELD = "net";
    private static final String GAME_MAX_PLAYERS_FIELD = "max-players";
    private static final String GAME_CREATOR_FIELD = "creator";
    private static final String CORPORATION_FIELD = "corporation";
    private static final String X_POSITION_FIELD = "x-position";
    private static final String Y_POSITION_FIELD = "y-position";
    private static final String UID_FIELD = "uid";
    private static final String CASH_FIELD = "cash";
    private static final String NET_FIELD = "net";
    private static final String NOTIFICATION_MESSAGE_FIELD = "message";
    private static final String NOTIFICATION_TIME_FIELD = "time";

    private static final String STOCKS_TABLE_NAME = "stocks";
    private static final String PLAYER_TABLE_NAME = "players";
    private static final String GAME_TABLE_NAME = "games";
    private static final String PLACED_CELLS_TABLE_NAME = "placed-cells";
    private static final String CURRENT_PLAYER_TABLE = "current-player";
    private static final String NOTIFICATIONS_TABLE = "notifications";

    private static final List<String> ALL_TABLES = new LinkedList<>();
    static {
        Collections.addAll(ALL_TABLES,
                STOCKS_TABLE_NAME,
                PLAYER_TABLE_NAME,
                GAME_TABLE_NAME,
                PLACED_CELLS_TABLE_NAME,
                CURRENT_PLAYER_TABLE,
                NOTIFICATIONS_TABLE);
    }

    public static void addPlayer(String gameId, Player player) throws Exception {
        ApiFuture<QuerySnapshot> reader = database.collection(PLAYER_TABLE_NAME)
                .whereEqualTo(UID_FIELD, player.getUID()).get();
        List<QueryDocumentSnapshot> docs = reader.get().getDocuments();
        if (!docs.isEmpty()) {
            throw new Exception();
        }

        HashMap<String, Object> newPlayer = new HashMap<>();

        newPlayer.put(PSEUDO_PLAYER_FIELD, player.getPseudo());
        newPlayer.put(UID_PLAYER_FIELD, player.getUID());
        newPlayer.put(GAME_ID_FIELD, gameId);
        newPlayer.put(PLAYER_CASH_FIELD, player.getCash());
        newPlayer.put(PLAYER_NET_FIELD, player.getNet());

        DocumentReference docRef = database.collection(PLAYER_TABLE_NAME).document();
        ApiFuture<WriteResult> future = docRef.set(newPlayer);
        future.get();

        initStocks(gameId, player);
    }

    private static void initStocks(String gameId, Player player) throws Exception {
        for (Corporation c : Corporation.values()) {
            DocumentReference doc = database.collection(STOCKS_TABLE_NAME).document();
            Map<String, Object> stocks = new HashMap<>();
            stocks.put(CORPORATION_FIELD, c.toString());
            stocks.put(STOCKS_AMOUNT_FIELD, 0);
            stocks.put(UID_FIELD, player.getUID());

            ApiFuture<WriteResult> writer = doc.set(stocks);
            writer.get();
        } 
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

    public static String createGame(Player creator, int maxPlayers) throws Exception {
        DocumentReference doc = database.collection(GAME_TABLE_NAME).document();
        String gameId = doc.getId();
        HashMap<String, Object> newGame = new HashMap<>();
        newGame.put(GAME_ID_FIELD, gameId);
        newGame.put(GAME_STATE_FIELD, 0);
        newGame.put(GAME_MAX_PLAYERS_FIELD, maxPlayers);
        newGame.put(GAME_CREATOR_FIELD, creator.getPseudo());
        ApiFuture<WriteResult> future = doc.set(newGame);
        future.get();
        return gameId;
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


    // TODO : Should reimplement this function
    
    public static void setStocks(Player player, String gameId) throws Exception {
        CollectionReference collection = database.collection(STOCKS_TABLE_NAME);
        ApiFuture<QuerySnapshot> future = collection
                .whereEqualTo(GAME_ID_FIELD, gameId)
                .whereEqualTo(UID_PLAYER_FIELD, player.getUID())
                .get();
        QuerySnapshot snapshot = future.get();
        HashMap<Corporation, Integer> newStocks = player.getEarnedStocks();
        for (QueryDocumentSnapshot doc : snapshot) {
            DocumentReference docToUpdate = doc.getReference();
            ApiFuture<DocumentSnapshot> future2 = docToUpdate.get();
            DocumentSnapshot data = future2.get();
            String corp = data.getString(CORPORATION_FIELD);
            ApiFuture<WriteResult> writer = docToUpdate.update(STOCKS_AMOUNT_FIELD, newStocks.get(Corporation.getCorporationFromName(corp)));
            writer.get();
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
        ApiFuture<WriteResult> future;
        for (Map.Entry<Point, Corporation> cell : newPlacedCells.entrySet()) {

            ApiFuture<QuerySnapshot> existingDocumentsRequest = database.collection(PLACED_CELLS_TABLE_NAME)
                    .whereEqualTo(GAME_ID_FIELD, gameId)
                    .whereEqualTo(X_POSITION_FIELD, cell.getKey().getX())
                    .whereEqualTo(Y_POSITION_FIELD, cell.getKey().getY())
                    .get();
            List<QueryDocumentSnapshot> existingDocuments = existingDocumentsRequest.get().getDocuments();

            Point cellPosition = cell.getKey();
            Corporation cellCorporation = cell.getValue();
            Integer x = cellPosition.getX(), y = cellPosition.getY();
            Map<String, Object> newPlacedCell = new HashMap<>();

            newPlacedCell.put(CORPORATION_FIELD, cellCorporation == null ? null : cellCorporation.toString());
            newPlacedCell.put(GAME_ID_FIELD, gameId);
            newPlacedCell.put(X_POSITION_FIELD, x);
            newPlacedCell.put(Y_POSITION_FIELD, y);

            if (existingDocuments.isEmpty()) {
                DocumentReference doc = database.collection(PLACED_CELLS_TABLE_NAME).document();

                future = doc.set(newPlacedCell);
                future.get();
            } else {
                QueryDocumentSnapshot existingDocument = existingDocuments.get(0);
                future = existingDocument.getReference().set(newPlacedCell);
            }
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

            int[] income = { cash.intValue(), net.intValue() };

            playersCashNet.put(uid, income);
        }

        return playersCashNet;
    }

    public static boolean isGameEnded(String gameId) throws Exception {
        ApiFuture<QuerySnapshot> future = database.collection(GAME_TABLE_NAME)
                .whereEqualTo(GAME_ID_FIELD, gameId).get();
        List<QueryDocumentSnapshot> docs = future.get().getDocuments();

        if (docs.isEmpty()) {
            return true;
        }

        return false;
    }

    public static void setGameState(@Nonnull String gameId, int gameState) throws Exception {
        ApiFuture<QuerySnapshot> reader = database.collection(GAME_TABLE_NAME)
                .whereEqualTo(GAME_ID_FIELD, gameId).get();
        List<QueryDocumentSnapshot> docs = reader.get().getDocuments();

        if (docs.isEmpty()) {
            throw new Exception();
        }

        DocumentSnapshot currentGame = docs.get(0);
        currentGame.getReference().update(GAME_STATE_FIELD, gameState);

    }

    public static void removeGame(String gameId) throws Exception {
        for (String table : ALL_TABLES) {
            if (table == null) {
                continue;
            }

            CollectionReference collection = database.collection(table);
            ApiFuture<QuerySnapshot> reader = collection.whereEqualTo(GAME_ID_FIELD, gameId).get();
            List<QueryDocumentSnapshot> docs = reader.get().getDocuments();

            for (QueryDocumentSnapshot doc : docs) {
                WriteBatch batch = collection.getFirestore().batch();
                DocumentReference ref = doc.getReference();
                batch.delete(ref);
                batch.commit();
            }
        }
    }

    public static void clear() throws Exception {
        for (String table : ALL_TABLES) {
            if (table == null) {
                continue;
            }

            CollectionReference collection = database.collection(table);
            ApiFuture<QuerySnapshot> reader = collection.get();
            List<QueryDocumentSnapshot> docs = reader.get().getDocuments();

            for (QueryDocumentSnapshot doc : docs) {
                WriteBatch batch = collection.getFirestore().batch();
                DocumentReference ref = doc.getReference();
                batch.delete(ref);
                batch.commit();
            }
        }
    }

    public static Map.Entry<String, Integer> getLastNotification(String gameId) throws Exception {
        ApiFuture<QuerySnapshot> reader = database.collection(NOTIFICATIONS_TABLE)
                .whereEqualTo(GAME_ID_FIELD, gameId).get();
        List<QueryDocumentSnapshot> docs = reader.get().getDocuments();
        if (docs.isEmpty()) {
            return null;
        }

        DocumentSnapshot gameNotification = docs.get(0);

        String notificationMessage = (String) gameNotification.get(NOTIFICATION_MESSAGE_FIELD);
        Long notificationTime = (Long) gameNotification.get(NOTIFICATION_TIME_FIELD);

        if (notificationMessage == null || notificationTime == null) {
            throw new Exception();
        }

        return new Map.Entry<String, Integer>() {

            private String key = notificationMessage;
            private Integer value = notificationTime.intValue();

            @Override
            public String getKey() {
                return key;
            }

            @Override
            public Integer getValue() {
                return value;
            }

            @Override
            public Integer setValue(Integer arg0) {
                Integer oldValue = value;
                value = arg0;
                return oldValue;
            }

        };
    }

    public static void setLastNotification(String gameId, String notificationMessage) throws Exception {
        ApiFuture<QuerySnapshot> reader = database.collection(NOTIFICATIONS_TABLE)
                .whereEqualTo(GAME_ID_FIELD, gameId).get();
        List<QueryDocumentSnapshot> docs = reader.get().getDocuments();
        DocumentReference notification;
        long currentTime = System.currentTimeMillis();
        Map<String, Object> notificationInformation = new HashMap<>();
        
        notificationInformation.put(GAME_ID_FIELD, gameId);
        notificationInformation.put(NOTIFICATION_MESSAGE_FIELD, notificationMessage);
        notificationInformation.put(NOTIFICATION_TIME_FIELD, currentTime);

        ApiFuture<WriteResult> writer;

        if (docs.isEmpty()) {
            notification = database.collection(NOTIFICATIONS_TABLE).document();
            writer = notification.set(notificationInformation);
        } else {
            notification = docs.get(0).getReference();
            writer = notification.update(notificationInformation);
        }

        writer.get();
    }

    public static Map<String, Integer> getAvailableGames() throws Exception {
        Map<String, Integer> availableGames = new HashMap<>();
        ApiFuture<QuerySnapshot> reader = database.collection(GAME_TABLE_NAME)
                .whereEqualTo(GAME_STATE_FIELD, GameController.GAME_NOT_STARTED_STATE).get();
        List<QueryDocumentSnapshot> docs = reader.get().getDocuments();

        for (QueryDocumentSnapshot doc : docs) {
            String creator = (String) doc.get(GAME_CREATOR_FIELD);
            Long max = (Long) doc.get(GAME_MAX_PLAYERS_FIELD);

            if (max == null) {
                throw new Exception();
            }

            availableGames.put(creator, max.intValue());
        }

        return availableGames;
    }


}