package edu.wm.cs.masc.mutation.properties;


import edu.wm.cs.masc.utils.config.PropertiesReader;
import org.apache.commons.configuration2.ex.ConfigurationException;

public abstract class AOperatorProperties {

    protected final String type;
    protected final String outputDir;
    protected final String apiName;
    protected final String className;
    protected final String leftOutOperators;
    protected PropertiesReader reader;

    public String getOtherClassName() {
        return reader.getValueForAKey("otherClassName");
    }

    public String getType() {
        return type;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public String getApiName() {
        return apiName;
    }

    public String getClassName() {
        return className;
    }

    public String getLeftOutOperators() { return leftOutOperators; }

    public PropertiesReader getReader() {
        return reader;
    }

    public AOperatorProperties(String path) throws ConfigurationException {
        reader = new PropertiesReader(path);
        type = reader.getValueForAKey("type");
        //String guy = ("Working Directory = " + System.getProperty("user.dir"));

        //outputDir = System.getProperty("user.dir").substring(0,System.getProperty("user.dir").length() - 29) + reader.getValueForAKey("outputDir");
        outputDir = reader.getValueForAKey("outputDir");
        apiName = reader.getValueForAKey("apiName");
        className = reader.getValueForAKey("className");
        leftOutOperators = reader.getValueForAKey("leftOutOperators");
//        reader.getValueForAKey("test");
        // otherClassName = reader.getValueForAKey("otherClassName");
    }

    public AOperatorProperties(String type, String outputDir,
                               String apiName, String className,
                               String otherClassName, String leftOutOperators) {
        this.type = type;
        this.outputDir = outputDir;
        this.apiName = apiName;
        this.className = className;
        this.leftOutOperators = leftOutOperators;
        // this.otherClassName = otherClassName;
    }
}
