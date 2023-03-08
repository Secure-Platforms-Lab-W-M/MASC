package edu.wm.cs.masc.mutation.builders.restrictive;

import edu.wm.cs.masc.mutation.properties.InterprocProperties;

public class BuilderNestedConditionalIterations {
    public static String getInterprocClassString(InterprocProperties p){
        StringBuilder sb = new StringBuilder();
//        if (p.getImports() != null)
//            for (String line : p.getImports()) {
//                sb.append(line).append("\n");
//            }
        String classStart =
                "public class %1$s {\n" +
                        "    private String %2$s = \"%3$s\";\n" +
                        "    private int n = 0;\n";
        //"    public %1$s A(){\n" +
        //"        %2$s = \"%3$s\";\n" +
        //"        return this;\n" +
        //"    }\n" +
        //"    public %1$s B(){\n" +
        //"        %2$s = \"%4$s\";\n" +
        //"        return this;\n" +
        //"    }\n" +
        //"    public String get%2$s(){\n" +
        //"        return %2$s;\n" +
        //"    }\n" +
        //"}";

        //for (int i = 0; i < Integer.parseInt(p.getIterationCount()); i++){

            String conditionalMethod = "    public %1$s A(){\n" +
                    createConditional(Integer.parseInt(p.getIterationCount()), "") +
                    "        return this;\n" +
                    "    }\n";
            //if (i == Integer.parseInt(p.getIterationCount() )- 1){

            //}
            //conditionals = conditionals +






        String getMethod =  "    public String get%2$s(){\n" +
                "        return %2$s;\n" +
                "    }\n" +
                "}";

        String template = classStart + conditionalMethod + getMethod;
        sb.append(String.format(template, p.getOtherClassName(), p.getPropertyName(), p.getSecureParam(),
                p.getInsecureParam()));
        return sb.toString();

    }

    public static String createConditional(int iterations, String whiteSpace) {
        String conditional = "";
        if (iterations == 1) {
             conditional = whiteSpace + "    if (n == 0) {\n" +
                     whiteSpace + "        %2$s = \"%4$s\";\n" +
                     whiteSpace + "    }\n" +
                     whiteSpace + "    else {\n" +
                     whiteSpace + "        %2$s = \"%3$s\";\n" +
                     whiteSpace + "    }\n";
             return conditional;
        }
        conditional = whiteSpace + "    if (n == 0) {\n" +
                createConditional(iterations - 1,whiteSpace + "    ") +
                whiteSpace + "    }\n" +
                whiteSpace + "    else {\n" +
                whiteSpace + "        %2$s = \"%3$s\";\n" +
                whiteSpace + "    }\n";

        return conditional;

    }
}
