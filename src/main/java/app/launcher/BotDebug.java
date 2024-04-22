package app.launcher;

import java.util.LinkedList;
import java.util.List;

import control.game.BotController;
import control.game.GameController;
import control.game.MonteCarloAlgorithm;
import model.game.Player;

public class BotDebug {
    public static void main(String[] args) throws CloneNotSupportedException {
        Player bot1 = Player.createBotPlayer();
        Player bot2 = Player.createBotPlayer();

        List<Player> players = new LinkedList<>();
        players.add(bot1);
        players.add(bot2);

        GameController controller = new GameController(players, bot1, null, false);
        BotController botController = new BotController(controller);
        MonteCarloAlgorithm monteCarlo = new MonteCarloAlgorithm(botController, 100);

        System.out.println(monteCarlo.runMonteCarlo());
    }
}
