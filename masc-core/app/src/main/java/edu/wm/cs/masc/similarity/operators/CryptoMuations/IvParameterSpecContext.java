package edu.wm.cs.masc.similarity.operators.CryptoMuations;

import edu.wm.cs.masc.mainScope.mutationmakers.AMutationMaker;
import edu.wm.cs.masc.mainScope.mutationmakers.ByteMutationMaker;
import edu.wm.cs.masc.mutation.operators.IOperator;
import edu.wm.cs.masc.mutation.operators.OperatorType;
import edu.wm.cs.masc.mutation.properties.ByteOperatorProperties;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.util.HashMap;

public class IvParameterSpecContext implements ASimilarityContext {
    ByteOperatorProperties byteOperator;
    AMutationMaker byteMuationMaker;
    public IvParameterSpecContext(String path){
        try {
            this.byteOperator = new ByteOperatorProperties(path);
            this.byteMuationMaker = new ByteMutationMaker(this.byteOperator);
            this.byteMuationMaker.populateOperators();

        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
    public HashMap<OperatorType, IOperator> getOperators(){
        return this.byteMuationMaker.operators;
    }
    public String mutation(){
        StringBuilder sb = new StringBuilder();
        for (OperatorType operatorType : this.getOperators().keySet()) {
            sb.append(this.getOperators().get(operatorType).mutation());
        }
        return sb.toString();
    }
}
