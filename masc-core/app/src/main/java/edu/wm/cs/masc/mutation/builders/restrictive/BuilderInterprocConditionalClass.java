package edu.wm.cs.masc.mutation.builders.restrictive;

import edu.wm.cs.masc.mutation.properties.InterprocProperties;

public class BuilderInterprocConditionalClass {

    public static String getInterprocClassString(InterprocProperties p){
        StringBuilder sb = new StringBuilder();
//        if (p.getImports() != null)
//            for (String line : p.getImports()) {
//                sb.append(line).append("\n");
//            }
        String template =
                "public class %1$s {\n" +
                        "    private String %2$s = \"%3$s\";\n" +
                        "    private int x = 1;\n" +
                        "    public %1$s A(){\n" +
                        "        if (x = 1) {\n" +
                        "        %2$s = \"%3$s\";\n" +
                        "        return this;\n" +
                        "        }\n" +
                        "        else {\n" +
                        "        %2$s = \"%4$s\";\n" +
                        "        return this;\n" +
                        "        }\n" +
                        "    }\n" +
                        "    public int changeX(){\n" +
                        "        x = 0;\n" +
                        "        return x;\n" +
                        "    }\n" +
                        "    public String get%2$s(){\n" +
                        "        return %2$s;\n" +
                        "    }\n" +
                        "}";

        sb.append(String.format(template, p.getOtherClassName(), p.getPropertyName(), p.getSecureParam(),
                p.getInsecureParam()));
        return sb.toString();

    }
}
