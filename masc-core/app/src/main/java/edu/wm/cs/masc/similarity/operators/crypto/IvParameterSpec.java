package edu.wm.cs.masc.similarity.operators.crypto;

import edu.wm.cs.masc.similarity.operators.CryptoMuationProperties.IvParameterSpecContext;

public class IvParameterSpec extends ACryptoMutationOperator {
    public IvParameterSpecContext iv = new IvParameterSpecContext("SimilarityByteOperator.properties");
    @Override
    protected String getMutatedLine() {
        String mutatedLine="String cipherVAL=\"\";\n"+
            "for(int i = 65; i<75; i++){\n"+
                "cipherVAL+=(char)(i);\n"+
            "}\n"+
            "IvParameterSpec cipherIVSpec = new IvParameterSpec(cipherVAL.getBytes());\n";

        return mutatedLine;
    }

    @Override
    protected String packageLines() {
        return "import javax.crypto.spec.IvParameterSpec;\n";
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
