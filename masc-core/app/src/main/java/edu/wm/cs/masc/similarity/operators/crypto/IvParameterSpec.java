package edu.wm.cs.masc.similarity.operators.crypto;

import edu.wm.cs.masc.similarity.operators.CryptoMuations.ASimilarityContext;
import edu.wm.cs.masc.similarity.operators.CryptoMuations.IvParameterSpecContext;

public class IvParameterSpec extends ACryptoMutationOperator {
    public ASimilarityContext iv = new IvParameterSpecContext("DefaultSimilarityByteOperator.properties");
    @Override
    protected String getMutatedLine() {

//        String mutatedLine="String cipherVAL=\"\";\n"+
//            "for(int i = 65; i<75; i++){\n"+
//                "cipherVAL+=(char)(i);\n"+
//            "}\n"+
//            "IvParameterSpec cipherIVSpec = new IvParameterSpec(cipherVAL.getBytes());\n";

        return iv.mutation();
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
