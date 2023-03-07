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
                userSelections.put(keyValue[0].trim(),keyValue[1].trim());
            }
            //System.out.println(userSelections);


            if (userSelections.get("sensitivity").equals("flow")){
                flowSensitivity flow = new flowSensitivity();
                userSelections.remove("sensitivity");
                flow.runFlowSensitivity(userSelections);
                flow.callMASC();

            }

        }
    }


}