package control.firebaseinit;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

public class FirebaseClient {

    public static final String FIREBASE_CREDENTIALS_FILE_PATH = "src/main/ressources/auth/very-emergency-alternative.json";
    public static final String FIREBASE_EXCEPTION_MESSAGE = "Failed to connect to Firebase console in initializer.\n" +
            "Please contact support.";

    private static boolean isConnected = false;

    public static void initialize() {

        try {
            URL url = new URL("http://www.google.com");
            URLConnection conn = url.openConnection();
            conn.connect();
            conn.getInputStream().close();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return;
        }

        try {
            FileInputStream serviceAccount = new FileInputStream(FIREBASE_CREDENTIALS_FILE_PATH);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
            FirestoreClient.getFirestore();

            isConnected = true;
        } catch (Exception e) {
            isConnected = false;
        }
    }

    public static boolean isConnected() {
        return isConnected;
    }
}
