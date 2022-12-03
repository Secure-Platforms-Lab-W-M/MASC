package edu.wm.cs.masc.similarity.operators.crypto;

import edu.wm.cs.masc.mutation.operators.OperatorType;
import edu.wm.cs.masc.similarity.operators.CryptoMuations.ASimilarityContext;
import edu.wm.cs.masc.similarity.operators.CryptoMuations.MessageDigestContext;

public class
MessageDigest extends ACryptoMutationOperator{
    public ASimilarityContext mdc = new MessageDigestContext("DefaultSimilarityMessageDigest.properties");
    @Override
    protected String getMutatedLine() {
        return "MessageDigest cryptoDigest;\n" +
                "        try {\n" +
//                "            cryptoDigest = MessageDigest.getInstance(\"SHA-256\".replace(\"SHA-256\", \"md5\"));\n" +
//                "            System.out.println(cryptoDigest.getAlgorithm());\n" +
                mdc.mutation() +
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
