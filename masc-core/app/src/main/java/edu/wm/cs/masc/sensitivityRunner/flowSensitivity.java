package edu.wm.cs.masc.sensitivityRunner;

import edu.wm.cs.masc.MASC;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class flowSensitivity extends aSensitivity{
    public void runFlowSensitivity(HashMap userSelections) throws IOException {
        propertiesBuilder(userSelections);
    }

    public final String[] excludedStringOperators = new String[] {"InjectInsecureObject DifferentClass"};
    public final String[] excludedIntOperators = new String[] {"IntWhileLoopAccumulation IntIterationMultipleCall IntNestedClass"};
    public final String[] excludedByteOperators = new String[] {"ByteLoop ByteReuse"};
    public final String[] excludedInterprocOperators = new String[] {"BaseCaseSeperateClass InterProcAddition InterprocConditional InterProcOperator"};


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


        masc.main(new String[] {"FlowSensitiveString.properties"});
        masc.main(new String[] {"FlowSensitiveInt.properties"});
        masc.main(new String[] {"FlowSensitiveInterProc.properties"});
        masc.main(new String[] {"FlowSensitiveByte.properties"});



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

        propertyMaker("FlowSensitiveString.properties",stringProperties);
        propertyMaker("FlowSensitiveInt.properties",intProperties);
        propertyMaker("FlowSensitiveInterProc.properties",interProcProperties);
        propertyMaker("FlowSensitiveByte.properties",byteProperties);


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
