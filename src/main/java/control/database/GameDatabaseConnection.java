package control.database;

import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import control.auth.AuthController;
import control.game.GameController;
import model.game.Board;
import model.game.Cell;
import model.game.Corporation;
import model.game.Player;
import model.tools.PlayerAnalytics;
import model.tools.PlayerCredentials;
import model.tools.Point;
import model.tools.PreGameAnalytics;

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
    private static final String TIME_FIELD = "time";
    private final static String BEST_SCORE_FIELD = "best-score";
    private final static String PLAYED_GAMES_FIELD = "played-games";
    private final static String WON_GAMES_FIELD = "won-games";

    private static final String STOCKS_TABLE_NAME = "stocks";
    private static final String PLAYER_TABLE_NAME = "players";
    private static final String GAME_TABLE_NAME = "games";
    private static final String PLACED_CELLS_TABLE_NAME = "placed-cells";
    private static final String CURRENT_PLAYER_TABLE = "current-player";
    private static final String NOTIFICATIONS_TABLE = "notifications";
    private final static String ANALYTICS_TABLE = "analytics";
    private final static String KEEP_SELL_TRADE_STOCKS_TABLE = "analytics";
    private final static String MAJOR_CORPORATINO_TABLE = "major-corporation";

    private static final List<String> ALL_TABLES = new LinkedList<>();
    static {
        Collections.addAll(ALL_TABLES,
                STOCKS_TABLE_NAME,
                PLAYER_TABLE_NAME,
                GAME_TABLE_NAME,
                PLACED_CELLS_TABLE_NAME,
                CURRENT_PLAYER_TABLE,
                NOTIFICATIONS_TABLE,
                ANALYTICS_TABLE,
                KEEP_SELL_TRADE_STOCKS_TABLE,
                MAJOR_CORPORATINO_TABLE);
    }

    public static void addPlayer(String gameId, PlayerCredentials c) throws Exception {
        ApiFuture<QuerySnapshot> reader = database.collection(PLAYER_TABLE_NAME)
                .whereEqualTo(UID_FIELD, c.uid()).get();
        List<QueryDocumentSnapshot> docs = reader.get().getDocuments();
        if (!docs.isEmpty()) {
            throw new Exception();
        }

        HashMap<String, Object> newPlayer = new HashMap<>();

        newPlayer.put(PSEUDO_PLAYER_FIELD, c.pseudo());
        newPlayer.put(UID_PLAYER_FIELD, c.uid());
        newPlayer.put(GAME_ID_FIELD, gameId);
        newPlayer.put(PLAYER_CASH_FIELD, Player.INITIAL_CASH);
        newPlayer.put(PLAYER_NET_FIELD, Player.INITIAL_NET);

        DocumentReference docRef = database.collection(PLAYER_TABLE_NAME).document();
        ApiFuture<WriteResult> future = docRef.set(newPlayer);
        future.get();

        initStocks(gameId, c);
    }

    private static void initStocks(String gameId, PlayerCredentials credentials) throws Exception {
        for (Corporation c : Corporation.values()) {
            DocumentReference doc = database.collection(STOCKS_TABLE_NAME).document();
            Map<String, Object> stocks = new HashMap<>();
            stocks.put(CORPORATION_FIELD, c.toString());
            stocks.put(STOCKS_AMOUNT_FIELD, 0);
            stocks.put(UID_FIELD, credentials.uid());
            stocks.put(GAME_ID_FIELD, gameId);

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

    public static PreGameAnalytics createGame(PlayerCredentials creator, int maxPlayers) throws Exception {
        DocumentReference doc = database.collection(GAME_TABLE_NAME).document();
        String gameId = doc.getId();
        HashMap<String, Object> newGame = new HashMap<>();
        newGame.put(GAME_ID_FIELD, gameId);
        newGame.put(GAME_STATE_FIELD, 0);
        newGame.put(GAME_MAX_PLAYERS_FIELD, maxPlayers);
        newGame.put(GAME_CREATOR_FIELD, creator.pseudo());
        ApiFuture<WriteResult> future = doc.set(newGame);
        future.get();

        return new PreGameAnalytics(creator.pseudo(), gameId, 1, maxPlayers);
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
        String uid = player.getUID();

        for (Corporation c : Corporation.values()) {
            int amount = player.getStocks(c);
            String corporationName = c.toString();
            ApiFuture<QuerySnapshot> reader = collection
                    .whereEqualTo(GAME_ID_FIELD, gameId)
                    .whereEqualTo(UID_FIELD, uid)
                    .whereEqualTo(CORPORATION_FIELD, corporationName)
                    .get();
            QueryDocumentSnapshot doc = reader.get().getDocuments().get(0);
            DocumentReference ref = doc.getReference();

            Map<String, Object> updatedReference = new HashMap<>();
            updatedReference.put(STOCKS_AMOUNT_FIELD, amount);
            updatedReference.put(CORPORATION_FIELD, corporationName);
            updatedReference.put(UID_FIELD, uid);

            ApiFuture<WriteResult> writer = ref.update(updatedReference);
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

    public static boolean isGameStarted(String gameId) throws Exception {
        ApiFuture<QuerySnapshot> reader = database.collection(GAME_TABLE_NAME)
                .whereEqualTo(GAME_ID_FIELD, gameId)
                .whereEqualTo(GAME_STATE_FIELD, GameController.GAME_NOT_STARTED_STATE)
                .get();
        List<QueryDocumentSnapshot> docs = reader.get().getDocuments();

        if (docs.isEmpty()) {
            return true;
        }

        return false;
    }

    public static boolean isGameFull(String gameId) throws Exception {
        ApiFuture<QuerySnapshot> reader = database.collection(GAME_TABLE_NAME)
                .whereEqualTo(GAME_ID_FIELD, gameId)
                .whereEqualTo(GAME_STATE_FIELD, GameController.GAME_NOT_STARTED_STATE)
                .get();
        List<QueryDocumentSnapshot> docs = reader.get().getDocuments();

        if (docs.isEmpty()) {
            return true;
        }

        Long maxAllowedPlayers = (Long) docs.get(0).get(GAME_MAX_PLAYERS_FIELD);

        if (maxAllowedPlayers == null) {
            throw new NullPointerException();
        }

        int maxPlayers = maxAllowedPlayers.intValue();

        ApiFuture<QuerySnapshot> maxPlayersReader = database.collection(PLAYER_TABLE_NAME)
                .whereEqualTo(GAME_ID_FIELD, gameId).get();
        List<QueryDocumentSnapshot> listOfPlayers = maxPlayersReader.get().getDocuments();

        if (listOfPlayers.size() >= maxPlayers) {
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
        Long notificationTime = (Long) gameNotification.get(TIME_FIELD);

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
        long currentTime = Instant.now().toEpochMilli();
        Map<String, Object> notificationInformation = new HashMap<>();

        notificationInformation.put(GAME_ID_FIELD, gameId);
        notificationInformation.put(NOTIFICATION_MESSAGE_FIELD, notificationMessage);
        notificationInformation.put(TIME_FIELD, currentTime);

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

    public static List<PreGameAnalytics> getAvailableGames() throws Exception {
        List<PreGameAnalytics> availableGames = new LinkedList<>();
        ApiFuture<QuerySnapshot> reader = database.collection(GAME_TABLE_NAME)
                .whereEqualTo(GAME_STATE_FIELD, GameController.GAME_NOT_STARTED_STATE).get();
        List<QueryDocumentSnapshot> docs = reader.get().getDocuments();

        for (QueryDocumentSnapshot doc : docs) {
            String creator = (String) doc.get(GAME_CREATOR_FIELD);
            String gameId = (String) doc.get(GAME_ID_FIELD);
            Long max = (Long) doc.get(GAME_MAX_PLAYERS_FIELD);

            if (max == null) {
                throw new Exception();
            }

            ApiFuture<QuerySnapshot> currentPlayersReader = database.collection(PLAYER_TABLE_NAME)
                    .whereEqualTo(GAME_ID_FIELD, gameId).get();
            List<QueryDocumentSnapshot> currentPlayersInGame = currentPlayersReader.get().getDocuments();
            int currentNumberOfPlayer = currentPlayersInGame.size();

            PreGameAnalytics analytics = new PreGameAnalytics(
                creator, 
                gameId, 
                currentNumberOfPlayer, 
                max.intValue());
            availableGames.add(analytics);
        }

        return availableGames;
    }

    public static PlayerAnalytics getPlayerAnalytics(String userId) throws Exception {
        ApiFuture<QuerySnapshot> reader = database.collection(ANALYTICS_TABLE)
                .whereEqualTo(UID_FIELD, userId).get();
        List<QueryDocumentSnapshot> docs = reader.get().getDocuments();

        if (docs.isEmpty()) {
            throw new Exception();
        }

        DocumentSnapshot doc = docs.get(0);
        PlayerCredentials credentials = AuthController.getPlayerCredentials(userId);
        String pseudo = credentials.pseudo();
        Long bestScore = (Long) doc.get(BEST_SCORE_FIELD);
        Long playedGames = (Long) doc.get(PLAYED_GAMES_FIELD);
        Long wonGames = (Long) doc.get(WON_GAMES_FIELD);

        if (pseudo == null || bestScore == null || wonGames == null || playedGames == null) {
            throw new NullPointerException();
        }

        return new PlayerAnalytics(
                pseudo,
                bestScore.intValue(),
                playedGames.intValue(),
                wonGames.intValue(),
                playedGames.intValue() - wonGames.intValue());
    }

    public static List<PlayerAnalytics> getRanking() throws Exception {
        ApiFuture<QuerySnapshot> reader = database.collection(ANALYTICS_TABLE).get();
        List<QueryDocumentSnapshot> docs = reader.get().getDocuments();
        List<PlayerAnalytics> players = new LinkedList<>();

        for (DocumentSnapshot doc : docs) {
            String userId = (String) doc.get(UID_FIELD);
            Long bestScore = (Long) doc.get(BEST_SCORE_FIELD);
            Long playedGames = (Long) doc.get(PLAYED_GAMES_FIELD);
            Long wonGames = (Long) doc.get(WON_GAMES_FIELD);

            if (userId == null || bestScore == null || wonGames == null || playedGames == null) {
                throw new NullPointerException();
            }

            String pseudo = AuthController.getPlayerCredentials(userId).pseudo();

            PlayerAnalytics analytics = new PlayerAnalytics(pseudo,
                    bestScore.intValue(),
                    playedGames.intValue(),
                    wonGames.intValue(),
                    playedGames.intValue() - wonGames.intValue());

            players.add(analytics);
        }

        players.sort(new Comparator<PlayerAnalytics>() {

            @Override
            public int compare(PlayerAnalytics arg0, PlayerAnalytics arg1) {
                return arg0.bestScore() - arg1.bestScore();
            }

        });

        return players;
    }
    

    public static void updateStocks(Player p, String gameId) throws Exception {
        ApiFuture<QuerySnapshot> reader = database.collection(STOCKS_TABLE_NAME)
                .whereEqualTo(UID_FIELD, p.getUID())
                .whereEqualTo(GAME_ID_FIELD, gameId)
                .get();
        List<QueryDocumentSnapshot> docs = reader.get().getDocuments();

        for (QueryDocumentSnapshot doc : docs) {
            Long amount = (Long) doc.get(STOCKS_AMOUNT_FIELD);
            String corporationName = (String) doc.get(CORPORATION_FIELD);

            if (amount == null) {
                throw new Exception();
            }

            Corporation corporation = Corporation.getCorporationFromName(corporationName);

            p.setStocks(corporation, amount.intValue());
        }
    }

    public static void startGame(String gameId) throws Exception {
        ApiFuture<QuerySnapshot> reader = database.collection(GAME_TABLE_NAME)
                .whereEqualTo(GAME_ID_FIELD, gameId).get();
        List<QueryDocumentSnapshot> docs = reader.get().getDocuments();

        if (docs.isEmpty()) {
            throw new Exception();
        }

        DocumentSnapshot game = docs.get(0);
        DocumentReference gameRef = game.getReference();

        ApiFuture<WriteResult> writer = gameRef.update(GAME_STATE_FIELD, GameController.GAME_IN_PROGRESS_STATE);
        writer.get();
    }

    public static List<Player> getAllPlayers(String gameId) throws Exception {
        ApiFuture<QuerySnapshot> reader = database.collection(PLAYER_TABLE_NAME)
                .whereEqualTo(GAME_ID_FIELD, gameId).get();
        List<QueryDocumentSnapshot> docs = reader.get().getDocuments();
        List<Player> allPlayers = new LinkedList<>();

        if (docs.isEmpty()) {
            throw new Exception();
        }

        for (DocumentSnapshot doc : docs) {
            String userId = (String) doc.get(UID_FIELD);
            String pseudo = (String) doc.get(PSEUDO_PLAYER_FIELD);

            Player p = Player.createHumanPlayer(pseudo, userId);
            allPlayers.add(p);
        }

        return allPlayers;
    }

    public static Map<Corporation, Long> getKeepSellOrTradeStocks(String gameId, long lastTime) throws Exception {
        ApiFuture<QuerySnapshot> reader = database.collection(KEEP_SELL_TRADE_STOCKS_TABLE)
                .whereEqualTo(GAME_ID_FIELD, gameId)
                .whereGreaterThan(TIME_FIELD, lastTime).get();
        List<QueryDocumentSnapshot> docs = reader.get().getDocuments();
        Map<Corporation, Long> stocks = new HashMap<>();

        if (docs.isEmpty()) {
            return stocks;
        }

        for (DocumentSnapshot doc : docs) {
            String corporationName = (String) doc.get(CORPORATION_FIELD);
            Long time = (Long) doc.get(TIME_FIELD);

            if (time == null || corporationName == null) {
                throw new NullPointerException();
            }

            stocks.put(Corporation.getCorporationFromName(corporationName), time);
        }

        return stocks;
    }

    public static void setKeepSellOrTradeStocks(Set<Corporation> stocks, String gameId) throws Exception {
        long time = Instant.now().toEpochMilli();
        for (Corporation c : stocks) {
            Map<String, Object> stockFields = new HashMap<>();
            stockFields.put(GAME_ID_FIELD, gameId);
            stockFields.put(CORPORATION_FIELD, c.toString());
            stockFields.put(TIME_FIELD, time);

            ApiFuture<QuerySnapshot> reader = database.collection(KEEP_SELL_TRADE_STOCKS_TABLE)
                    .whereEqualTo(GAME_ID_FIELD, gameId)
                    .whereEqualTo(CORPORATION_FIELD, c.toString())
                    .whereLessThan(TIME_FIELD, time).get();
            List<QueryDocumentSnapshot> docs = reader.get().getDocuments();
            ApiFuture<WriteResult> writer;
            DocumentReference stockRef;

            if (docs.isEmpty()) {
                stockRef = database.collection(KEEP_SELL_TRADE_STOCKS_TABLE).document();
                writer = stockRef.set(stockFields);
            } else {
                stockRef = docs.get(0).getReference();
                writer = stockRef.update(stockFields);
            }

            writer.get();
        }
    }

    public static Corporation getMajorCorporation(String gameId, long time) throws Exception {
        ApiFuture<QuerySnapshot> reader = database.collection(MAJOR_CORPORATINO_TABLE)
                .whereEqualTo(GAME_ID_FIELD, gameId)
                .whereEqualTo(TIME_FIELD, time).get();
        List<QueryDocumentSnapshot> docs = reader.get().getDocuments();

        if (docs.isEmpty()) {
            throw new Exception();
        }

        DocumentSnapshot doc = docs.get(0);
        String corporationName = (String) doc.get(CORPORATION_FIELD);
        Corporation major = Corporation.getCorporationFromName(corporationName);

        return major;
    }

    public static void setMajorCorporation(String gameId, Corporation major) throws Exception {
        ApiFuture<QuerySnapshot> reader = database.collection(MAJOR_CORPORATINO_TABLE)
                .whereEqualTo(GAME_ID_FIELD, gameId).get();
        List<QueryDocumentSnapshot> docs = reader.get().getDocuments();
        Map<String, Object> majorFields = new HashMap<>();
        DocumentReference majorRef;
        ApiFuture<WriteResult> writer;
        long time = Instant.now().toEpochMilli();

        majorFields.put(GAME_ID_FIELD, gameId);
        majorFields.put(CORPORATION_FIELD, major.toString());
        majorFields.put(TIME_FIELD, time);

        if (docs.isEmpty()) {
            majorRef = database.collection(MAJOR_CORPORATINO_TABLE).document();
            writer = majorRef.set(majorFields);
        } else {
            majorRef = docs.get(0).getReference();
            writer = majorRef.update(majorFields);
        }

        writer.get();
    }

}