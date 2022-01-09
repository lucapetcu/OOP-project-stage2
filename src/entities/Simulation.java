package entities;

import java.util.List;
import java.util.Map;

public final class Simulation {
    private Double budget;
    private List<Child> children;
    private List<Gift> gifts;
    private List<AnnualChange> annualChanges;
    private int numberOfYears;

    public Simulation(final List<Child> children, final List<Gift> gifts,
                      final List<AnnualChange> annualChanges,
                      final int numberOfYears, final Double budget) {
        this.children = children;
        this.gifts = gifts;
        this.annualChanges = annualChanges;
        this.numberOfYears = numberOfYears;
        this.budget = budget;
    }


    public Double getBudget() {
        return budget;
    }

    public void setBudget(final Double budget) {
        this.budget = budget;
    }

    public List<Child> getChildren() {
        return children;
    }

    public void setChildren(final List<Child> children) {
        this.children = children;
    }

    public List<Gift> getGifts() {
        return gifts;
    }

    public void setGifts(final List<Gift> gifts) {
        this.gifts = gifts;
    }

    public List<AnnualChange> getAnnualChanges() {
        return annualChanges;
    }

    public void setAnnualChanges(final List<AnnualChange> annualChanges) {
        this.annualChanges = annualChanges;
    }

    public long getNumberOfYears() {
        return numberOfYears;
    }

    public void setNumberOfYears(final int numberOfYears) {
        this.numberOfYears = numberOfYears;
    }

    /**
     * Accept method in Visitor pattern
     * @param visitor visitor object
     * @param arrayResult array which stores the output
     */
    public void simulate(final VisitorInterface visitor,
                         final List<Map<String, Object>> arrayResult) {
        visitor.visit(this, arrayResult);
    }
}
