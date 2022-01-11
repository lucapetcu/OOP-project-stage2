package strategies;

import enums.CityStrategyEnum;

public final class StrategyFactory {
    private StrategyFactory() {

    }

    /**
     * factory method to get the strategy based on the string
     * @param strategy string which tells the strategy
     * @return strategy object corresponding to the string
     */
    public static GenericStrategy getStrategy(final CityStrategyEnum strategy) {
        return switch (strategy) {
            case ID -> new IdStrategy();
            case NICE_SCORE -> new NiceScoreStrategy();
            case NICE_SCORE_CITY -> new NiceScoreCityStrategy();
        };
    }
}
