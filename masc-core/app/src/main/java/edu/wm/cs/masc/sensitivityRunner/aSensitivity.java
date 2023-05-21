package edu.wm.cs.masc.sensitivityRunner;


import java.io.IOException;
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

    public void createSensitivities(Properties p) {

    }

    public HashMap getOperatorOptions(HashMap userSelections, String operatorType) {
        HashMap operatorOptions = new HashMap<>();
        for (Object key : userSelections.keySet()) {
            if (key.toString().contains(operatorType)) {
                String newKey = key.toString().replace(operatorType, "");
                char[] keyChar = newKey.toCharArray();
                keyChar[0] = Character.toLowerCase(keyChar[0]);
                //newKey = keyChar.toString();
                newKey = new String(keyChar);
                operatorOptions.put(newKey.toString(), userSelections.get(key));
            }
        }
        if (operatorType == "Interproc"){
            operatorOptions.put("type", operatorType);
        }
        else {
            operatorOptions.put("type", operatorType + "Operator");
        }

        return operatorOptions;
    }

    public String stringProperties(String baseProperties, String excludedOperators[], HashMap stringOptions) {
        String stringProperties = baseProperties + "\n";
        for (Object key : stringOptions.keySet()) {
            stringProperties = stringProperties + key + " = " + stringOptions.get(key) + "\n";
        }
        String excludedOps = "excludedOperators = ";
        for (int i = 0; i < excludedOperators.length; i++)
            excludedOps = excludedOps + excludedOperators[i] + ", ";
        stringProperties = stringProperties + excludedOps + "\n";
        return stringProperties;
    }

    public String intProperties(String baseProperties, String excludedOperators[], HashMap intOptions) {
        String intProperties = baseProperties + "\n";
        for (Object key : intOptions.keySet()) {
            intProperties = intProperties + key + " = " + intOptions.get(key) + "\n";
        }
        String excludedOps = "excludedOperators = ";
        for (int i = 0; i < excludedOperators.length; i++)
            excludedOps = excludedOps + excludedOperators[i] + ", ";
        intProperties = intProperties + excludedOps + "\n";
        return intProperties;
    }

    public String byteProperties(String baseProperties, String excludedOperators[], HashMap byteOptions) {
        String byteProperties = baseProperties + "\n";
        for (Object key : byteOptions.keySet()) {
            byteProperties = byteProperties + key + " = " + byteOptions.get(key) + "\n";
        }
        String excludedOps = "excludedOperators = ";
        for (int i = 0; i < excludedOperators.length; i++)
            excludedOps = excludedOps + excludedOperators[i] + ", ";
        byteProperties = byteProperties + excludedOps + "\n";
        return byteProperties;
    }

    public String interProcProperties(String baseProperties, String excludedOperators[], HashMap interProcOptions) {
        String interProcProperties = baseProperties + "\n";
        for (Object key : interProcOptions.keySet()) {
            interProcProperties = interProcProperties + key + " = " + interProcOptions.get(key) + "\n";
        }
        String excludedOps = "excludedOperators = ";
        for (int i = 0; i < excludedOperators.length; i++)
            excludedOps = excludedOps + excludedOperators[i] + ", ";
        interProcProperties = interProcProperties + excludedOps + "\n";
        return interProcProperties;
    }


}






