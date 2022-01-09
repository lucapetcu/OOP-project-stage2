package strategies;

import common.Constants;
import entities.Child;
import entities.Gift;
import entities.Simulation;
import enums.Category;
import enums.Cities;
import enums.ElvesType;
import utils.Utils;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public final class NiceScoreCityStrategy implements GenericStrategy {
    @Override
    public List<Map<String, Object>> assignGifts(final Simulation simulation,
                                                 final Double budgetUnit) {
        List<Cities> cities = new ArrayList<>();
        for (Child child : simulation.getChildren()) {
            if (!cities.contains(child.getCity())) {
                cities.add(child.getCity());
            }
        }
        cities.sort((o1, o2) -> {
            Double sum1 = 0.0;
            Double cnt = 0.0;
            for (Child child : simulation.getChildren()) {
                if (child.getCity().equals(o1)) {
                    sum1 += Utils.calculateAverage(child);
                    cnt++;
                }
            }
            Double average1 = sum1 / cnt;
            cnt = 0.0;
            Double sum2 = 0.0;
            for (Child child : simulation.getChildren()) {
                if (child.getCity().equals(o2)) {
                    sum2 += Utils.calculateAverage(child);
                    cnt++;
                }
            }
            Double average2 = sum2 / cnt;
            if (average1.compareTo(average2) != 0) {
                return -average1.compareTo(average2);
            }
            String city1 = o1.name();
            String city2 = o2.name();
            return city1.compareTo(city2);
        });
        /*Assigning gifts*/
        Map<Integer, List<Gift>> childrenGifts = new HashMap<>();
        for (Cities city : cities) {
            for (Child child : simulation.getChildren()) {
                if (child.getCity().equals(city)) {
                    Double averageScore = Utils.calculateAverage(child);
                    Double childBudget = averageScore * budgetUnit;
                    if (child.getElf().equals(ElvesType.BLACK)) {
                        childBudget = childBudget
                                - childBudget * Constants.THIRTY / Constants.HUNDRED;
                    } else if (child.getElf().equals(ElvesType.PINK)) {
                        childBudget = childBudget
                                + childBudget * Constants.THIRTY / Constants.HUNDRED;
                    }
                    List<Gift> receivedGifts = new ArrayList<>();
                    Double prices = 0.0;
                    for (Category preference : child.getGiftPreferences()) {
                        Double minPrice = Double.MAX_VALUE;
                        Gift crtGift = null;
                        for (Gift gift : simulation.getGifts()) {
                            if (prices >= childBudget) {
                                break;
                            }
                            if (gift.getCategory().equals(preference)
                                    && gift.getQuantity() > 0
                                    && gift.getPrice() < minPrice
                                    && gift.getPrice() + prices < childBudget) {
                                minPrice = gift.getPrice();
                                crtGift = gift;
                            }
                        }
                        if (crtGift != null) {
                            receivedGifts.add(crtGift);
                            prices += crtGift.getPrice();
                            crtGift.setQuantity(crtGift.getQuantity() - 1);
                        }
                    }
                    childrenGifts.put(child.getId(), receivedGifts);
                }
            }
        }

        return Utils.buildOutput(simulation, childrenGifts, budgetUnit);
    }
}
