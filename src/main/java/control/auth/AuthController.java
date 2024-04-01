package control.auth;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

public class AuthController {
    private final static Firestore database = FirestoreClient.getFirestore();

    private final static String EMAIL_FIELD = "email";
    private final static String PSEUDO_FIELD = "pseudo";
    private final static String PASSWORD_FIELD = "password";
    private final static String USER_ID_FIELD = "user-id";

    private final static String REGISTERED_USERS_DATABASE = "registered-users";

    private final static int MINIMUM_LENGTH_PASSWORD = 5;
    private final static String ALPHA_NUMERIC_AND_SYMBOLS_REGEX = "\"^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()-_=+\\\\\\\\|\\\\[{\\\\]};:'\\\",<.>/?]).+$\"";

    public static boolean alreadyRegisteredUser(String email) throws Exception {
        ApiFuture<QuerySnapshot> reader = database.collection(REGISTERED_USERS_DATABASE)
                .whereEqualTo(EMAIL_FIELD, email).get();
        List<QueryDocumentSnapshot> docs = reader.get().getDocuments();

        if (docs.isEmpty()) {
            return false;
        }

        return true;
    }

    public static String signUpWithEmailAndPassword(@Nonnull String email, @Nonnull String pseudo, @Nonnull String password) throws Exception {
        if (alreadyRegisteredUser(email)) {
            throw new AlreadyRegisteredUserException();
        }

        if (!isStrongPassword(password)) {
            throw new NotStrongEnoughPasswordException();
        }

        DocumentReference newUser = database.collection(password).document();
        String userId = newUser.getId();
        String hashedPassword = sha256(userId);

        Map<String, Object> userCredentials = new HashMap<>();
        userCredentials.put(EMAIL_FIELD, email);
        userCredentials.put(PSEUDO_FIELD, pseudo);
        userCredentials.put(PASSWORD_FIELD, hashedPassword);
        userCredentials.put(USER_ID_FIELD, userId);

        ApiFuture<WriteResult> writer = newUser.update(userCredentials);
        writer.get();

        return userId;
    }

    private static String sha256(String input) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(input.getBytes());
        byte[] hashedBytes = messageDigest.digest();

        return new String(hashedBytes);
    }

    private static boolean isStrongPassword(String password) throws Exception {
        if (password.length() <= MINIMUM_LENGTH_PASSWORD) {
            return false;
        }

        if (!password.matches(ALPHA_NUMERIC_AND_SYMBOLS_REGEX)) {
            return false;
        }

        return true;
    }

    public static String loginWithEmailAndPassword(String email, String password) throws Exception {
        ApiFuture<QuerySnapshot> reader = database.collection(REGISTERED_USERS_DATABASE)
                .whereEqualTo(EMAIL_FIELD, email).get();
        List<QueryDocumentSnapshot> docs = reader.get().getDocuments();

        if (docs.isEmpty()) {
            throw new NotExistingUserException();
        }

        QueryDocumentSnapshot doc = docs.get(0);
        String registeredHashedPassword = (String) doc.get(PASSWORD_FIELD);
        String hashedInputPassword = sha256(password);

        if (registeredHashedPassword == null) {
            throw new NullPointerException();
        }

        if (!registeredHashedPassword.equals(hashedInputPassword)) {
            throw new WrongPasswordException();
        }

        String userId = (String) doc.get(USER_ID_FIELD);
        return userId;
    }


}
