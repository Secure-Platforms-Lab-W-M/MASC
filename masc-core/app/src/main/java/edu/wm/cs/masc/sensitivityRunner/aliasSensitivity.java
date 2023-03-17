package edu.wm.cs.masc.sensitivityRunner;

import edu.wm.cs.masc.MASC;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class aliasSensitivity extends aSensitivity{
    public void runAliasSensitivity(HashMap userSelections) throws IOException {
        propertiesBuilder(userSelections);
    }

    public final String[] excludedStringOperators = new String[] {"DifferentClass StringDifferentCase StringNoiseReplace StringSafeReplaceWithUnsafe StringStringCaseTransform StringUnsafeReplaceWithUnsafe ValueOfChar"};
    public final String[] excludedIntOperators = new String[] {"IntAbsoluteValue IntArithmetic IntFromString IntIterationMultipleCall IntNestedClass IntRoundValue Overflow"};
    public final String[] excludedByteOperators = new String[] {"ByteCurrentTime ByteLoop ByteReuse ByteStatic"};
    public final String[] excludedInterprocOperators = new String[] {"InterprocBaseCaseSeperateClass InterprocAddition InterprocConditional InterProc InterprocNestedConditional"};


    public void callMASC() throws Exception {

        MASC masc = new MASC();


        masc.main(new String[] {"AliasSensitiveString.properties"});
        masc.main(new String[] {"AliasSensitiveInt.properties"});
        masc.main(new String[] {"AliasSensitiveInterProc.properties"});
        masc.main(new String[] {"AliasSensitiveByte.properties"});



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

        propertyMaker("AliasSensitiveString.properties",stringProperties);
        propertyMaker("AliasSensitiveInt.properties",intProperties);
        propertyMaker("AliasSensitiveInterProc.properties",interProcProperties);
        propertyMaker("AliasSensitiveByte.properties",byteProperties);

    }

    public void propertyMaker(String fileName, String fileContents) throws IOException {
        File aliasProperties = new File(fileName);
        if(aliasProperties.exists()){
            aliasProperties.delete();
        }
        aliasProperties.createNewFile();
        FileWriter aliasPropWriter = new FileWriter(fileName);
        aliasPropWriter.write(fileContents);
        aliasPropWriter.close();

    }


}
