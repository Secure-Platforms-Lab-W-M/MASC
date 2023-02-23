package edu.wm.cs.masc.mutation.operators.restrictive.interprocoperator;

import edu.wm.cs.masc.mutation.properties.InterprocProperties;

//Builds class in same file as the method calls
public class BaseCase extends AInterProcOperator{
    public BaseCase(InterprocProperties p) {
        super(p);
    }
    /*
    Example:
    public class CipherPack {
        private String propertyName = "AES/GCM/NoPadding";
        public CipherPack A(){
            propertyName = "AES/GCM/NoPadding";
            return this;
        }
        public CipherPack B(){
            propertyName = "AES";
            return this;
        }
        public String getpropertyName(){
            return propertyName;
        }
    }

    public class CryptoTest {
        public static void main(java.lang.String[] args) throws java.lang.Exception {
            java.lang.System.out.println("Hello");
            javax.crypto.Cipher cryptoVariable = javax.crypto.Cipher.getInstance(new CipherPack().A().B().getpropertyName());
        }
    }
     */

    public String insecure_call() {
        StringBuilder sb = new StringBuilder();
        String template =
                "public class %1$s {\n" +
                        "    private String %2$s = \"%3$s\";\n" +
                        "    public %1$s A(){\n" +
                        "        %2$s = \"%3$s\";\n" +
                        "        return this;\n" +
                        "    }\n" +
                        "    public %1$s B(){\n" +
                        "        %2$s = \"%4$s\";\n" +
                        "        return this;\n" +
                        "    }\n" +
                        "    public String get%2$s(){\n" +
                        "        return %2$s;\n" +
                        "    }\n" +
                        "}\n";

        sb.append(String.format(template, otherClassName, propertyName, secureParam,
                insecureParam));
        sb.append(String
                .format("%1$s %2$s = " +
                                "%1$s.%3$s(new %4$s().A().B().get%5$s());" +
                                "\n",
                        //properties.getApiName(),
                        api_name,
                        //properties.getVariableName(),
                        variableName,
                        //properties.getInvocation(),
                        invocation,
                        //properties.getOtherClassName(),
                        otherClassName,
                        //properties.getPropertyName());
                        propertyName));
        return sb.toString();
        //return String
        //        .format("%1$s %2$s = " +
        //                        "%1$s.%3$s(new %4$s().B().get%5$s());" +
        //                        "\n",
        //                //properties.getApiName(),
        //                api_name,
        //                //properties.getVariableName(),
        //                variableName,
                        //properties.getInvocation(),
        //                invocation,
                        //properties.getOtherClassName(),
        //                otherClassName,
                        //properties.getPropertyName());
        //                propertyName);
    }

    public String insecure_call_trycatch() {
        return "try {\n" +
                this.insecure_call() +
                //"System.out.println(" + properties.getVariableName() +
                "System.out.println(" + variableName +
                ".getAlgorithm());\n" +
                "} catch (Exception e) {\n" +
                "System.out.println(\"Error\");\n" +
                "}";
    }

    @Override
    public String mutation() {
        if (try_catch) {
            return insecure_call_trycatch();
        } else
            return insecure_call();
    }
}
