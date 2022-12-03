package edu.wm.cs.masc.similarity.operators.CryptoMuations;

import edu.wm.cs.masc.mainScope.mutationmakers.AMutationMaker;
import edu.wm.cs.masc.mainScope.mutationmakers.StringOperatorMutationMaker;
import edu.wm.cs.masc.mutation.operators.IOperator;
import edu.wm.cs.masc.mutation.operators.OperatorType;
import edu.wm.cs.masc.mutation.properties.AOperatorProperties;
import edu.wm.cs.masc.mutation.properties.StringOperatorProperties;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.util.HashMap;

public class MessageDigestContext implements ASimilarityContext {
    StringOperatorProperties stringOperator;
    AMutationMaker stringMuationMaker;

    public MessageDigestContext(String path){
        try {
            this.stringOperator = new StringOperatorProperties(path);
            this.stringMuationMaker = new StringOperatorMutationMaker(this.stringOperator);
            this.stringMuationMaker.populateOperators();
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
    public HashMap<OperatorType, IOperator> getOperators(){
        return this.stringMuationMaker.operators;
    }

    @Override
    public String mutation() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.stringOperator.getVariableName())
                .append(" = ");
        for (OperatorType operatorType : this.getOperators().keySet()) {
            sb.append(this.getOperators().get(operatorType).mutation());
        }
        sb.append(";\n");
        sb.append("System.out.println(")
                .append(this.stringOperator.getVariableName())
                .append(".getAlgorithm()")
                .append(");\n");
        return sb.toString();
    }
}
