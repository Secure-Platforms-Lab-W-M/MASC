package edu.wm.cs.masc.sensitivityRunner;

import edu.wm.cs.masc.MASC;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class objectSensitivity extends aSensitivity{
    public void runObjectSensitivity(HashMap userSelections) throws IOException {
        propertiesBuilder(userSelections);
    }

    public final String[] excludedStringOperators = new String[] {"InjectInsecureObject StringDifferentCase StringNoiseReplace StringSafeReplaceWithUnsafe StringStringCaseTransform StringUnsafeReplaceWithUnsafe StringValueInVariable ValueOfChar"};
    public final String[] excludedIntOperators = new String[] {"IntWhileLoopAccumulation IntIterationMultipleCall IntAbsoluteValue IntArithmetic IntFromString IntRoundValue IntValueInVariable IntValueInVariableArithmetic Overflow"};
    public final String[] excludedByteOperators = new String[] {"ByteLoop ByteCurrentTime ByteStatic"};
    public final String[] excludedInterprocOperators = new String[] {"InterprocBaseCaseSeperateClass InterprocAddition InterprocConditional Interproc InterprocNestedConditional"};


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


        masc.main(new String[] {"ObjectSensitiveString.properties"});
        masc.main(new String[] {"ObjectSensitiveInt.properties"});
        masc.main(new String[] {"ObjectSensitiveInterProc.properties"});
        masc.main(new String[] {"ObjectSensitiveByte.properties"});



    }
    public void propertiesBuilder(HashMap userSelections) throws IOException {

        String baseProperties = "";

        for (Object key : userSelections.keySet()){
            if (!(key.toString().contains("String") || key.toString().contains("Interproc") || key.toString().contains("Int") || key.toString().contains("Byte"))){
                baseProperties = baseProperties + key + " = " + userSelections.get(key) + "\n";
            }

        }

        String stringProperties = stringProperties(baseProperties,excludedStringOperators,getOperatorOptions(userSelections,"String"));
        String intProperties = intProperties(baseProperties,excludedIntOperators,getOperatorOptions(userSelections,"Int"));;
        String interProcProperties = interProcProperties(baseProperties,excludedInterprocOperators,getOperatorOptions(userSelections,"Interproc"));;
        String byteProperties = byteProperties(baseProperties,excludedByteOperators,getOperatorOptions(userSelections,"Byte"));;

        propertyMaker("ObjectSensitiveString.properties",stringProperties);
        propertyMaker("ObjectSensitiveInt.properties",intProperties);
        propertyMaker("ObjectSensitiveInterProc.properties",interProcProperties);
        propertyMaker("ObjectSensitiveByte.properties",byteProperties);


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
        File objectProperties = new File(fileName);
        //File intFlowProperties = new File("/properties/stringFlowSensitivity.properties");
        if(objectProperties.exists()){
            objectProperties.delete();
        }
        objectProperties.createNewFile();
        FileWriter objectPropWriter = new FileWriter(fileName);
        objectPropWriter.write(fileContents);
        objectPropWriter.close();

    }


}
