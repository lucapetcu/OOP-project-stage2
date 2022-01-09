package strategies;

public final class StrategyFactory {
    private StrategyFactory() {

    }

    /**
     * factory method to get the strategy based on the string
     * @param strategy string which tells the strategy
     * @return strategy object corresponding to the string
     */
    public static GenericStrategy getStrategy(final String strategy) {
        return switch (strategy) {
          case "id" -> new IdStrategy();
          case "niceScore" -> new NiceScoreStrategy();
          case "niceScoreCity" -> new NiceScoreCityStrategy();
          default -> null;
        };
    }
}
