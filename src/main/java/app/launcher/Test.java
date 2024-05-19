package app.launcher;

import control.auth.AuthController;
import control.database.GameDatabaseConnection;
import control.firebaseinit.FirebaseClient;

public class Test {
    public static void main(String[] args) {

        try {
            FirebaseClient.initialize();
            GameDatabaseConnection.clear();

            AuthController.signUpWithEmailAndPassword("a", "arthur.91", "Password@2024");
            
            AuthController.signUpWithEmailAndPassword("b", "banana_xx.91", "Password@2024");
            
            AuthController.signUpWithEmailAndPassword("c", "amayas", "Password@2024");
            
            AuthController.signUpWithEmailAndPassword("d", "lamine.crayon", "Password@2024");
            
            AuthController.signUpWithEmailAndPassword("e", "maxiBestOf", "Password@2024");
            
            AuthController.signUpWithEmailAndPassword("f", "igor-de-nazareth", "Password@2024");
            
            AuthController.signUpWithEmailAndPassword("g", "Taylor Swift", "Password@2024");
            
            AuthController.signUpWithEmailAndPassword("h", "Taylor Lagrange", "Password@2024");
            
            AuthController.signUpWithEmailAndPassword("i", "Georges Skandalis", "Password@2024");
            
            AuthController.signUpWithEmailAndPassword("j", "Anne Micheli", "Password@2024");
            
            AuthController.signUpWithEmailAndPassword("k", "Maximilien Lesellier", "Password@2024");
            
            AuthController.signUpWithEmailAndPassword("l", "Vlady Ravelomanana", "Password@2024");
            
            AuthController.signUpWithEmailAndPassword("m", "Riccardo Brasca", "Password@2024");
            
            AuthController.signUpWithEmailAndPassword("n", "Thierry Joly", "Password@2024");
            
            AuthController.signUpWithEmailAndPassword("o", "Thierry Henry", "Password@2024");
            
            AuthController.signUpWithEmailAndPassword("p", "Cedric Doumbé", "Password@2024");
            
            AuthController.signUpWithEmailAndPassword("q", "Arthur Troupel", "Password@2024");
            
            AuthController.signUpWithEmailAndPassword("r", "Elie Studnia", "Password@2024");
            
            AuthController.signUpWithEmailAndPassword("s", "Guillaume Malod", "Password@2024");
            
            AuthController.signUpWithEmailAndPassword("t", "Aldric Degorre", "Password@2024");
            
            AuthController.signUpWithEmailAndPassword("u", "Emmanuel Letellier", "Password@2024");
            
            AuthController.signUpWithEmailAndPassword("v", "Dominique Poulalhon", "Password@2024");
            
            AuthController.signUpWithEmailAndPassword("w", "Matthieu Picantin", "Password@2024");
            
            AuthController.signUpWithEmailAndPassword("x", "Yan Jurski", "Password@2024");
            
            AuthController.signUpWithEmailAndPassword("y", "Adrien Boyer", "Password@2024");
            
            AuthController.signUpWithEmailAndPassword("z", "Tommaso Scognamiglio", "Password@2024");
            
            AuthController.signUpWithEmailAndPassword("aa", "Jaouad Sahbani", "Password@2024");
            
            AuthController.signUpWithEmailAndPassword("ab", "Ralf Treinen", "Password@2024");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}