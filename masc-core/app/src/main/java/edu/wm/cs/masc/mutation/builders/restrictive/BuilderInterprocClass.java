package edu.wm.cs.masc.mutation.builders.restrictive;

import edu.wm.cs.masc.mutation.properties.InterprocProperties;

public class BuilderInterprocClass {
    public static String getInterprocClassString(InterprocProperties p){
        StringBuilder sb = new StringBuilder();
//        if (p.getImports() != null)
//            for (String line : p.getImports()) {
//                sb.append(line).append("\n");
//            }
        String classStart =
                "public class %1$s {\n" +
                        "    private String %2$s = \"%3$s\";\n";
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
        String methods = "";
        for (int i = 0; i < Integer.parseInt(p.getIterationCount()); i++){
            if (i % 2 == 0) {
                methods = methods +
                        "    public %1$s A" + i/2 + "(){\n" +
                        "        %2$s = \"%3$s\";\n" +
                        "        return this;\n" +
                        "    }\n";
            }
            else {
                methods = methods +
                        "    public %1$s B" + i/2 + "(){\n" +
                        "        %2$s = \"%4$s\";\n" +
                        "        return this;\n" +
                        "    }\n";
            }

        }
        String getMethod =  "    public String get%2$s(){\n" +
                            "        return %2$s;\n" +
                            "    }\n" +
                            "}";

        String template = classStart + methods + getMethod;
        sb.append(String.format(template, p.getOtherClassName(), p.getPropertyName(), p.getSecureParam(),
                p.getInsecureParam()));
        return sb.toString();

    }



}
