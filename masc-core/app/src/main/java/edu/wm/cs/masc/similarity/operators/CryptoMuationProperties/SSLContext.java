package edu.wm.cs.masc.similarity.operators.CryptoMuationProperties;

import edu.wm.cs.masc.mainScope.mutationmakers.AMutationMaker;
import edu.wm.cs.masc.mainScope.mutationmakers.StringOperatorMutationMaker;
import edu.wm.cs.masc.mutation.operators.IOperator;
import edu.wm.cs.masc.mutation.operators.OperatorType;
import edu.wm.cs.masc.mutation.properties.AOperatorProperties;
import edu.wm.cs.masc.mutation.properties.StringOperatorProperties;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.util.HashMap;

public class SSLContext{
    AOperatorProperties stringOperator;
    AMutationMaker stringMuationMaker;

    public SSLContext(String path){
        try {
            this.stringOperator = new StringOperatorProperties(path);
            this.stringMuationMaker = new StringOperatorMutationMaker((StringOperatorProperties) this.stringOperator);
            this.stringMuationMaker.populateOperators();
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
    public HashMap<OperatorType, IOperator> getOperators(){
        return this.stringMuationMaker.operators;
    }
}
