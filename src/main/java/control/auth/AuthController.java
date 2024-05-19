package control.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

import model.tools.PlayerCredentials;

public class AuthController {
    private final static Firestore database = FirestoreClient.getFirestore();

    private final static String EMAIL_FIELD = "email";
    private final static String PSEUDO_FIELD = "pseudo";
    private final static String PASSWORD_FIELD = "password";
    private final static String USER_ID_FIELD = "uid";
    private final static String BEST_SCORE_FIELD = "best-score";
    private final static String PLAYED_GAMES_FIELD = "played-games";
    private final static String WON_GAMES_FIELD = "won-games";

    public final static String REGISTERED_USERS_TABLE = "registered-users";
    public final static String ANALYTICS_TABLE = "analytics";

    private final static String ALPHA_NUMERIC_AND_SYMBOLS_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$";
    private final static int MAX_PASSWORD_LENGTH = 20;

    public static synchronized boolean alreadyRegisteredUser(String email) throws Exception {
        ApiFuture<QuerySnapshot> reader = database.collection(REGISTERED_USERS_TABLE)
                .whereEqualTo(EMAIL_FIELD, email).get();
        List<QueryDocumentSnapshot> docs = reader.get().getDocuments();

        if (docs.isEmpty()) {
            return false;
        }

        return true;
    }

    public static synchronized String signUpWithEmailAndPassword(@Nonnull String email, @Nonnull String pseudo,
            @Nonnull String password) throws Exception {
        if (alreadyRegisteredUser(email)) {
            throw new AlreadyRegisteredUserException();
        }

        if (password.length() > MAX_PASSWORD_LENGTH) {
            throw new TooLongPasswordException();
        }

        if (!isStrongPassword(password)) {
            throw new NotStrongEnoughPasswordException();
        }

        DocumentReference newUser = database.collection(REGISTERED_USERS_TABLE).document();
        String userId = newUser.getId();

        Map<String, Object> userCredentials = new HashMap<>();
        userCredentials.put(EMAIL_FIELD, email);
        userCredentials.put(PSEUDO_FIELD, pseudo);
        userCredentials.put(PASSWORD_FIELD, password);
        userCredentials.put(USER_ID_FIELD, userId);

        ApiFuture<WriteResult> writer = newUser.set(userCredentials);
        writer.get();

        addToAnalytics(userId);

        return userId;
    }

    private static synchronized boolean isStrongPassword(String password) throws Exception {

        if (!password.matches(ALPHA_NUMERIC_AND_SYMBOLS_REGEX)) {
            return false;
        }

        return true;
    }

    public static synchronized String loginWithEmailAndPassword(String email, String password) throws Exception {
        ApiFuture<QuerySnapshot> reader = database.collection(REGISTERED_USERS_TABLE)
                .whereEqualTo(EMAIL_FIELD, email).get();
        List<QueryDocumentSnapshot> docs = reader.get().getDocuments();

        if (docs.isEmpty()) {
            throw new NotExistingUserException();
        }

        QueryDocumentSnapshot doc = docs.get(0);
        String registeredPassword = (String) doc.get(PASSWORD_FIELD);

        if (registeredPassword == null) {
            throw new NullPointerException();
        }

        if (!registeredPassword.equals(password)) {
            throw new WrongPasswordException();
        }

        String userId = (String) doc.get(USER_ID_FIELD);
        return userId;
    }

    public static synchronized void addToAnalytics(String userId) throws Exception {
        if (alreadyExistingAnalytics(userId)) {
            throw new Exception();
        }

        DocumentReference ref = database.collection(ANALYTICS_TABLE).document();
        Map<String, Object> playerAnalytics = new HashMap<>();

        playerAnalytics.put(USER_ID_FIELD, userId);
        playerAnalytics.put(PLAYED_GAMES_FIELD, 0);
        playerAnalytics.put(WON_GAMES_FIELD, 0);
        playerAnalytics.put(BEST_SCORE_FIELD, 0);

        ApiFuture<WriteResult> writer = ref.set(playerAnalytics);
        writer.get();
    }

    private static synchronized boolean alreadyExistingAnalytics(String userId) throws Exception {
        ApiFuture<QuerySnapshot> reader = database.collection(ANALYTICS_TABLE)
                .whereEqualTo(USER_ID_FIELD, userId).get();
        List<QueryDocumentSnapshot> docs = reader.get().getDocuments();

        return !docs.isEmpty();
    }

    public static synchronized PlayerCredentials getPlayerCredentials(String userId) throws Exception {
        ApiFuture<QuerySnapshot> reader = database.collection(REGISTERED_USERS_TABLE)
                .whereEqualTo(USER_ID_FIELD, userId).get();
        List<QueryDocumentSnapshot> docs = reader.get().getDocuments();

        if (docs.isEmpty()) {
            throw new Exception();
        }

        DocumentSnapshot doc = docs.get(0);

        String email = (String) doc.get(EMAIL_FIELD);
        String pseudo = (String) doc.get(PSEUDO_FIELD);

        return new PlayerCredentials(userId, email, pseudo);
    }

}
