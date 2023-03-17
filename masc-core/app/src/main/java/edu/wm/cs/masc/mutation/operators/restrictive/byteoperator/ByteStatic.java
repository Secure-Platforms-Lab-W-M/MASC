package edu.wm.cs.masc.mutation.operators.restrictive.byteoperator;

import edu.wm.cs.masc.mutation.properties.ByteOperatorProperties;

public class ByteStatic extends AByteOperator{

    public ByteStatic(ByteOperatorProperties p) {
        super(p);
    }

    //* Note: Reusing the tempVariableName. This can instantiate
    // misuses that reuse IVs for example. We simply alter the
    // api_variable and reuse the tempVariable. This file has a
    // corresponding test file ByteReuseTest.java. The test passes!

    @Override
    public String mutation() {
        return

                "Byte[] " + tempVariableName + " = \"12345678\".getBytes();\n" +
                api_name + " " + api_variable + " = new " + api_name + "." + invocation + "(" + tempVariableName + ",\"" + insecureParam + "\");";


    }


}
