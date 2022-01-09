package utils;


import common.Constants;

import java.util.List;

public final class AverageScoreCalculator {
    private List<Double> niceScore;
    private Double bonusScore;

    private AverageScoreCalculator(final List<Double> niceScore, final Double bonusScore) {
        this.niceScore = niceScore;
        this.bonusScore = bonusScore;
    }

    public final static class Builder {
        private List<Double> niceScore; //mandatory
        private int age; //mandatory
        private Double bonusScore; //optional
        private Double averageScore; //helper parameter to keep track of the averageScore

        public Builder(final List<Double> niceScore, final int age) {
            this.niceScore = niceScore;
            this.age = age;
        }

        /**
         * Calculates initial average score for the child
         * @return updated Builder object
         */
        public Builder calculateAverageScore() {
            if (age < Constants.FIVE) {
                this.averageScore = Constants.TEN;
            } else if (age < Constants.TWELVE) {
                Double sum = 0.0;
                Double cnt = 0.0;
                for (Double d : niceScore) {
                    sum += d;
                    cnt++;
                }
                this.averageScore = sum / cnt;
            } else {
                Double weight = 0.0;
                Double sum = 0.0;
                for (int i = 0; i < niceScore.size(); i++) {
                    weight += (i + 1);
                    sum += (i + 1) * niceScore.get(i);
                }
                this.averageScore = sum / weight;
            }
            return this;
        }

        /**
         * Sets the bonus score
         * @param score bonus score of the child
         * @return updated Builder object
         */
        public Builder setBonusScore(final Double score) {
            this.bonusScore = score;
            return this;
        }

        /**
         * Adds the bonus score to the average
         * @return updated Builder object
         */
        public Builder addBonusScore() {
            averageScore += averageScore * bonusScore / Constants.HUNDRED;
            averageScore = averageScore > Constants.TEN ? Constants.TEN : averageScore;
            return this;
        }

        /**
         * Outputs the final average
         * @return final average score for the child
         */
        public Double build() {
            return this.averageScore;
        }
    }

    public List<Double> getNiceScore() {
        return niceScore;
    }
}
