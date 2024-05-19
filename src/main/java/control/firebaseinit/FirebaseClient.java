package control.firebaseinit;

import java.io.FileInputStream;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseClient {

    public static final String FIREBASE_CREDENTIALS_FILE_PATH = "src/main/ressources/auth/emergency-alternative.json";
    public static final String FIREBASE_EXCEPTION_MESSAGE = "Failed to connect to Firebase console in initializer.\n" +
            "Please contact support.";

    private static boolean isConnected = false;

    public static void initialize() {
        try {
            FileInputStream serviceAccount = new FileInputStream(FIREBASE_CREDENTIALS_FILE_PATH);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
            FirebaseApp.getInstance();
            FirebaseDatabase.getInstance();

            isConnected = true;

        } catch (Exception e) {
            isConnected = false;
        }
    }

    public static boolean isConnected() {
        return isConnected;
    }
}
