package view.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author Arthur Deck
 */
@Deprecated
public class KeyManager implements KeyListener {
    public KeyManager(GameView g) {
        this.g = g;
        g.addKeyListener(this);
        g.setFocusable(true);
    }

    private boolean enabled = true;
    final private GameView g;

    public void setEnabled(boolean arg) {
        this.enabled = arg;
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (!enabled) return;
    }

    @Override
    public void keyReleased(KeyEvent e) {if (!enabled) return;}
    @Override
    public void keyTyped(KeyEvent e) {if (!enabled) return;}
}
