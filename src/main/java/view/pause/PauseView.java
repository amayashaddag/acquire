package view.pause;

import javax.swing.*;

import com.formdev.flatlaf.FlatDarculaLaf;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PauseView extends JFrame {

    private BufferedImage backgroundImage;
    private BufferedImage blurredImage;
    private JPanel backgroundPanel;

    public PauseView() {
        super("Blur Example");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ecouteur de redimensionnement pour ajuster le flou lorsque la fenetre est redimensionnee
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (backgroundImage != null) {
                    blurredImage = applyGaussianBlur(backgroundImage, 7, 2);
                    backgroundPanel.repaint();
                }
            }
        });

        try {
            
            backgroundImage = ImageIO.read(new File("src/main/ressources/images/menu/background.jpeg"));

            
            blurredImage = applyGaussianBlur(backgroundImage, 7, 2);

            
            backgroundPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    
                    g.drawImage(blurredImage, 0, 0, getWidth(), getHeight(), this);
                }
            };

            
            JPanel buttonPanel = new JPanel(new GridBagLayout());
            buttonPanel.setOpaque(false); 

            
            JButton resumeButton = new JButton("Resume");
            JButton backButton = new JButton("Disconnect");

            Dimension buttonSize = new Dimension(150, 30); 
            resumeButton.setPreferredSize(buttonSize);
            backButton.setPreferredSize(buttonSize);

            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(5, 0, 5, 0); 

            
            buttonPanel.add(resumeButton, gbc);

            
            gbc.gridy = 1;
            buttonPanel.add(backButton, gbc);

            
            backgroundPanel.setLayout(new GridBagLayout());
            backgroundPanel.add(buttonPanel, new GridBagConstraints());

            setContentPane(backgroundPanel);

            resumeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // logique pour "Resume"
                }
            });

            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // logique pour "disconnect"
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
            new PauseView().setVisible(true);
        });
    }
}
       


