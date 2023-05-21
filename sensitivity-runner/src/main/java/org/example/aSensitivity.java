package org.example;


import java.io.IOException;
import java.lang.reflect.Array;

import java.util.HashMap;
import java.util.Properties;

public class aSensitivity {

    public aSensitivity() {

    }

    public void callMASC() throws Exception {
        //Process process = Runtime.getRuntime().exec("");
        //MASC masc = new MASC();




    }
    public void propertyBuilder() throws IOException {

    }

    public void createSensitivities(Properties p){

    }

    public HashMap getOperatorOptions(HashMap userSelections, String operatorType){
        HashMap operatorOptions = new HashMap<>();
        for (Object key : userSelections.keySet()){
            if (key.toString().contains(operatorType)){
                String newKey = key.toString().replace(operatorType,"");
                char[] keyChar = newKey.toCharArray();
                keyChar[0] = Character.toLowerCase(keyChar[0]);
                //newKey = keyChar.toString();
                newKey = new String(keyChar);
                operatorOptions.put(newKey.toString(),userSelections.get(key));
            }
        }
        operatorOptions.put("type",operatorType+"Operator");
        return operatorOptions;
    }

    public String stringProperties(String baseProperties, String excludedOperators[], HashMap stringOptions){
        String stringProperties = baseProperties + "\n";
        for (Object key : stringOptions.keySet()){
            stringProperties = stringProperties + key + " = " + stringOptions.get(key) + "\n";
        }
        String excludedOps = "excludedOperators = ";
        for (int i = 0; i < excludedOperators.length; i++)
            excludedOps = excludedOps + excludedOperators[i] + ", ";
        stringProperties = stringProperties + excludedOps + "\n";
        return stringProperties;
    }


    public String intProperties(String baseProperties, String excludedOperators[]){

        return baseProperties;
    }


    public String byteProperties(String baseProperties, String excludedOperators[]){
        //
        return baseProperties;
    }


    public String interprocProperties(String baseProperties, String excludedOperators[]){
        //interations


        return baseProperties;
    }
}
