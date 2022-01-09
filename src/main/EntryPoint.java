package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import common.Constants;
import entities.ConcreteVisitor;
import entities.Simulation;
import utils.InputParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Map;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.ArrayList;

public final class EntryPoint {
    /**
     * Main method which performs the simulation and writes the output array
     * @throws IOException
     */
    public void action() throws IOException {
        File testDirectory = new File(Constants.INPUT_PATH);
        Path outputDir = Paths.get(Constants.OUTPUT_DIR);

        /*
        Create the output directory
         */
        if (!Files.exists(outputDir)) {
            Files.createDirectories(outputDir);
        }

        File outputDirectory = new File(Constants.OUTPUT_DIR);
        for (File file : Objects.requireNonNull(outputDirectory.listFiles())) {
            file.delete();
        }

        for (File file : Objects.requireNonNull(testDirectory.listFiles())) {
            String inputFileName = Constants.INPUT_PATH + file.getName();
            /*
            Filter the input file name
             */
            String number = "";
            for (int i = Constants.STARTING_POSITION; i < file.getName().length(); i++) {
                if (file.getName().charAt(i) == '.') {
                    break;
                }
                number += file.getName().charAt(i);
            }
            String outputFileName = Constants.OUTPUT_PATH + number + Constants.FILE_EXTENSION;
            File outputFile = new File(outputFileName);
            boolean out = outputFile.createNewFile();
            FileWriter outFile = new FileWriter(outputFileName);
            if (out) {
                InputParser inputParser = InputParser.getInstance(inputFileName);
                Simulation simulation = inputParser.simulationLoader();
                ConcreteVisitor visitor = new ConcreteVisitor();
                List<Map<String, Object>> arrayResult = new ArrayList<>();
                simulation.simulate(visitor, arrayResult);
                ObjectMapper mapper = new ObjectMapper();
                Map<String, List<Map<String, Object>>> result = new LinkedHashMap<>();
                result.put(Constants.ANNUAL_CHILDREN, arrayResult);
                mapper.enable(SerializationFeature.INDENT_OUTPUT);
                String output = mapper.writeValueAsString(result);
                outFile.write(output);
                outFile.flush();
                outFile.close();
            }
        }
    }
}
