package entities;


import common.Constants;
import utils.Utils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public final class ConcreteVisitor implements VisitorInterface {
    @Override
    public void visit(final Simulation simulation, final List<Map<String, Object>> arrayResult) {
        /* First round simulation */
        simulation.getChildren().removeIf(child -> child.getAge() > Constants.EIGHTEEN);
        List<Map<String, Object>> childrenList = Utils.simulateRound(simulation, "id");
        Map<String, Object> firstRound = new LinkedHashMap<>();
        firstRound.put(Constants.CHILDREN, childrenList);
        arrayResult.add(firstRound);

        for (int cnt = 0; cnt < simulation.getNumberOfYears(); cnt++) {
            List<Map<String, Object>> currentRound;
            for (Child child : simulation.getChildren()) {
                child.setAge(child.getAge() + 1);
            }
            simulation.getChildren().removeIf(child -> child.getAge() > Constants.EIGHTEEN);
            AnnualChange annualChange = simulation.getAnnualChanges().get(cnt);
            /* Update existing children */
            for (ChildUpdate childUpdate : annualChange.getChildUpdates()) {
                for (Child child : simulation.getChildren()) {
                    if (child.getId() == childUpdate.getId()) {
                        if (childUpdate.getNewNiceScore() != -1) {
                            child.getNiceScore().add(childUpdate.getNewNiceScore());
                        }
                        for (int i = childUpdate.getNewGiftPreferences().size() - 1; i >= 0; i--) {
                            int finalI = i;
                            child.getGiftPreferences().removeIf(pref -> pref
                                    .equals(childUpdate.getNewGiftPreferences()
                                            .get(finalI)));
                            child.getGiftPreferences().add(0, childUpdate
                                    .getNewGiftPreferences().get(i));
                        }
                        child.setElf(childUpdate.getNewElf());
                        break;
                    }
                }
            }
            /* Add new children to the list */
            for (Child child : annualChange.getNewChildren()) {
                if (child.getAge() <= Constants.EIGHTEEN) {
                    simulation.getChildren().add(child);
                }
            }
            /* Add new gifts */
            for (Gift gift : annualChange.getNewGifts()) {
                simulation.getGifts().add(gift);
            }
            /* Update budget */
            simulation.setBudget(annualChange.getNewSantaBudget());

            /* Round simulation */
            currentRound = Utils.simulateRound(simulation, annualChange.getStrategy());

            /* Add the results to the output array */
            Map<String, Object> round = new LinkedHashMap<>();
            round.put(Constants.CHILDREN, currentRound);
            arrayResult.add(round);
        }
    }
}
