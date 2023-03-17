package edu.wm.cs.masc.mutation.operators.restrictive.interprocoperator;

import edu.wm.cs.masc.mutation.properties.InterprocProperties;

public class InterprocObjectSensitive extends AInterProcOperator{
    public InterprocObjectSensitive(InterprocProperties properties) {
        super(properties);
        properties.setBuilder("InterprocOperator");

    }

    public String insecure_call() {

        return String
                .format("String secure%6$s = " +
                                "new %4$s().A0().get%5$s();" +
                                "\n" +
                                "String %6$s = " +
                                "new %4$s().B().get%5$s();" +
                                "\n" +
                                "secure%6$s = %6$s;\n" +
                                "%1$s %2$s = " +
                                "%1$s.%3$s(secure%6$s);" +
                                "\n",
                        //properties.getApiName(),
                        api_name,
                        //properties.getVariableName(),
                        variableName,
                        //properties.getInvocation(),
                        invocation,
                        //properties.getOtherClassName(),
                        otherClassName,
                        //properties.getPropertyName());
                        propertyName,
                        tempVariable);
    }

    public String insecure_call_trycatch() {
        return "try {\n" +
                this.insecure_call() +
                //"System.out.println(" + properties.getVariableName() +
                "System.out.println(" + variableName +
                ".getAlgorithm());\n" +
                "} catch (Exception e) {\n" +
                "System.out.println(\"Error\");\n" +
                "}";
    }

    @Override
    public String mutation() {
        if (try_catch) {
            return insecure_call_trycatch();
        } else
            return insecure_call();
    }
}
