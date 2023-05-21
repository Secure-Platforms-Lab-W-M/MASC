package edu.wm.cs.masc.mutation.operators.restrictive.stringoperator;

import edu.wm.cs.masc.mutation.properties.StringOperatorProperties;

public class Substring extends AStringOperator{
    public Substring(StringOperatorProperties properties) {
        super(properties);
    }

    @Override
    public String mutation() {
        StringBuilder s = new StringBuilder();
        s.append(api_name)
                .append(".")
                .append(invocation)
                .append("(\"").append("secureParam").append(insecureParam).append("\".")
                .append("substring(11));");
        return s.toString();
    }

}
