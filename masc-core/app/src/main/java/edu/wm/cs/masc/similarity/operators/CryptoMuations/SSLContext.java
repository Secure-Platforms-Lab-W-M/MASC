package edu.wm.cs.masc.similarity.operators.CryptoMuations;

import edu.wm.cs.masc.mutation.properties.StringOperatorProperties;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class SSLContext implements ASimilarityContext {
    StringOperatorProperties stringOperator;

    public SSLContext(String path){
        try {
            this.stringOperator = new StringOperatorProperties(path);
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public String mutation(){
        StringBuilder sb = new StringBuilder();
        sb.append("SSLContext ")
                .append(this.stringOperator.getVariableName())
                .append(" = ")
                .append( this.stringOperator.getApiName())
                .append(".")
                .append(this.stringOperator.getInvocation())
                .append("(\"")
                .append(this.stringOperator.getInsecureParam())
                .append("\");\n");
        sb.append("System.out.println(")
                .append(this.stringOperator.getVariableName())
                .append(".getProtocol()")
                .append(");\n");
        return sb.toString();
    }
}
