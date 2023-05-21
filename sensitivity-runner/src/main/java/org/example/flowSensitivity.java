package org.example;

import java.io.BufferedInputStream;
import java.io.File;  // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors
import java.io.FileWriter;

import java.lang.reflect.Array;
import java.util.*;

public class flowSensitivity extends aSensitivity{
    public void runFlowSensitivity(HashMap userSelections) throws IOException {
        propertiesBuilder(userSelections);
    }

    public final String[] excludedStringOperators = new String[] {"InjectInsecureObject"};
    public final String[] excludedIntOperators = new String[] {"InjectInsecureObject"};
    public final String[] excludedByteOperators = new String[] {"InjectInsecureObject"};
    public final String[] excludedInterprocOperators = new String[] {"InjectInsecureObject"};

    public void callMASC() throws Exception {
        Process process = Runtime.getRuntime().exec("java -jar app-all.jar FlowSensitiveString.properties");
        BufferedInputStream processOutput= new BufferedInputStream(process.getInputStream());
        int read = 0;
        byte[] output = new byte[1024];
        while ((read = processOutput.read(output)) != -1) {
            System.out.println(output[read]);
        }
        //System.out.println(process.exitValue());
        //MASC masc = new MASC();
        //masc.main(new String[] "FlowSensi");
        //System.out.println(process);

        //masc.main(new String[] {"FlowSensitiveString.properties"});



    }
    public void propertiesBuilder(HashMap userSelections) throws IOException {
        Set keys = userSelections.keySet();
        String baseProperties = "";
        //if (scope == "MAIN"){

        for (Object key : userSelections.keySet()){
            if (!(key.toString().contains("String") || key.toString().contains("Interproc") || key.toString().contains("Int") || key.toString().contains("Byte"))){
                baseProperties = baseProperties + key + " = " + userSelections.get(key) + "\n";
            }

        }

        String stringProperties = stringProperties(baseProperties,excludedStringOperators,getOperatorOptions(userSelections,"String"));
        String intProperties = baseProperties;
        String interProcProperties = baseProperties;
        String byteProperties = baseProperties;

        propertyMaker("FlowSensitiveString.properties",stringProperties);


        //}
        //if (scope == "SELECTIVE"){

        //}
        //if (scope == "EXHAUSTIVE"){

        //}
        //For all flow sensitive operators figure out root category and create

        //propertyMaker("stringFlowSensitivity.properties", properties);
    }

    public void propertyMaker(String fileName, String fileContents) throws IOException {
        //File flowProperties = new File("/properties/"+fileName);
        File flowProperties = new File(fileName);
        //File intFlowProperties = new File("/properties/stringFlowSensitivity.properties");
        if(flowProperties.exists()){
            flowProperties.delete();
        }
        flowProperties.createNewFile();
        //FileWriter flowPropWriter = new FileWriter("/properties/"+fileName);
        FileWriter flowPropWriter = new FileWriter(fileName);
        flowPropWriter.write(fileContents);
        flowPropWriter.close();

    }


}
