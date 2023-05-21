package edu.wm.cs.masc.sensitivityRunner;


import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    //private static Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args) throws Exception {
        //Create default input option for each sensitivity
        System.out.println("Hello world!");
        //MASC masc = new MASC();
    //Ask for type of flow sensitivity
        //The outputdir
        //scope
        //api name
        //secure param
        //insecure param
        //noise
        //variable name
        //class name
        //property name
        //excluded operators
        if (args.length == 0) {
            System.out.println("No file supplied");
        } else if (args.length > 1) {
            System.out.println("Too many arguments were provided");
        //} else if (!args[0].endsWith(".properties")) {
        //    System.out.println("Properties file must end with the .properties extension");
        } else {
            HashMap userSelections = new HashMap();
            File props = new File(args[0]);
            //props = new File("/Users/scottmarsden/Documents/GitHub/MASC/sensitivity-runner/src/main/java/org/example/input.txt");
            Scanner scnr = new Scanner(props);
            while (scnr.hasNext()){

                String line = scnr.nextLine();

                String keyValue[] = line.split("=");
                if (keyValue.length == 2){
                    userSelections.put(keyValue[0].trim(),keyValue[1].trim());
                }

            }
            //System.out.println(userSelections);


            if (userSelections.get("sensitivity").equals("flow")){
                flowSensitivity flow = new flowSensitivity();
                userSelections.remove("sensitivity");
                flow.runFlowSensitivity(userSelections);
                flow.callMASC();

            }
            else if (userSelections.get("sensitivity").equals("context")){
                contextSensitivity context = new contextSensitivity();
                userSelections.remove("sensitivity");
                context.runContextSensitivity(userSelections);
                context.callMASC();

            }
            else if (userSelections.get("sensitivity").equals("object")){
                objectSensitivity object = new objectSensitivity();
                userSelections.remove("sensitivity");
                object.runObjectSensitivity(userSelections);
                object.callMASC();

            }
            else if (userSelections.get("sensitivity").equals("path")){
                pathSensitivity path = new pathSensitivity();
                userSelections.remove("sensitivity");
                path.runPathSensitivity(userSelections);
                path.callMASC();

            }
            else if (userSelections.get("sensitivity").equals("alias")){
                aliasSensitivity alias = new aliasSensitivity();
                userSelections.remove("sensitivity");
                alias.runAliasSensitivity(userSelections);
                alias.callMASC();

            }
            else if (userSelections.get("sensitivity").equals("field")){
                fieldSensitivity alias = new fieldSensitivity();
                userSelections.remove("sensitivity");
                alias.runFieldSensitivity(userSelections);
                alias.callMASC();

            }
            else{
                System.out.println("Please provide a supported sensitivity type: flow, context, object, field, path, or alias");
            }

        }
    }


}