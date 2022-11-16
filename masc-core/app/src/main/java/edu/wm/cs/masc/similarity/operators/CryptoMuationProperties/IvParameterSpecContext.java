package edu.wm.cs.masc.similarity.operators.CryptoMuationProperties;

import edu.wm.cs.masc.mainScope.mutationmakers.AMutationMaker;
import edu.wm.cs.masc.mainScope.mutationmakers.ByteMutationMaker;
import edu.wm.cs.masc.mainScope.mutationmakers.StringOperatorMutationMaker;
import edu.wm.cs.masc.mutation.operators.IOperator;
import edu.wm.cs.masc.mutation.operators.OperatorType;
import edu.wm.cs.masc.mutation.properties.AOperatorProperties;
import edu.wm.cs.masc.mutation.properties.ByteOperatorProperties;
import edu.wm.cs.masc.mutation.properties.StringOperatorProperties;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.util.HashMap;

public class IvParameterSpecContext {
    AOperatorProperties byteOperator;
    AMutationMaker byteMuationMaker;

    public IvParameterSpecContext(String path){
        try {
            this.byteOperator = new ByteOperatorProperties(path);
            this.byteMuationMaker = new ByteMutationMaker((ByteOperatorProperties) this.byteOperator);
            this.byteMuationMaker.populateOperators();
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
    public HashMap<OperatorType, IOperator> getOperators(){
        return this.byteMuationMaker.operators;
    }
}
