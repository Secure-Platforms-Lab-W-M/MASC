package edu.wm.cs.masc.mutation.operators.restrictive.interprocoperator;

import edu.wm.cs.masc.mutation.operators.IOperator;
import edu.wm.cs.masc.mutation.properties.InterprocProperties;

public class InterProcOperator extends AInterProcOperator {

    public InterProcOperator(InterprocProperties properties) {
        super(properties);
    }

    public String insecure_call() {

        String iteration = "";
        for (int i = 0; i < iterationCount; i++){
            iteration = iteration + ".A().B()";
        }
        return String
                .format("%1$s %2$s = " +
                                "%1$s.%3$s(new %4$s()%6$s.get%5$s());" +
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
                        iteration);
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
