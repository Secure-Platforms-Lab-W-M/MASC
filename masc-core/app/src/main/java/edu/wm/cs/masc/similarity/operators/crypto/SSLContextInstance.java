package edu.wm.cs.masc.similarity.operators.crypto;

import edu.wm.cs.masc.similarity.operators.CryptoMuations.ASimilarityContext;
import edu.wm.cs.masc.similarity.operators.CryptoMuations.CipherInstanceContext;
import edu.wm.cs.masc.similarity.operators.CryptoMuations.MessageDigestContext;
import edu.wm.cs.masc.similarity.operators.CryptoMuations.SSLContext;

public class SSLContextInstance extends ACryptoMutationOperator {

    public ASimilarityContext ssl = new SSLContext("DefaultSSLContextStringOperator.properties");

    @Override
    protected String getMutatedLine() {
        return "try {\n" +
//                "   SSLContext cryptoContext = SSLContext.getInstance(\"SSL\");\n" +
//                "   System.out.println(cryptoContext.getProtocol());\n" +
                 ssl.mutation() +
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
