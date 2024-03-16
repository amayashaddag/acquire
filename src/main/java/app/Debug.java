package app;

import javax.swing.JFrame;
import login.*;

import control.auth.FirebaseClient;

public class Debug {
    public static void main(String[] args) throws Exception {
        try {
            JFrame frame = new JFrame();
            LoginView loginView = new LoginView();
            FirebaseClient.initialize();

            frame.setContentPane(loginView);
            frame.setVisible(true);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

/* signInButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        String email = idArea.getText();
        String password = passwordArea.getText();
        
        String customToken = FirebaseInitializer.getCustomToken(email, password);
        if (customToken != null) {
            System.out.println("Custom token: " + customToken);
        } else {
            System.out.println("Echec de la creation du token d'authentification");
        
        }
    }
}); */
