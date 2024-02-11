package view.game;

/**
 * @author Arthur Deck
 * @version 0.1
 */
public enum GameString {
    
    RESSOURCES_PATH("ressources/"),
    IMAGES_PATH("images/game/");




    GameString(String s) {msg = s;}
    private String msg;
    @Override
    public String toString() {return msg;}
}
