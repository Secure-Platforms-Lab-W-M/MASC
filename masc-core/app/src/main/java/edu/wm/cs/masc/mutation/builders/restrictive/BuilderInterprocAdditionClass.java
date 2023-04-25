package edu.wm.cs.masc.mutation.builders.restrictive;

import edu.wm.cs.masc.mutation.properties.InterprocProperties;

public class BuilderInterprocAdditionClass {
    public static String getInterprocClassString(InterprocProperties p){

        StringBuilder sb = new StringBuilder();
//        if (p.getImports() != null)
//            for (String line : p.getImports()) {
//                sb.append(line).append("\n");
//            }
        String insecure = p.getInsecureParam();

        String classStart =
                "public class %1$s {\n" +
                        "    private String %2$s = \"%3$s\";\n" +
                        "    public %1$s A(){\n" +
                        "        %2$s = \"%3$s\";\n" +
                        "        return this;\n" +
                        "    }\n";
                        /*"    public %1$s B(){\n" +
                        "        return \"%4$s\";\n" +
                        "    }\n" +
                        "    public %1$s C(){\n" +
                        "        return \"%5$s\";\n" +
                        "    }\n" +
                        "    public %1$s D(){\n" +
                        "        return \"%6$s\";\n" +
                        "    }\n" +
                        "    public String add%2$s(){\n" +
                        "        %2$s = B() + C() + D();\n" +
                        "        return this;\n" +
                        "    }\n" + */
        String addMethods = "";
        String methods = "";
                        for (int i = 0; i < insecure.length(); i++){
                            methods = methods +
                                    "    public String " + insecure.charAt(i) + "(){\n" +
                                    "        return \"" +  insecure.charAt(i) + "\";\n" +
                                    "    }\n";
                            addMethods = addMethods + insecure.charAt(i) + "() + ";

                        }
                        addMethods = addMethods.substring(0,addMethods.length() - 3);
                        methods = methods +
                                "    public String add(){\n" +
                                "        %2$s = " + addMethods + ";\n" +
                                "        return this;\n" +
                                "    }\n" +
                                "    public String get%2$s(){\n" +
                                "        return %2$s;\n" +
                                "    }\n" +
                                "}";

        sb.append(String.format(classStart + methods, p.getOtherClassName(), p.getPropertyName(), p.getSecureParam()));
        return sb.toString();

    }


}
