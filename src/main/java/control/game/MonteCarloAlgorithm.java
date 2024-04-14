package control.game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.game.Player;
import model.tools.Action;

public class MonteCarloAlgorithm {

    private final BotController botController;
    private final static int NUM_SIMULATIONS = 1000;

    public MonteCarloAlgorithm(BotController botController, int numSimulations) {
        this.botController = botController;
    }

    public Action runMonteCarlo(Player currentPlayer) {
        Map<Action, Integer> actionScores = new HashMap<>();

        List<Action> possibleActions = botController.getPossibleActions();
        for (Action action : possibleActions) {
            int totalMoneyEarned = 0;

            for (int i = 0; i < NUM_SIMULATIONS; i++) {
                // BotController cloneController = botController.cloneController();
                // cloneController.handlePlayerTurn(action, currentPlayer);
                // cloneController.simulateGame();

                int moneyEarned = currentPlayer.getNet() - Player.INITIAL_CASH;
                totalMoneyEarned += moneyEarned;
            }

            int averageMoneyEarned = totalMoneyEarned / NUM_SIMULATIONS;
            // Action currentAction = new Action(action, null, null); 
            // actionScores.put(currentAction, averageMoneyEarned);
        }

        return chooseBestAction(actionScores);
    }

    private Action chooseBestAction(Map<Action, Integer> actionScores) {
        Action bestAction = null;
        int bestScore = Integer.MIN_VALUE;
        for (Map.Entry<Action, Integer> entry : actionScores.entrySet()) {
            if (entry.getValue() > bestScore) {
                bestAction = entry.getKey();
                bestScore = entry.getValue();
            }
        }
        return bestAction;
    }
}
