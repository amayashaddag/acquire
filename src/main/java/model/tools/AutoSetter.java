package model.tools;

import java.lang.annotation.*;

/**
 * <p>For graphics components wich auto
 * set on a <? extends JPanel> </p>
 * 
 * @apiNote Fo an example of utilisation see {@link view.game.JetonsPanel}
 * @apiNote All class taged
 * must implement a public YourClass(typeParam g)
 * constructor
 * @author Arthur Deck
 * @version 1
*/
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface AutoSetter {
    Class<? extends javax.swing.JPanel> typeParam();
}
