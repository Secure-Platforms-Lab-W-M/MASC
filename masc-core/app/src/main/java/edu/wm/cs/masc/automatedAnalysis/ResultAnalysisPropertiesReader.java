package edu.wm.cs.masc.automatedAnalysis;

import edu.wm.cs.masc.utils.config.PropertiesReader;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;

public class ResultAnalysisPropertiesReader {
    PropertiesReader propertiesReader;
    String toolName, toolLocation, toolRunCommand, outputDIr;
    String codeCompileCommand, outputReportDirectory, outputFileName,
            stopCondition;
    String wrapper;

    public ResultAnalysisPropertiesReader(PropertiesReader propertiesReader) {
        this.propertiesReader = propertiesReader;
        initializeValues();
    }

    public ResultAnalysisPropertiesReader(String path) throws ConfigurationException {
        this.propertiesReader = new PropertiesReader(path);
        initializeValues();
    }

    private void initializeValues() {
        toolName = propertiesReader.getValueForAKey("toolName");
        toolLocation = propertiesReader.getValueForAKey("toolLocation");
        toolRunCommand = propertiesReader.getValueForAKey("toolRunCommand");
        if(propertiesReader.contains("codeCompileCommand"))
            codeCompileCommand = propertiesReader.getValueForAKey("codeCompileCommand");
        else
            codeCompileCommand = "echo hi";
        outputReportDirectory = propertiesReader.getValueForAKey("outputReportDirectory");
        outputFileName = propertiesReader.getValueForAKey("outputFileName");
        stopCondition = propertiesReader.getValueForAKey("stopCondition");
        outputDIr = propertiesReader.getValueForAKey("outputDir");

        wrapper = propertiesReader.getValueForAKeyNoInput("wrapper");
    }

    public String getToolRunCommand(String dir) {
//        return toolRunCommand.replace("{}", System.getProperty("user.dir") + outputDIr + dir);
        return toolRunCommand.replace("{}", getAbsolutePathOfMutatedAppsDirectory() + "/" + dir);

    }

    public String getAbsolutePathOfMutatedAppsDirectory(){
        File file = new File(getMutatedAppsLocation());
        return file.getAbsolutePath();
    }

    public String getOutputReportFileWithDir(){
        return outputReportDirectory + "/" + outputFileName;
    }

    public boolean stopOnError() {
        return stopCondition.equalsIgnoreCase("OnError") || stopCondition.equalsIgnoreCase("OnErrorOrUnkilled");
    }

    public boolean stopOnUnkilled() {
        return stopCondition.equalsIgnoreCase("OnUnkilledMutant") || stopCondition.equalsIgnoreCase("OnErrorOrUnkilled");
    }

    public String getMutatedAppsLocation() {
        return propertiesReader.getValueForAKey("outputDir");
    }

    public String getWrapper() {
        return wrapper;
    }
}
