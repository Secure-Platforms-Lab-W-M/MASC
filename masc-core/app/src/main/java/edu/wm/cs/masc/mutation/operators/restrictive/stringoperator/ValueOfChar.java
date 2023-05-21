package edu.wm.cs.masc.mutation.operators.restrictive.stringoperator;

import edu.wm.cs.masc.mutation.properties.StringOperatorProperties;

public class ValueOfChar extends AStringOperator{
    public ValueOfChar(StringOperatorProperties properties) {
        super(properties);
    }

    @Override
    public String mutation() {
        StringBuilder sb = new StringBuilder();
        sb.append("String ")
                .append(variableName)
                .append(" = \"")
                .append(insecureParam)
                .append("\";\n");
        sb.append("char[] ").append(variableName + "1 ")
                .append(" = ")
                .append(variableName).append(".toCharArray()")
                .append(";\n");
        sb.append(api_name)
                .append(".")
                .append(invocation)
                .append("(")
                .append("String")
                .append(".valueOf")
                .append("(")
                .append(variableName + "1")
                .append(")")
                .append(");");
        return sb.toString();
    }
}
