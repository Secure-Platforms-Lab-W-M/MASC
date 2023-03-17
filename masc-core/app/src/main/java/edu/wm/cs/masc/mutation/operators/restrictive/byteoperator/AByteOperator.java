package edu.wm.cs.masc.mutation.operators.restrictive.byteoperator;

import edu.wm.cs.masc.mutation.operators.IOperator;
import edu.wm.cs.masc.mutation.properties.ByteOperatorProperties;

public abstract class AByteOperator implements IOperator {


    protected final String api_name;
    protected final String api_variable;
    protected final String tempVariableName;
    protected final String invocation;
    protected final String insecureParam;

    public AByteOperator(ByteOperatorProperties p) {
        this.api_name = p.getApiName();
        this.api_variable = p.getApiVariable();
        this.tempVariableName = p.getTempVariableName();
        this.invocation = p.getInvocation();
        this.insecureParam = p.getInsecureParam();
    }
}
