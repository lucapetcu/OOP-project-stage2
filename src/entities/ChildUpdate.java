package entities;


import enums.Category;
import enums.ElvesType;

import java.util.List;

public final class ChildUpdate {
    private int id;
    private Double newNiceScore;
    private List<Category> newGiftPreferences;
    private ElvesType newElf;

    public ChildUpdate(final int id, final Double newNiceScore,
                       final List<Category> newGiftPreferences,
                       final ElvesType newElf) {
        this.id = id;
        this.newNiceScore = newNiceScore;
        this.newGiftPreferences = newGiftPreferences;
        this.newElf = newElf;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public Double getNewNiceScore() {
        return newNiceScore;
    }

    public List<Category> getNewGiftPreferences() {
        return newGiftPreferences;
    }

    public ElvesType getNewElf() {
        return newElf;
    }

    public void setNewElf(final ElvesType newElf) {
        this.newElf = newElf;
    }
}
