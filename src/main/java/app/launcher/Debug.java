package app.launcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.game.Corporation;

public class Debug {
        public static List<Map<Corporation, Integer>> generateCombinations(Map<Corporation, Integer> inputMap,
                        int threshold) {
                List<Map<Corporation, Integer>> combinations = new ArrayList<>();
                generateCombinationsHelper(inputMap, threshold, new HashMap<>(), combinations);
                return combinations;
        }

        private static void generateCombinationsHelper(Map<Corporation, Integer> inputMap, int threshold,
                        Map<Corporation, Integer> current, List<Map<Corporation, Integer>> combinations) {
                int sum = current.values().stream().mapToInt(Integer::intValue).sum();
                if (sum > threshold) {
                        return;
                }

                if (!current.isEmpty()) {
                        combinations.add(new HashMap<>(current)); // Add a copy of the current combination
                }

                for (Map.Entry<Corporation, Integer> entry : inputMap.entrySet()) {
                        Corporation key = entry.getKey();
                        int value = entry.getValue();

                        if (!current.containsKey(key)) {
                                current.put(key, value);
                                generateCombinationsHelper(inputMap, threshold, current, combinations);
                                current.remove(key);
                        }
                }
        }

        // Example usage
        public static void main(String[] args) {
                Map<Corporation, Integer> inputMap = new HashMap<>();
                inputMap.put(Corporation.IMPERIAL, 1);
                inputMap.put(Corporation.TOWER, 2);
                inputMap.put(Corporation.AMERICAN, 1);

                List<Map<Corporation, Integer>> combinations = generateCombinations(inputMap, 3);
                for (Map<Corporation, Integer> combination : combinations) {
                        System.out.println(combination);
                }
        }
}