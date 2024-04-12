package view.endgame;

/**
 *
 * @author laminebetraoui
 */
public class ExitMenu extends javax.swing.JFrame {

    /**
     * Creates new form ExitMenu
     */
    public ExitMenu() {
        initComponents();
    }
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        mainPanel = new javax.swing.JPanel();
        scorePanel = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        menuButton = new javax.swing.JButton();
        GameOverLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setFont(new java.awt.Font("Bambino-Bold", 1, 18));
        setMinimumSize(new java.awt.Dimension(1000, 1000));

        mainPanel.setBackground(new java.awt.Color(243, 235, 174));
        mainPanel.setLayout(new java.awt.GridBagLayout());

        scorePanel.setBackground(new java.awt.Color(9, 55, 61));
        scorePanel.setPreferredSize(new java.awt.Dimension(350, 350));
        scorePanel.setLayout(new java.awt.GridBagLayout());

        jTextField1.setEditable(false);
        jTextField1.setBackground(new java.awt.Color(0, 94, 91));
        jTextField1.setFont(new java.awt.Font("DejaVu Math TeX Gyre", 1, 15)); 
        jTextField1.setForeground(new java.awt.Color(255, 255, 255));
        jTextField1.setText("1.Amayas : 40 000$");
        jTextField1.setPreferredSize(new java.awt.Dimension(124, 40));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 15, 10, 15);
        scorePanel.add(jTextField1, gridBagConstraints);

        jTextField2.setEditable(false);
        jTextField2.setBackground(new java.awt.Color(0, 94, 91));
        jTextField2.setFont(new java.awt.Font("DejaVu Math TeX Gyre", 1, 15));
        jTextField2.setForeground(new java.awt.Color(255, 255, 255));
        jTextField2.setText("2.Lamine : 38 000$");
        jTextField2.setPreferredSize(new java.awt.Dimension(124, 40));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 15, 10, 15);
        scorePanel.add(jTextField2, gridBagConstraints);

        jTextField3.setEditable(false);
        jTextField3.setBackground(new java.awt.Color(0, 94, 91));
        jTextField3.setFont(new java.awt.Font("DejaVu Math TeX Gyre", 1, 15));
        jTextField3.setForeground(new java.awt.Color(255, 255, 255));
        jTextField3.setText("5.Arthur : 0$");
        jTextField3.setPreferredSize(new java.awt.Dimension(124, 40));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 15, 10, 15);
        scorePanel.add(jTextField3, gridBagConstraints);

        jTextField4.setEditable(false);
        jTextField4.setBackground(new java.awt.Color(0, 94, 91));
        jTextField4.setFont(new java.awt.Font("DejaVu Math TeX Gyre", 1, 15)); 
        jTextField4.setForeground(new java.awt.Color(255, 255, 255));
        jTextField4.setText("3.Igor : 32 000$");
        jTextField4.setPreferredSize(new java.awt.Dimension(124, 40));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 15, 10, 15);
        scorePanel.add(jTextField4, gridBagConstraints);

        jTextField5.setEditable(false);
        jTextField5.setBackground(new java.awt.Color(0, 94, 91));
        jTextField5.setFont(new java.awt.Font("DejaVu Math TeX Gyre", 1, 15));
        jTextField5.setForeground(new java.awt.Color(255, 255, 255));
        jTextField5.setText("4.Nida : 100$");
        jTextField5.setPreferredSize(new java.awt.Dimension(124, 40));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 15, 10, 15);
        scorePanel.add(jTextField5, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(35, 0, 10, 0);
        mainPanel.add(scorePanel, gridBagConstraints);

        menuButton.setBackground(new java.awt.Color(0, 94, 91));
        menuButton.setFont(new java.awt.Font("DejaVu Math TeX Gyre", 1, 15));
        menuButton.setForeground(new java.awt.Color(255, 255, 255));
        menuButton.setText("Go to Menu");
        menuButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        menuButton.setPreferredSize(new java.awt.Dimension(120, 72));
        menuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, -50, 0);
        mainPanel.add(menuButton, gridBagConstraints);

        GameOverLabel.setFont(new java.awt.Font("DejaVu Sans", 1, 36)); // NOI18N
        GameOverLabel.setText("Game Over");
        mainPanel.add(GameOverLabel, new java.awt.GridBagConstraints());

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>                        

    private void menuButtonActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // TODO add your handling code here:
    }                                          

                               

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ExitMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ExitMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ExitMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ExitMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }



        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ExitMenu().setVisible(true);
            }
        });
    }
                 
    private javax.swing.JLabel GameOverLabel;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton menuButton;
    private javax.swing.JPanel scorePanel;
                 
}
