package edu.wm.cs.masc;

import edu.wm.cs.masc.plugins.MutationMakerForPluginOperators;

import edu.wm.cs.masc.automatedAnalysis.ResultAnalyzer;
import edu.wm.cs.masc.similarity.MPlus;
import edu.wm.cs.masc.mainScope.mutationmakers.*;
import edu.wm.cs.masc.similarity.processors.SourceCodeProcessor;
import edu.wm.cs.masc.utils.config.PropertiesReader;
import edu.wm.cs.masc.mutation.operators.RootOperatorType;
import edu.wm.cs.masc.mutation.properties.*;
import edu.wm.cs.masc.exhaustive.MuseRunner;
import edu.wm.cs.masc.logger.LocalLogger;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.eclipse.jface.text.BadLocationException;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MASC {

    private static Logger logger = LogManager.getLogger(MASC.class);

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            logger.trace("No properties file supplied");
            System.exit(10);
        } else if (args.length > 1) {
            logger.trace("Too many arguments were provided");
            System.exit(20);
        } else if (!args[0].endsWith(".properties")) {
            logger.trace("Properties file must end with the .properties extension");
            System.exit(30);
        } else {
            String path = args[0];
            try {
                runMain(path);
            } catch (ConfigurationException e) {
                System.out.printf("Filed to load the properties file %s", path);
            }
        }
    }

    public static void runResultAnalysis(PropertiesReader propertiesReader) throws ConfigurationException {
        try {
            ResultAnalyzer resultAnalyzer = new ResultAnalyzer(propertiesReader);
            resultAnalyzer.runAnalysis();
        } catch (ConfigurationException e) {
            logger.trace(e.getMessage());
            logger.trace("Skipping automated analysis...");
        }
    }

    public static void runMain(String path) throws IOException, BadLocationException, ConfigurationException {
        PropertiesReader reader = new PropertiesReader(path);
        String scope = reader.getValueForAKey("scope");

        // MASC Exhaustive Layer
        if (scope.equalsIgnoreCase("EXHAUSTIVE")) {
            logger.trace("Exhaustive scope selected");
            runExhaustiveScope(reader);
        }
        // MASC Selective Layer
        else if (scope.equalsIgnoreCase("SELECTIVE")) {
            logger.trace("Similarity scope selected");
            runSelectiveScope(reader);
        }
        // MASC MainScope
        else if (scope.equalsIgnoreCase("MAIN")) {
            logger.trace("Main Scope Selected");
            if (reader.contains("mutantGeneration") && reader.getValueForAKey("mutantGeneration").equalsIgnoreCase("true")){
                logger.trace("running mutation maker for main scope");
                runMainScope(reader, path);
            }
            else {
                logger.trace("Skipping mutant generation");
            }
            if (reader.contains("automatedAnalysis") && reader.getValueForAKey("automatedAnalysis").equalsIgnoreCase("true")){
                logger.trace("running result analysis for main scope");
                runResultAnalysis(reader);
            }
            else {
                logger.trace("Skipping automated analysis...");
            }

        } else {
            logger.trace("Unknown Scope: " + scope);
        }
    }

    public static void runSelectiveScope(PropertiesReader reader) throws IOException {
        File lib4ast = new File("libs4ast/");
        File opDir = new File("app/src/main/resources");
        logger.trace("running mutation for similarity scope");
        String[] args = {lib4ast.getAbsolutePath(),
                reader.getValueForAKey("appSrc"),
                reader.getValueForAKey("appName"),
                reader.getValueForAKey("outputDir"),
                opDir.getAbsolutePath(),
                "false"}; // Hardcode this because it never changes in MASC
        MPlus.runMPlus(args);
        
    }

    public static void runExhaustiveScope(PropertiesReader reader) throws
            IOException, BadLocationException {
//        StringOperatorProperties p = new StringOperatorProperties(
//                "Cipher.properties");
//        StringOperatorMutationMaker mutationMaker = new StringOperatorMutationMaker(p);
        MuseRunner.setUpMuse(reader);
//        mutationMaker.populateOperators();
//        MuseRunner.runMuse(mutationMaker.operators);
        MuseRunner.runMuse();
    }

    public static void runMainScope(PropertiesReader reader, String path) throws ConfigurationException {
        String type = reader.getValueForAKey("type");
        // Special logs for front end traversing
        LocalLogger.getLocalLogger().severe("[ValueForAKey]#" + type);
        AMutationMaker m = null;
        AOperatorProperties p;
        MutationMakerForPluginOperators pluginOperatorsMutationMaker = new MutationMakerForPluginOperators(path);

        if (type.equalsIgnoreCase(RootOperatorType.IntOperator.name())) {
            logger.trace("Selected Operator type "+ RootOperatorType.IntOperator.name());
            p = new IntOperatorProperties(path);
            m = new IntMutationMaker((IntOperatorProperties) p);
        } else if (type.equalsIgnoreCase(RootOperatorType.StringOperator.name())) {
            logger.trace("Selected Operator type "+ RootOperatorType.StringOperator.name());
            p = new StringOperatorProperties(path);
            m = new StringOperatorMutationMaker((StringOperatorProperties) p);
        } else if (type.equalsIgnoreCase(RootOperatorType.ByteOperator.name())) {
            logger.trace("Selected Operator type "+ RootOperatorType.ByteOperator.name());
            p = new ByteOperatorProperties(path);
            m = new ByteMutationMaker((ByteOperatorProperties) p);
        } else if (type.equalsIgnoreCase(RootOperatorType.Interproc.name())) {
            logger.trace("Selected Operator type "+ RootOperatorType.Interproc.name());
            p = new InterprocProperties(path);
            m = new InterprocMutationMaker((InterprocProperties) p);
        } else if (type.equalsIgnoreCase(RootOperatorType.Flexible.name())) {
            logger.trace("Selected Operator type "+ RootOperatorType.Flexible.name());
            p = new FlexibleOperatorProperties(path);
            m = new FlexibleMutationMaker((FlexibleOperatorProperties) p);
        } else if (type.equalsIgnoreCase(RootOperatorType.Custom.name())) {
            logger.trace("Selected Operator type "+ RootOperatorType.Custom.name());
            p = new CustomOperatorProperties(path);
        } else {
            logger.trace("Unknown Operator Type: " + type);
            return;
        }

        if (m != null)
            m.make(p);

        pluginOperatorsMutationMaker.make(p);

        logger.info("Main Scope mutation process ended");
    }

}