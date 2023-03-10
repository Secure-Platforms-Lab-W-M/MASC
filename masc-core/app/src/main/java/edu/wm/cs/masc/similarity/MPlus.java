package edu.wm.cs.masc.similarity;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import edu.wm.cs.masc.similarity.model.MutationType;
import edu.wm.cs.masc.similarity.model.location.MutationLocation;
import edu.wm.cs.masc.similarity.operators.OperatorBundle;
import edu.wm.cs.masc.similarity.processors.MutationsProcessor;
import edu.wm.cs.masc.similarity.processors.SourceCodeProcessor;
import edu.wm.cs.masc.similarity.processors.TextBasedDetectionsProcessor;
import edu.wm.cs.masc.similarity.detectors.MutationLocationDetector;
import edu.wm.cs.masc.similarity.detectors.MutationLocationListBuilder;
import edu.wm.cs.masc.similarity.helper.PlacementChecker;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MPlus {

    private static Logger logger = LogManager.getLogger(MPlus.class);
    public static void main(String[] args) {
        DateTimeFormatter dtf = DateTimeFormatter
                .ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        try {
            runMPlus(args);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        LocalDateTime then = LocalDateTime.now();
        logger.trace(dtf.format(now));
        logger.trace(dtf.format(then));
    }

    public static void runMPlus(String[] args) throws IOException {

        //Usage Error
        if (args.length != 6) {
            logger.trace("******* ERROR: INCORRECT USAGE *******");
            logger.trace("Argument List:");
            logger.trace("1. Binaries path");
            logger.trace("2. App Source Code path");
            logger.trace("3. App Name");
            logger.trace("4. Mutants path");
            logger.trace("5. Directory containing the operator.properties file");
            logger.trace("6. Multithread generation (true/false)");
            return;
        }

        String binariesFolder = args[0];
        String rootPath = args[1];
        String appName = args[2];
        String mutantsFolder = args[3];
        String operatorsDir = args[4];
        boolean multithread = Boolean.parseBoolean(args[5]);

        //Read selected operators
        OperatorBundle operatorBundle = new OperatorBundle(operatorsDir);
        logger.info(operatorBundle.printSelectedOperators());


        //Text-Based operators selected
        List<MutationLocationDetector> textBasedDetectors = operatorBundle.getTextBasedDetectors();

        //1. Run detection phase for Text-based detectors
        HashMap<MutationType, List<MutationLocation>> locations = TextBasedDetectionsProcessor.process(rootPath, textBasedDetectors);

        Set<MutationType> keys = locations.keySet();
        logger.trace("[Tax based detector] "+keys);
        List<MutationLocation> list = null;
        for (MutationType mutationType : keys) {
            list = locations.get(mutationType);
            for (MutationLocation mutationLocation : list) {
                logger.trace("File: " + mutationLocation
                        .getFilePath() + ", start line:" + mutationLocation
                        .getStartLine() + ", end line: " + mutationLocation
                        .getEndLine() + ", start column" + mutationLocation
                        .getStartColumn());
            }
        }

        //2. Run detection phase for AST-based detectors
        //2.1 Preprocessing: Find locations to target API calls (including
        // calls to constructors)
        //SourceCodeProcessor scp = SourceCodeProcessor.getInstance(); (not
        // safe, if MPlus is executed on different apps)
        SourceCodeProcessor scp = new SourceCodeProcessor(operatorBundle);
        locations.putAll(scp.processFolder(rootPath, binariesFolder, appName));

        //2.2. Call the detectors on each location in order to find any extra
        // information required for each case.
        locations = scp.findExtraInfoRequired(locations);

        //3. Build MutationLocation List
        List<MutationLocation> mutationLocationList =
                MutationLocationListBuilder
                .buildList(locations);
        logger.trace("Total Locations: " + mutationLocationList.size());

        //3. Run mutation phase
        MutationsProcessor mProcessor = new MutationsProcessor(rootPath,
                appName, mutantsFolder);

        if (multithread) {
            mProcessor.processMultithreaded(mutationLocationList);
        } else {
            mProcessor.process(mutationLocationList);
        }

        // 4. minimal JDT-AST based location reachability check
        PlacementChecker checker = new PlacementChecker(rootPath,
                binariesFolder);
        checker.process(mutationLocationList);


    }

}
