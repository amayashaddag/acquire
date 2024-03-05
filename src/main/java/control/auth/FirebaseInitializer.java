package control.auth;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;

import java.io.FileInputStream;

public class FirebaseInitializer {

    public static final String FIREBASE_CREDENTIALS_FILE_PATH = "src/main/ressources/auth/firebase-credentials.json";
    public static void initialize() {
        try {
            FileInputStream serviceAccount =
                    new FileInputStream(FIREBASE_CREDENTIALS_FILE_PATH);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);

        } catch (Exception e) {
            // TODO : Arthur should handle this in GUI
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        initialize();
    }
}
