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

        System.out.println(possibleActions.size());

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


        Action best = chooseBestAction(actionScores);
        return best;
    }

    private Action chooseBestAction(Map<Action, Integer> actionScores) {
        Map.Entry<Action, Integer> entry = actionScores.entrySet().iterator().next();
        Action bestAction = entry.getKey();
        int bestScore = entry.getValue();

        for (Map.Entry<Action, Integer> e : actionScores.entrySet()) {
            if (e.getValue() > bestScore) {
                bestAction = e.getKey();
                bestScore = e.getValue();
            }
        }

        return bestAction;
    }
}
