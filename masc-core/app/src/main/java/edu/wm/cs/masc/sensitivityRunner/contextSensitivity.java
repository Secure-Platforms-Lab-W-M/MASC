package edu.wm.cs.masc.sensitivityRunner;

import edu.wm.cs.masc.MASC;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class contextSensitivity extends aSensitivity{
    public void runContextSensitivity(HashMap userSelections) throws IOException {
        propertiesBuilder(userSelections);
    }

    public final String[] excludedStringOperators = new String[] {"StringDifferentCase DifferentClass StringNoiseReplace StringSafeReplaceWithUnsafe StringStringCaseTransform StringUnsafeReplaceWithUnsafe StringValueInVariable"};
    public final String[] excludedIntOperators = new String[] {"IntWhileLoopAccumulation IntArithmetic IntNestedClass IntAbsoluteValue IntValueInVariable IntRoundValue Overflow IntFromString IntValueInVariableArithmetic"};
    public final String[] excludedByteOperators = new String[] {"ByteLoop ByteCurrentTime"};
    public final String[] excludedInterprocOperators = new String[] {""};


    public void callMASC() throws Exception {
        //Process process = Runtime.getRuntime().exec("java -jar app-all.jar FlowSensitiveString.properties");
        //BufferedInputStream processOutput= new BufferedInputStream(process.getInputStream());
        //int read = 0;
        //byte[] output = new byte[1024];
        //while ((read = processOutput.read(output)) != -1) {
        //    System.out.println(output[read]);
        //}
        //System.out.println(process.exitValue());
        MASC masc = new MASC();


        masc.main(new String[] {"ContextSensitiveString.properties"});
        masc.main(new String[] {"ContextSensitiveInt.properties"});
        masc.main(new String[] {"ContextSensitiveInterProc.properties"});
        masc.main(new String[] {"ContextSensitiveByte.properties"});



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
        String intProperties = intProperties(baseProperties,excludedIntOperators,getOperatorOptions(userSelections,"Int"));;
        String interProcProperties = interProcProperties(baseProperties,excludedInterprocOperators,getOperatorOptions(userSelections,"Interproc"));;
        String byteProperties = byteProperties(baseProperties,excludedByteOperators,getOperatorOptions(userSelections,"Byte"));;

        propertyMaker("ContextSensitiveString.properties",stringProperties);
        propertyMaker("ContextSensitiveInt.properties",intProperties);
        propertyMaker("ContextSensitiveInterProc.properties",interProcProperties);
        propertyMaker("ContextSensitiveByte.properties",byteProperties);


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
