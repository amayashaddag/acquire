package control.firebaseinit;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseClient {

    public static final String FIREBASE_CREDENTIALS_FILE_PATH = "src/main/ressources/auth/alternative.json";
    public static final String FIREBASE_EXCEPTION_MESSAGE = "Failed to connect to Firebase console in initializer.\n" +
            "Please contact support.";

    public static void initialize() throws IOException, FirebaseException {
        FileInputStream serviceAccount =
                new FileInputStream(FIREBASE_CREDENTIALS_FILE_PATH);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);

        if (FirebaseApp.getApps().isEmpty()) {
            throw new FirebaseException(FIREBASE_EXCEPTION_MESSAGE);
        }
    }
}
