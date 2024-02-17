package view.game;

import control.GameController;
import model.Player;
import view.Form;
import view.GameFrame;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.BorderLayout;

/**
 * The panel which has the map
 * 
 * @author Arthur Deck
 * @version 0.1
 * @see view.Form
 */
public class GameView extends Form {

    final int SIZE = 12; // TODO : delete for test
    final protected GameController controller;
    final protected Player player;
    final private JetonsPanel jetonsPanel;
    final private MouseManager mouseListener;

    AffineTransform at;

    public GameView(GameController controller, Player player) {
        super();
        this.setLayout(new BorderLayout());

        this.at = new AffineTransform();
        this.controller = controller;
        this.player = player;
        this.jetonsPanel = new JetonsPanel(this);
        this.mouseListener = new MouseManager(this);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setTransform(mouseListener.getAffineTransform());
        g2d.drawImage(Ressources.Assets.BACKGROUND, 0, 0, getWidth(), getHeight(), this);

        int cellWidth = getWidth() / SIZE;
        int cellHeight = getHeight() / SIZE;

        int x = 0;
        int y = 0;
        for (int row = 0; row < SIZE; row++) {  // TODO : if selection colorer la case
            x = -row * cellWidth / 2 + getWidth() / 2 - cellWidth;
            y = row * cellHeight / 2; // FIXME 20 for menu bar
            for (int col = 0; col < SIZE; col++) {
                x += cellWidth / 2;
                y += cellHeight / 2;
                g2d.drawImage(Ressources.Assets.GRASS, x, y, cellWidth, cellHeight, this);
            }
        }

        g2d.dispose();
    }

    /*------------------ Form ------------------- */

    /**
     * {@link view.Form#setOn()}
     */
    public void setOn(GameFrame g) {
        this.setSize(g.getWidth(), g.getHeight()); // FIXME map != null
        g.getContentPane().add(this);
    }
}
