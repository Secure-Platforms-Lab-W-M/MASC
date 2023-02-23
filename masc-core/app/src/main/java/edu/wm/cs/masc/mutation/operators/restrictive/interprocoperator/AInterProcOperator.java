package edu.wm.cs.masc.mutation.operators.restrictive.interprocoperator;

import edu.wm.cs.masc.mutation.operators.IOperator;
import edu.wm.cs.masc.mutation.properties.InterprocProperties;

public abstract class AInterProcOperator implements IOperator {
    protected final String api_name;
    protected final String invocation;
    protected final String secureParam;
    protected final String insecureParam;
    protected final String noise;
    protected final String variableName;
    protected final String propertyName;
    protected final  Boolean try_catch;
    protected final String otherClassName;
    protected final int iterationCount;
    //public final InterprocProperties p;

    public AInterProcOperator(InterprocProperties p) {

        //this.p = p;
        this.api_name = p.getApiName();
        this.invocation = p.getInvocation();
        this.secureParam = p.getSecureParam();
        this.insecureParam = p.getInsecureParam();
        this.noise = p.getNoise();
        this.variableName = p.getVariableName();
        this.propertyName = p.getPropertyName();
        this.try_catch = p.getTry_catch();
        this.otherClassName = p.getOtherClassName();
        this.iterationCount = Integer.parseInt(p.getIterationCount());



    }
}
