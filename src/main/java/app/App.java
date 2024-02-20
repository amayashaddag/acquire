package app;

import java.util.List;
import java.util.LinkedList;

import control.GameController;
import model.Board;
import model.Player;

public class App {
    public static void main(String[] args) {
        Board board = new Board();
        List<Player> players = new LinkedList<>();
        Player p1 = new Player();
        players.add(p1);

        GameController controller = new GameController(board, null, players);
        controller.gameLoop();
    }
}
