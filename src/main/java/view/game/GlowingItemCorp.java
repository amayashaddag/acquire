package view.game;

import javaswingdev.pggb.PanelGlowingGradient;
import model.game.Corporation;
import view.assets.GameResources;

import java.awt.*;

/**
 * A pane which show a Tower.
 *
 * @author Arthur Deck
 * @version 1 
 */
public class GlowingItemCorp extends PanelGlowingGradient {
    private final Corporation corp;
    private final Image img;
    private final Color color;
    public GlowingItemCorp(Corporation corp) {
        super();
        this.corp = corp;
        this.img = GameResources.GImage.getCorpImage(this.corp);
        this.color = GameResources.getCorpColor(corp);

        setGradientColor1(color.darker());
        setGradientColor2(color.brighter());
        setBackground(color.darker());
        setBackgroundLight(color.brighter());
    }

    public Corporation getCorp() {
        return corp;
    }

    public Image getImg() {
        return img;
    }

    public Color getColor() {
        return color;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int bordShadSize = getShadowSize() + getBorderSize() + 5; // 5 for spacing
        g.drawImage(img, bordShadSize, bordShadSize,  getWidth() - 2*bordShadSize, getHeight() - 2*bordShadSize, null);
    }
}