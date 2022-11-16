package edu.wm.cs.masc.similarity.operators.crypto;

import edu.wm.cs.masc.mutation.operators.OperatorType;
import edu.wm.cs.masc.similarity.operators.CryptoMuationProperties.MessageDigestContext;

public class
MessageDigest extends ACryptoMutationOperator{
    public MessageDigestContext mdc = new MessageDigestContext("SSLContextStringOperator.properties");
    @Override
    protected String getMutatedLine() {
        for (OperatorType operatorType : mdc.getOperators().keySet()) {
            System.out.println(mdc.getOperators().get(operatorType).mutation());
        }
        return "MessageDigest cryptoDigest;\n" +
                "        try {\n" +
                "            cryptoDigest = MessageDigest.getInstance(\"SHA-256\".replace(\"SHA-256\", \"md5\"));\n" +
                "            System.out.println(cryptoDigest.getAlgorithm());\n" +
                "\n" +
                "        } catch (NoSuchAlgorithmException e) {\n" +
                "            System.out.println(\"Error\");\n" +
                "        }";
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
