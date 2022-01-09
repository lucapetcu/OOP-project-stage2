package utils;

import common.Constants;
import entities.Child;
import entities.Gift;
import entities.AnnualChange;
import entities.Simulation;
import entities.ChildUpdate;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class InputParser {
    private static String inputFilePath;
    private static InputParser instance = null;

    private InputParser(final String inputFilePath) {
        InputParser.inputFilePath = inputFilePath;
    }

    /**
     * Method used to provide the single instance of this class. It also updates
     * the inputFilePath if the instance is already not null.
     * @param inputFile name of the file to be parsed
     * @return Singleton instance of the class
     */
    public static InputParser getInstance(final String inputFile) {
        if (instance == null) {
            instance = new InputParser(inputFile);
        } else {
            inputFilePath = inputFile;
        }
        return instance;
    }

    /**
     * Parses the JSON input file
     * @return a simulation object
     */
    public Simulation simulationLoader() {
        List<Child> children = new ArrayList<>();
        List<Gift> gifts = new ArrayList<>();
        List<AnnualChange> annualChanges = new ArrayList<>();
        int numberOfYears = 0;
        Double initialBudget = 0.0;
        try {
            JSONParser jsonParser = new JSONParser();

            JSONObject jsonObject = (JSONObject)
                    jsonParser.parse(new FileReader(this.inputFilePath));
            JSONObject jsonInitialData = (JSONObject) jsonObject.get(Constants.INITIAL_DATA);
            JSONArray jsonChildren = (JSONArray) jsonInitialData.get(Constants.CHILDREN);
            JSONArray jsonGifts = (JSONArray) jsonInitialData.get(Constants.GIFT_LIST);
            JSONArray jsonAnnualChanges = (JSONArray) jsonObject.get(Constants.ANNUAL_CHANGES);

            numberOfYears = Integer.parseInt(jsonObject.get(Constants.NUMBER_OF_YEARS).toString());
            initialBudget = Double.parseDouble(jsonObject.get(Constants.SANTA_BUDGET).toString());

            if (jsonChildren != null) {
                for (Object jsonChild : jsonChildren) {
                    List<Double> niceScore = new ArrayList<>();
                    niceScore.add(Double.parseDouble(((JSONObject) jsonChild)
                            .get(Constants.NICE_SCORE).toString()));
                    children.add(new Child(Integer
                            .parseInt(((JSONObject) jsonChild).get(Constants.ID).toString()),
                            (String) ((JSONObject) jsonChild).get(Constants.LAST_NAME),
                            (String) ((JSONObject) jsonChild).get(Constants.FIRST_NAME),
                            Integer.parseInt(
                                    ((JSONObject) jsonChild).get(Constants.AGE).toString()),
                            Utils.stringToCities(
                                    (String) ((JSONObject) jsonChild).get(Constants.CITY)),
                            niceScore,
                            Utils.arrayToCategory((JSONArray) ((JSONObject) jsonChild)
                                    .get(Constants.GIFTS_PREFERENCES)),
                            Double.parseDouble(((JSONObject) jsonChild)
                                    .get(Constants.NICE_SCORE_BONUS).toString()),
                            Utils.stringToElf((String) ((JSONObject) jsonChild).get(Constants.ELF))
                    ));
                }
            }

            if (jsonGifts != null) {
                for (Object jsonGift : jsonGifts) {
                    gifts.add(new Gift((String) ((JSONObject) jsonGift).get(Constants.PRODUCT_NAME),
                            Double.parseDouble(
                                    ((JSONObject) jsonGift).get(Constants.PRICE).toString()),
                            Utils.stringToCategory(
                                    (String) ((JSONObject) jsonGift).get(Constants.CATEGORY)),
                            Integer.parseInt(((JSONObject) jsonGift)
                                    .get(Constants.QUANTITY).toString())
                    ));
                }
            }

            if (jsonAnnualChanges != null) {
                for (Object jsonAnnualChange : jsonAnnualChanges) {
                    Double newSantaBudget = Double.parseDouble(((JSONObject) jsonAnnualChange)
                            .get(Constants.NEW_SANTA_BUDGET).toString());
                    String strategy = (String) ((JSONObject) jsonAnnualChange)
                            .get(Constants.STRATEGY);
                    List<Gift> newGifts = new ArrayList<>();
                    List<Child> newChildren = new ArrayList<>();
                    List<ChildUpdate> childUpdates = new ArrayList<>();

                    JSONArray jsonNewGifts = (JSONArray) ((JSONObject) jsonAnnualChange)
                            .get(Constants.NEW_GIFTS);
                    JSONArray jsonNewChildren = (JSONArray) ((JSONObject) jsonAnnualChange)
                            .get(Constants.NEW_CHILDREN);
                    JSONArray jsonChildrenUpdates = (JSONArray) ((JSONObject) jsonAnnualChange)
                            .get(Constants.CHILDREN_UPDATES);

                    if (jsonNewGifts != null) {
                        for (Object jsonNewGift : jsonNewGifts) {
                            newGifts.add(new Gift((String) ((JSONObject) jsonNewGift)
                                    .get(Constants.PRODUCT_NAME),
                                    Double.parseDouble(((JSONObject) jsonNewGift)
                                            .get(Constants.PRICE).toString()),
                                    Utils.stringToCategory(
                                            (String) ((JSONObject) jsonNewGift)
                                                    .get(Constants.CATEGORY)),
                                    Integer.parseInt(((JSONObject) jsonNewGift)
                                            .get(Constants.QUANTITY).toString())
                            ));
                        }
                    }

                    if (jsonNewChildren != null) {
                        for (Object jsonNewChild : jsonNewChildren) {
                            List<Double> niceScore = new ArrayList<>();
                            niceScore.add(Double.parseDouble(((JSONObject) jsonNewChild)
                                    .get(Constants.NICE_SCORE).toString()));
                            newChildren.add(new Child(Integer.parseInt(((JSONObject) jsonNewChild)
                                    .get(Constants.ID).toString()),
                                    (String) ((JSONObject) jsonNewChild).get(Constants.LAST_NAME),
                                    (String) ((JSONObject) jsonNewChild).get(Constants.FIRST_NAME),
                                    Integer.parseInt(((JSONObject) jsonNewChild)
                                            .get(Constants.AGE).toString()),
                                    Utils.stringToCities(
                                            (String) ((JSONObject) jsonNewChild)
                                                    .get(Constants.CITY)),
                                    niceScore,
                                    Utils.arrayToCategory((JSONArray) ((JSONObject) jsonNewChild)
                                            .get(Constants.GIFTS_PREFERENCES)),
                                    Double.parseDouble(((JSONObject) jsonNewChild)
                                            .get(Constants.NICE_SCORE_BONUS).toString()),
                                    Utils.stringToElf((String) ((JSONObject) jsonNewChild)
                                            .get(Constants.ELF))
                            ));
                        }
                    }

                    if (jsonChildrenUpdates != null) {
                        for (Object jsonChildUpdate : jsonChildrenUpdates) {
                            Double newNiceScore = -1.0;
                            if (((JSONObject) jsonChildUpdate).get(Constants.NICE_SCORE) != null) {
                                newNiceScore = Double.parseDouble(((JSONObject) jsonChildUpdate)
                                        .get(Constants.NICE_SCORE).toString());
                            }
                            childUpdates.add(new ChildUpdate(Integer
                                    .parseInt(((JSONObject) jsonChildUpdate)
                                            .get(Constants.ID).toString()),
                                    newNiceScore,
                                    Utils.arrayToCategory((JSONArray) ((JSONObject) jsonChildUpdate)
                                            .get(Constants.GIFTS_PREFERENCES)),
                                    Utils.stringToElf((String) ((JSONObject) jsonChildUpdate)
                                            .get(Constants.ELF))
                            ));
                        }
                    }

                    annualChanges.add(new AnnualChange(newSantaBudget, newGifts,
                            newChildren, childUpdates, strategy));
                }
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return new Simulation(children, gifts, annualChanges, numberOfYears, initialBudget);
    }
}
