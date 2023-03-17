package edu.wm.cs.masc.sensitivityRunner;

import edu.wm.cs.masc.MASC;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class fieldSensitivity extends aSensitivity{
    public void runFieldSensitivity(HashMap userSelections) throws IOException {
        propertiesBuilder(userSelections);
    }

    public final String[] excludedStringOperators = new String[] {"StringDifferentCase StringNoiseReplace StringSafeReplaceWithUnsafe StringStringCaseTransform StringUnsafeReplaceWithUnsafe StringValueInVariable ValueOfChar"};
    public final String[] excludedIntOperators = new String[] {"IntWhileLoopAccumulation IntIterationMultipleCall IntNestedClass IntAbsoluteValue IntArithmetic IntFromString IntRoundValue IntValueInVariable IntValueInVariableArithmetic Overflow"};
    public final String[] excludedByteOperators = new String[] {"ByteLoop ByteReuse ByteCurrentTime ByteStatic"};
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


        masc.main(new String[] {"FieldSensitiveString.properties"});
        masc.main(new String[] {"FieldSensitiveInt.properties"});
        masc.main(new String[] {"FieldSensitiveInterProc.properties"});
        masc.main(new String[] {"FieldSensitiveByte.properties"});



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

        propertyMaker("FieldSensitiveString.properties",stringProperties);
        propertyMaker("FieldSensitiveInt.properties",intProperties);
        propertyMaker("FieldSensitiveInterProc.properties",interProcProperties);
        propertyMaker("FieldSensitiveByte.properties",byteProperties);

    }

    public void propertyMaker(String fileName, String fileContents) throws IOException {
        File fieldProperties = new File(fileName);
        if(fieldProperties.exists()){
            fieldProperties.delete();
        }
        fieldProperties.createNewFile();
        FileWriter fieldPropWriter = new FileWriter(fileName);
        fieldPropWriter.write(fileContents);
        fieldPropWriter.close();

    }

}
