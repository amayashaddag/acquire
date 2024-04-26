package view.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatPanel extends JPanel {

    public ChatPanel() {
        setLayout(new BorderLayout());

        // Création du chatPane
        JTextPane chatPane = new JTextPane();
        chatPane.setEditable(false); // Rendre la zone de chat non modifiable
        JScrollPane chatScrollPane = new JScrollPane(chatPane);
        chatScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Ajout du chatPane au panel en bas à droite
        add(chatScrollPane, BorderLayout.SOUTH);

        // Création du champ de saisie pour les nouveaux messages
        JTextField inputField = new JTextField(20);
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage(chatPane, inputField); // Envoyer le message lorsqu'Entrée est pressée
            }
        });

        // Ajout du champ de saisie au panel en bas
        add(inputField, BorderLayout.CENTER);
    }

    // Méthode pour ajouter un message à la zone de chat
    private static void appendMessage(JTextPane chatPane, String sender, String message) {
        // Ajouter le nom d'utilisateur et le message à la zone de chat
        chatPane.setText(chatPane.getText() + sender + " : " + message + "\n");
        chatPane.setCaretPosition(chatPane.getDocument().getLength()); // Faire défiler vers le bas pour afficher le nouveau message
    }

    // Méthode pour envoyer un message à partir du champ de saisie principal
    private static void sendMessage(JTextPane chatPane, JTextField inputField) {
        String message = inputField.getText();
        if (!message.isEmpty()) {
            appendMessage(chatPane, "Utilisateur", message); // Ajouter le message à la zone de chat
            inputField.setText(""); // Effacer le champ de saisie après l'envoi du message
        }
    }
}
