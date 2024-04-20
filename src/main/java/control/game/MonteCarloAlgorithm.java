package control.game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.game.Player;
import model.tools.Action;

/**
 * @author Nida HAMMOUCHE
 * @version 1.0
 */
public class MonteCarloAlgorithm {

    private final BotController botController;
    private final int numSimulations;

    public MonteCarloAlgorithm(BotController botController, int numSimulations) {
        this.botController = botController;
        this.numSimulations = numSimulations;
    }

    public Action runMonteCarlo() {

        Map<Action, Integer> actionScores = new HashMap<>();
        List<Action> possibleActions = botController.getPossibleActions();

        System.out.println(possibleActions);

        for (Action action : possibleActions) {
            int totalMoneyEarned = 0;

            for (int i = 0; i < numSimulations; i++) {
                BotController cloneController;
                try {
                    cloneController = (BotController) botController.clone();
                } catch (Exception e) {
                    return null;
                }
                Player currentPlayer = cloneController.getCurrentPlayer();
                cloneController.handlePlayerTurn(action, currentPlayer, false);
                cloneController.simulateGame();

                int moneyEarned = currentPlayer.getNet() - Player.INITIAL_CASH;
                totalMoneyEarned += moneyEarned;
            }

            int averageMoneyEarned = totalMoneyEarned / numSimulations; 
            actionScores.put(action, averageMoneyEarned);

        }

        System.out.println(actionScores);

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
