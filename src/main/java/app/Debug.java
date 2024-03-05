package app;

import control.auth.FirebaseInitializer;
import frame.GameFrame;
import login.LoginView;

public class Debug {
    public static void main(String[] args) {
        GameFrame frame = new GameFrame();
        LoginView loginView = new LoginView();
        FirebaseInitializer.initialize();

        frame.setContentPane(loginView);
        frame.setVisible(true);
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
