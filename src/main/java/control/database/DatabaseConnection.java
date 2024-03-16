package control.database;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

public class DatabaseConnection {
    private static final Firestore database = FirestoreClient.getFirestore();


}
