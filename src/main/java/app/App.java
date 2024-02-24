package app;

import control.GameController;
import model.Player;

import java.util.LinkedList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        String pseudo = "amayas.haddag";
        Player player = Player.createHumanPlayer(pseudo);

        List<Player> listOfPlayers = new LinkedList<>();
        listOfPlayers.add(player);

        GameController controller = new GameController(listOfPlayers, player);
        controller.consoleGameLoop();
    }
}
