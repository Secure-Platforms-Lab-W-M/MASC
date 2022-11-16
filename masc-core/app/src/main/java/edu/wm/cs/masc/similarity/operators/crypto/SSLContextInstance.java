package edu.wm.cs.masc.similarity.operators.crypto;

import edu.wm.cs.masc.mutation.operators.OperatorType;
import edu.wm.cs.masc.similarity.operators.CryptoMuationProperties.SSLContext;

public class SSLContextInstance extends ACryptoMutationOperator {

    public SSLContext ssl = new SSLContext("SSLContextStringOperator.properties");

    @Override
    protected String getMutatedLine() {

        for (OperatorType operatorType : ssl.getOperators().keySet()) {
            System.out.println(ssl.getOperators().get(operatorType).mutation());
        }
        return "try {\n" +
                "   SSLContext cryptoContext = SSLContext.getInstance(\"SSL\");\n" +
                "   System.out.println(cryptoContext.getProtocol());\n" +
                "} catch (NoSuchAlgorithmException e) {\n" +
                "   System.out.println(\"Error\");\n" +
                "}";
    }

    @Override
    protected String packageLines() {
        return "import java.security.NoSuchAlgorithmException;\n";
    }

    @Override
    protected String getTemplatePath() {
        return null;
    }

    @Override
    protected String getTemplateBasedFileName() {
        return null;
    }
}
