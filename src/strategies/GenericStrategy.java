package strategies;

import entities.Simulation;

import java.util.List;
import java.util.Map;

public interface GenericStrategy {
    /**
     * Template method to implement the strategies
     * @param simulation object that holds the data
     * @param budgetUnit budget unit assigned for this round; previously computed in simulateRound
     * @return list o children information based on the strategy
     */
    List<Map<String, Object>> assignGifts(Simulation simulation, Double budgetUnit);
}
