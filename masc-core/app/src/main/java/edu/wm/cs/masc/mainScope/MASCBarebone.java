package edu.wm.cs.masc.mainScope;

//public class MASCBarebone {

//  Deprecated -- call with Main.java instead
//    public static void main(String[] args) {
//        try {
//            if (args.length < 1) {
//
//                System.out.println(
//                        "No arguments provided. Running Cipher.properties by " +
//                                "default.");
//                run("IntOperatorPbeMisuse.properties");
//
//            } else if (args.length == 1) {
//                run(args[0]);
//            } else {
//                System.out.println(
//                        "Please run " + args[0] + "with .properties file as " +
//                                "argument.");
//            }
//        } catch (ConfigurationException e) {
//            System.out.println("Something wrong with the properties file.");
//            e.printStackTrace();
//        }
//    }
//
//    private static void run(String path) throws ConfigurationException {
//
//        PropertiesReader reader = new PropertiesReader(path);
//
//        AMutationMaker m = null;
//        String type = reader.getValueForAKey("type");
//        if (type.equalsIgnoreCase(RootOperatorType.StringOperator.name())) {
//            StringOperatorProperties properties =
//                    new StringOperatorProperties(
//                            path);
//            m = new StringOperatorMutationMaker(
//                    properties);
//            m.make(properties);
//
//        } else if (type
//                .equalsIgnoreCase(RootOperatorType.ByteOperator.name())) {
//            ByteOperatorProperties properties = new ByteOperatorProperties(
//                    path);
//            m = new ByteMutationMaker(properties);
//            m.make(properties);
//        } else if (type.equalsIgnoreCase(RootOperatorType.Interproc.name())) {
//            InterprocProperties properties = new InterprocProperties(path);
//            m = new InterprocMutationMaker(properties);
//            m.make(properties);
//        } else if (type.equalsIgnoreCase(RootOperatorType.IntOperator.name())){
//            IntOperatorProperties properties = new IntOperatorProperties(path);
//            m = new IntMutationMaker(properties);
//            m.make(properties);
//        } else if (type.equalsIgnoreCase(RootOperatorType.Flexible.name())) {
//            FlexibleOperatorProperties properties =
//                    new FlexibleOperatorProperties(
//                            path);
//            m = new FlexibleMutationMaker(properties);
//            m.make(properties);
//        }
//    }
//}