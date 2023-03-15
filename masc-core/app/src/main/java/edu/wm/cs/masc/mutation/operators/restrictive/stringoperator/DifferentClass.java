package edu.wm.cs.masc.mutation.operators.restrictive.stringoperator;

import edu.wm.cs.masc.mutation.properties.StringOperatorProperties;



public class DifferentClass extends AStringOperator {
    public DifferentClass(StringOperatorProperties properties) {
        super(properties);
    }



    @Override
    public String mutation() {
        StringBuilder s = new StringBuilder();
        s.append("public class CryptoObject { \n")
                .append("   " + variableName + " = \"\";\n")
                .append("   public CryptoObject(){ \n")
                .append("       " + variableName + " = \"" + secureParam +"\";\n")
                .append("   } \n")
                .append("   public void setValue(String value) { \n")
                .append("       " + variableName + " = value;\n" )
                .append("   } \n")
                .append("   public void getValue(String value) { \n")
                .append("       return " + variableName + ";\n" )
                .append("   } \n")
                .append("} \n")
                .append("CryptoObject " + variableName + " = new CryptoObject();\n")
                .append(variableName + ".setValue(\"" + insecureParam + "\");\n")
                .append("CryptoObject " + variableName + "Secure = new CryptoObject();\n")
                .append(variableName + "Secure.setValue(\"" + secureParam + "\");\n")
                .append(api_name)
                .append(".")
                .append(invocation)
                .append("(")
                .append(variableName + "Secure.getValue().replace(\"" + secureParam + "\"," + variableName + ".getValue())")
                .append(");\n");
        return s.toString();
    }
}

