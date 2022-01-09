package strategies;

import common.Constants;
import entities.Child;
import entities.Gift;
import entities.Simulation;
import enums.Category;
import enums.ElvesType;
import utils.Utils;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public final class NiceScoreStrategy implements GenericStrategy {
    @Override
    public List<Map<String, Object>> assignGifts(final Simulation simulation,
                                                 final Double budgetUnit) {
        Map<Integer, List<Gift>> childrenGifts = new HashMap<>();
        List<Child> children = new ArrayList<>();
        /*Sort the children by age*/
        for (Child child : simulation.getChildren()) {
            children.add(new Child(child));
        }
        children.sort((o1, o2) -> {
            Double average1 = Utils.calculateAverage(o1);
            Double average2 = Utils.calculateAverage(o2);
            /*Sort by average, then by ID*/
            if (average1.compareTo(average2) != 0) {
                return -average1.compareTo(average2);
            }
            Integer id1 = o1.getId();
            Integer id2 = o2.getId();
            return id1.compareTo(id2);
        });

        /*Assigning gifts to the sorted children*/
        for (Child child : children) {
            Double averageScore = Utils.calculateAverage(child);
            Double childBudget = averageScore * budgetUnit;
            if (child.getElf().equals(ElvesType.BLACK)) {
                childBudget = childBudget - childBudget * Constants.THIRTY / Constants.HUNDRED;
            } else if (child.getElf().equals(ElvesType.PINK)) {
                childBudget = childBudget + childBudget * Constants.THIRTY / Constants.HUNDRED;
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

        return Utils.buildOutput(simulation, childrenGifts, budgetUnit);
    }
}
