package view.game.annotations;

import java.lang.annotation.*;

/**
 * <p>For graphics components wich auto
 * set on a {@link view.GameView} </p>
 * 
 * @apiNote Fo an example of utilisation see {@link view.JetonsPanel}
 * @apiNote All class taged
 * must implement a public YourClass(GameView g) 
 * constructor
 * @author Arthur Deck
 * @version 1
*/
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface AutoSetter {
}
