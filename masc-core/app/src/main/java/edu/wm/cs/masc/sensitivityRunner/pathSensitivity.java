package edu.wm.cs.masc.sensitivityRunner;

import edu.wm.cs.masc.MASC;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class pathSensitivity extends aSensitivity{
    public void runPathSensitivity(HashMap userSelections) throws IOException {
        propertiesBuilder(userSelections);
    }

    public final String[] excludedStringOperators = new String[] {"StringDifferentCase StringNoiseReplace StringSafeReplaceWithUnsafe StringStringCaseTransform StringUnsafeReplaceWithUnsafe StringValueInVariable ValueOfCharInjectInsecureObject DifferentClass"};
    public final String[] excludedIntOperators = new String[] {"IntWhileLoopAccumulation IntIterationMultipleCall IntAbsoluteValue IntArithmetic IntFromString IntRoundValue IntValueInVariable IntValueInVariableArithmetic Overflow IntNestedClass"};
    public final String[] excludedByteOperators = new String[] {"ByteLoop ByteReuse ByteStatic ByteCurrentTime"};
    public final String[] excludedInterprocOperators = new String[] {"InterprocBaseCaseSeperateClass InterprocAddition Interproc"};


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


        masc.main(new String[] {"PathSensitiveString.properties"});
        masc.main(new String[] {"PathSensitiveInt.properties"});
        masc.main(new String[] {"PathSensitiveInterProc.properties"});
        masc.main(new String[] {"PathSensitiveByte.properties"});



    }
    public void propertiesBuilder(HashMap userSelections) throws IOException {
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

        propertyMaker("PathSensitiveString.properties",stringProperties);
        propertyMaker("PathSensitiveInt.properties",intProperties);
        propertyMaker("PathSensitiveInterProc.properties",interProcProperties);
        propertyMaker("PathSensitiveByte.properties",byteProperties);


        //}
        //if (scope == "SELECTIVE"){

        //}
        //if (scope == "EXHAUSTIVE"){

        //}
        //For all flow sensitive operators figure out root category and create

        //propertyMaker("stringFlowSensitivity.properties", properties);
    }

    public void propertyMaker(String fileName, String fileContents) throws IOException {
        //File pathProperties = new File("/properties/"+fileName);
        File pathProperties = new File(fileName);
        //File intFlowProperties = new File("/properties/stringFlowSensitivity.properties");
        if(pathProperties.exists()){
            pathProperties.delete();
        }
        pathProperties.createNewFile();
        //FileWriter flowPropWriter = new FileWriter("/properties/"+fileName);
        FileWriter pathPropWriter = new FileWriter(fileName);
        pathPropWriter.write(fileContents);
        pathPropWriter.close();

    }


}
