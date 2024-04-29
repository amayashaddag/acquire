package view.pause;
import javax.swing.*;

import com.formdev.flatlaf.FlatDarculaLaf;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class BlurImage extends JFrame {

    public BlurImage() {
        super("Blur Example");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            // Charger l'image
            BufferedImage backgroundImage = ImageIO.read(new File("src/main/ressources/images/menu/background.jpeg"));

            // Appliquer le flou gaussien à l'image
            BufferedImage blurredImage = applyGaussianBlur(backgroundImage, 10, 2);

            // Utiliser un JPanel personnalisé pour afficher l'image floue en arrière-plan
            JPanel backgroundPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(blurredImage, 0, 0, getWidth(), getHeight(), this);
                }
            };

            // Création du panel pour les boutons avec GridBagLayout
            JPanel buttonPanel = new JPanel(new GridBagLayout());
            buttonPanel.setOpaque(false); // Rendre le panel transparent

            // Création des boutons "Resume" et "Back To Menu"
            JButton resumeButton = new JButton("Resume");
            JButton backButton = new JButton("Disconnect");

            // Configuration des contraintes pour centrer les boutons
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(10, 0, 10, 0); // Espacement pour les boutons

            // Ajout du bouton "Resume" au panel
            buttonPanel.add(resumeButton, gbc);

            // Configuration des contraintes pour centrer les boutons
            gbc.gridy = 1;
            buttonPanel.add(backButton, gbc);

            // Ajout du panel de boutons au centre du panel principal
            backgroundPanel.setLayout(new GridBagLayout());
            backgroundPanel.add(buttonPanel, new GridBagConstraints());

            setContentPane(backgroundPanel);

            // Ajout des écouteurs d'événements aux boutons
            resumeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Code à exécuter lorsque le bouton "Resume" est cliqué
                    // Ajoutez votre logique ici
                }
            });

            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Code à exécuter lorsque le bouton "Back To Menu" est cliqué
                    // Ajoutez votre logique ici
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private BufferedImage applyGaussianBlur(BufferedImage srcImage, int radius, int iterations) {
        int size = radius * 2 + 1;
        float weight = 1.0f / (size * size);
        float[] data = new float[size * size];

        for (int i = 0; i < data.length; i++) {
            data[i] = weight;
        }

        Kernel kernel = new Kernel(size, size, data);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        BufferedImage result = srcImage;
        for (int i = 0; i < iterations; i++) {
            result = op.filter(result, null);
        }
        return result;
    }

    public static void main(String[] args) {

        FlatDarculaLaf.setup();

        SwingUtilities.invokeLater(() -> {
            new BlurImage().setVisible(true);
        });
    }
}
