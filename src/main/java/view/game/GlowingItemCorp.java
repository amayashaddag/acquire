package view.game;

import javaswingdev.pggb.PanelGlowingGradient;
import model.Corporation;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GlowingItemCorp extends PanelGlowingGradient {
    private final Corporation corp;
    private final Image img;
    public GlowingItemCorp(Corporation corp) {
        super();
        this.corp = corp;
        this.img = Ressources.Assets.getCorpImage(this.corp);


        BufferedImage bfi = Ressources.imageToBufferedImage(img);
        int color = bfi.getRGB(img.getWidth(null)/2,img.getHeight(null)* 3/4);
        setGradientColor1(new Color(color, true).darker());
        setGradientColor2(new Color(color, true).brighter());
        setBackground(new Color(color,true).darker());
        setBackgroundLight(new Color(color,true).brighter());
    }

    public Corporation getCorp() {
        return corp;
    }

    public Image getImg() {
        return img;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int bordShadSize = getShadowSize() + getBorderSize() + 5; // 5 for spacing
        g.drawImage(img, bordShadSize, bordShadSize,  getWidth() - 2*bordShadSize, getHeight() - 2*bordShadSize, null);
    }
}