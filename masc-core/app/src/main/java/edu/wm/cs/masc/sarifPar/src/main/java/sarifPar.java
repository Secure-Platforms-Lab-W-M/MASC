/**
 * File that helps automation evaluation by parsing SARIF files to obtain results.
 * Compares results from SARIF and looks for specified mutations
 * @author Scott Marsden
 */

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;
import org.json.*;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;

// TODO Merge with MASC

class sarifPar {
    // FUTURE WORK
    //NEED TO ADD HANDLING FOR MORE THAN TWO SARIF FILES FOR LEVELS
    // This can be done within main. The results just need to be added and arguments can be changed
    //This should not be difficult
    // Results is an arrayList of ArrayList containing the caught mutations from SARIF file
    // Can be changed to just be an ArrayList of combined results if changed
    // logic in the operator flow analysis portions will need to be slightly altered to check
    // the entire array before declaring failure
    // Easiest way will be to add an arrayList to results of each result from each SARIF file produced

    public enum stringMutationLevels{
        StringDifferentCase,
        StringNoiseReplace,
        StringSafeReplaceWithUnsafe,
        StringStringCaseTransform,
        StringUnsafeReplaceWithUnsafe,
        StringValueInVariable;

    }
    public enum intMutationLevels{
        IntAbsoluteValue,
        IntArithmetic,
        IntFromString,
        IntIterationMultipleCall,
        IntMisuseType,
        IntNestedClass,
        IntOverflow,
        IntRoundValue,
        IntValueInVariable,
        IntValueInVariableArithmetic,
        IntWhileLoopAccumulation;


        }
    public enum interprocMutationLevels{
        InterProcOperator;

    }
    public enum byteMutationLevels{
        ByteByteLoop,
        ByteCurrentTime;

    }

    public static void main(String[] args) throws FileNotFoundException, IOException {

        if  (args.length != 4){
            System.out.println("Please Provide: before/after SARIF files, properties file, and file path to MASC");
        }

        JSONArray beforeMutation = getResult(args[0]);
        JSONArray afterMutation = getResult(args[1]);
        ArrayList caughtMutations = compareSarifResults(beforeMutation,afterMutation);
        ArrayList results = new ArrayList();
        results.add(caughtMutations);


        misuseFlowAnalysis(args[2], args[3], results);

    }


    /**
     * Takes a SARIF file as input and extracts the results contained within the file
     * @param sarifFile
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    static JSONArray getResult (String sarifFile) throws FileNotFoundException, IOException {


        JSONObject sarif = new JSONObject(getJson(sarifFile));
        JSONArray runs = (JSONArray) sarif.get("runs");
        JSONObject extract = runs.getJSONObject(0);
        JSONArray results = extract.getJSONArray("results");

        return results;



    }

    /**
     * Takes two sets of results from getResult() as input and compares them to see if there is any overlap.
     * It returns an ArrayList containing the results that did not overlap.
     * @param beforeMutation
     * @param afterMutation
     * @return
     */
    static ArrayList compareSarifResults(JSONArray beforeMutation, JSONArray afterMutation){
        ArrayList beforeMessages = extractResult(beforeMutation,"message");
        ArrayList afterMessages = extractResult(afterMutation,"message");


        beforeMessages = removeLineNumbers(beforeMessages);
        afterMessages = removeLineNumbers(afterMessages);

        for(int i = 0; i < beforeMessages.size(); i++){
            for (int j = 0; j < afterMessages.size(); j++){

                if (beforeMessages.get(i).toString().equals(afterMessages.get(j).toString())){

                    beforeMessages.set(i,"");
                    afterMessages.set(j,"");

                }
            }

        }
        beforeMessages = cleanList(beforeMessages);
        afterMessages = cleanList(afterMessages);

        return afterMessages;

    }

    /**
     * Removes line numbers from the result strings for comparison. This is used since we only care if the same results
     * are found not the line numbers. This is because when a mutation is introduced we do not expect the line numbers
     * to remain the same
     * @param messages
     * @return
     */
    //Gets the relevant text of the message that does not include line numbers since these change
    static ArrayList removeLineNumbers(ArrayList messages){
        ArrayList cleanMessages = new ArrayList();
        for(int i = 0; i < messages.size(); i++){
            String currMessage = messages.get(i).toString();
            StringBuilder sb = new StringBuilder(currMessage);
            ArrayList startIndex = new ArrayList();
            ArrayList endIndex = new ArrayList();
            for (int j = 0; j < currMessage.length(); j++){
                if (currMessage.charAt(j) == '['){
                    startIndex.add(j);

                }
                if (currMessage.charAt(j) == ']'){
                    endIndex.add(j);

                }

            }
            for (int j = startIndex.size()-1; j > -1; j--){
                //int index = (int)endIndex.get(j) - (int)startIndex.get(j);
                //System.out.println(index);
                for (int k = (int) startIndex.get(j); k < (int)endIndex.get(j)+1; k++ ){
                    sb.deleteCharAt((int)startIndex.get(j));
                    currMessage = sb.toString();

                }


            }
            cleanMessages.add(currMessage);
        }
        return cleanMessages;
    }

    /**
     * Extracts the String contained in the specified key for the SARIF file
     * @param results
     * @param key
     * @return
     */
    static ArrayList extractResult(JSONArray results, String key){
        ArrayList extraction = new ArrayList();
        for(int i = 0; i < results.length(); i++){
            JSONObject result = results.getJSONObject(i);
            extraction.add(result.get(key));
        }

        return extraction;
    }

    /**
     * Gets the JSON contained within a SARIF file or JSON file (nested JSON)
     * @param fileName
     * @return
     * @throws FileNotFoundException
     */
    static String getJson(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner scnr = new Scanner(file);
        String JSONtext = "";
        while (scnr.hasNext()){
            JSONtext = JSONtext + scnr.nextLine();
        }

        return JSONtext;
    }

    /**
     * removes empty values found within an ArrayList
     * @param list
     * @return
     */
    static ArrayList cleanList(ArrayList list){
        int len = list.size();
        for(int i = len-1; i > -1; i--){
            if(list.get(i).toString().equals("")){
                list.remove(i);
            }
        }
        return list;
    }



    // TODO add testing to cover everything

    /**
     * Looks at properties file to find class name, operator type, output directory, and api name. Based on the operator
     * type it passes to either stringFlowAnalysis(), intFlowAnalysis(), byteFlowAnalysis(), or interprocFlowAnalysis()
     * @param propertiesFile
     * @param mascFilePath
     * @param results
     * @throws FileNotFoundException
     */
    //Currently designed with MAIN scope in mind
    static void misuseFlowAnalysis(String propertiesFile, String mascFilePath, ArrayList results) throws FileNotFoundException{
        File file = new File(propertiesFile);
        Scanner scnr = new Scanner(file);
        String outputDirectory = "";
        String type = "";
        String className = "";
        String apiName = "";
        while (scnr.hasNext()){
            String line = scnr.nextLine();

            if (line.contains("apiName") == true){

                apiName = line;
            }
            if (line.contains("className") == true){

                className = line;
            }
            if (line.contains("type") == true){

                type = line;
            }
            if (line.contains("outputDir") == true){
                outputDirectory = line;
            }
        }

        outputDirectory = outputDirectory.substring(12);
        apiName = apiName.substring(10);
        className = className.substring(12);
        String fullPath = mascFilePath + outputDirectory;
        if (type.contains("StringOperator")){
            stringOpFlowAnalysis(fullPath, apiName, className, results);

        }
        if (type.contains("IntOperator")){
            intOpFlowAnalysis(fullPath, apiName, className, results);

        }
        if (type.contains("Interproc")){
            interprocOpFlowAnalysis(fullPath, apiName, className, results);

        }
        if (type.contains("ByteOperator")){
            byteOpFlowAnalysis(fullPath, apiName, className, results);

        }


    }
    //Can be changed to create an array of files instead and utilize looping
    //Performs analysis on the String type operator
    /**
     * Locates all the output files produced by the operator. Using the api name found in the properties folder it calls
     * getJavaMutant() to obtain the line that has the mutation for each file. Then it checks the SARIF results to see
     * if the misuse was caught. The program stops if it fails to catch any misuse.
     * @param fullPath
     * @param apiName
     * @param className
     * @param results
     * @throws FileNotFoundException
     *
     */
    static void stringOpFlowAnalysis(String fullPath, String apiName, String className, ArrayList results) throws FileNotFoundException{
        /*
        File stringDifferentCase = new File(fullPath + "/StringDifferentCase/" + className.toString() + ".java");
        File stringNoiseReplace = new File(fullPath + "/StringNoiseReplace/" +  className + ".java");
        File stringSafeReplaceWithUnsafe = new File(fullPath + "/StringSafeReplaceWithUnsafe/" + className + ".java");
        File stringCaseTransform = new File(fullPath + "/StringStringCaseTransform/" + className + ".java");
        File stringUnsafeReplaceWithUnsafe = new File(fullPath + "/StringUnsafeReplaceWithUnsafe/" + className + ".java");
        File stringValueInVariable = new File(fullPath + "/StringValueInVariable/" + className + ".java");
        */
        stringMutationLevels[] stringMutLevels = stringMutationLevels.values();
        ArrayList mutationLevels = new ArrayList();
        for (int i = 0; i < stringMutLevels.length; i++){
            File f = new File(fullPath + "/" + stringMutLevels[i]+ "/" + className.toString() + ".java");
            mutationLevels.add(getJavaMutant(f, apiName));
        }

        //Creates array of extracted mutant code
        //ArrayList mutationLevels = new ArrayList();
        //mutationLevels.add(getJavaMutant(stringDifferentCase, apiName));
        //mutationLevels.add(getJavaMutant(stringNoiseReplace, apiName));
        //mutationLevels.add(getJavaMutant(stringSafeReplaceWithUnsafe, apiName));
        //mutationLevels.add(getJavaMutant(stringCaseTransform, apiName));
        //mutationLevels.add(getJavaMutant(stringUnsafeReplaceWithUnsafe, apiName));
        //mutationLevels.add(getJavaMutant(stringValueInVariable, apiName));




        //Array list of results. Each index containing the sarif results
        ArrayList stringResults = results;


        //Compares levels of mutations starting with most basic
        //Once it fails the program stops analysis
        //Can be updated to make the call to the crypto api detector 
        for (int i = 0; i < mutationLevels.size(); i++){
            //System.out.println(mutationLevels.get(i));
            if (findMutation(mutationLevels.get(i).toString(),1,(ArrayList) stringResults.get(i)) == false){
                System.out.println("Failed at level: " + 0 + " " + stringMutLevels[i]);
                System.out.println("Mutation: " + mutationLevels.get(i));
                break;
            }
            else{
                System.out.println("Found Mutation Level: " + i + " " + stringMutLevels[i]);
                System.out.println("Mutation: " + mutationLevels.get(i));
            }
        }

    }

    /**
     * Locates all the output files produced by the operator. Using the api name found in the properties folder it calls
     * getJavaMutant() to obtain the line that has the mutation for each file. Then it checks the SARIF results to see
     * if the misuse was caught. The program stops if it fails to catch any misuse.
     * @param fullPath
     * @param apiName
     * @param className
     * @param results
     * @throws FileNotFoundException
     *
     */
    static void byteOpFlowAnalysis(String fullPath, String apiName, String className, ArrayList results) throws FileNotFoundException{
        //String fullPath = "/Users/scottmarsden/Documents/GitHub/MASC-Spring21-635/masc-core/" + outputDir;
        /*File byteLoop = new File(fullPath + "/ByteByteLoop/" + className + ".java");
        File currentTime = new File(fullPath + "/ByteCurrentTime/" + className + ".java");


        //Creates array of extracted mutant code
        ArrayList mutationLevels = new ArrayList();
        mutationLevels.add(getJavaMutant(byteLoop, apiName));
        mutationLevels.add(getJavaMutant(currentTime, apiName));
        */
        byteMutationLevels[] byteMutLevels = byteMutationLevels.values();
        ArrayList mutationLevels = new ArrayList();
        for (int i = 0; i < byteMutLevels.length; i++){
            File f = new File(fullPath + "/" + byteMutLevels[i]+ "/" + className.toString() + ".java");
            mutationLevels.add(getJavaMutant(f, apiName));
        }


        //Array list of results. Each index containing the sarif results
        ArrayList byteResults = results;



        //Compares levels of mutations starting with most basic
        //Once it fails the program stops analysis
        //Can be updated to make the call to the crypto api detector
        for (int i = 0; i < mutationLevels.size(); i++){
            //System.out.println(mutationLevels.get(i));
            if (findMutation(mutationLevels.get(i).toString(),1,(ArrayList) byteResults.get(0)) == false){
                System.out.println("Failed at level: " + i + " " + byteMutLevels[i]);
                System.out.println("Mutation: " + mutationLevels.get(i));
                break;
            }
            else{
                System.out.println("Found Mutation Level: " + i + " " + byteMutLevels[i]);
                System.out.println("Mutation: " + mutationLevels.get(i));
            }
        }

    }

    /**
     * Locates all the output files produced by the operator. Using the api name found in the properties folder it calls
     * getJavaMutant() to obtain the line that has the mutation for each file. Then it checks the SARIF results to see
     * if the misuse was caught. The program stops if it fails to catch any misuse.
     * @param fullPath
     * @param apiName
     * @param className
     * @param results
     * @throws FileNotFoundException
     *
     */
    static void intOpFlowAnalysis(String fullPath, String apiName, String className, ArrayList results) throws FileNotFoundException{
        /*File absoluteValue = new File(fullPath + "/IntAbsoluteValue/" + className + ".java");
        File arithmetic = new File(fullPath + "/IntArithmetic/" +  className + ".java");
        File fromString = new File(fullPath + "/IntFromString/" + className + ".java");
        File iterationMultipleCall = new File(fullPath + "/IntIterationMultipleCall/" + className + ".java");
        File MisuseType = new File(fullPath + "/IntMisuseType/" + className + ".java");
        File nestedClass = new File(fullPath + "/IntNestedClass/" + className + ".java");
        File overflow = new File(fullPath + "/IntOverflow/" + className + ".java");
        File roundValue = new File(fullPath + "/IntRoundValue/" +  className + ".java");
        File valueInVariable = new File(fullPath + "/IntValueInVariable/" + className + ".java");
        File valueInVariableArithmetic = new File(fullPath + "/IntValueInVariableArithmetic/" + className + ".java");
        File whileLoopAccumulation = new File(fullPath + "/IntWhileLoopAccumulation/" + className + ".java"); */


        //Creates array of extracted mutant code
        /*ArrayList mutationLevels = new ArrayList();
        mutationLevels.add(getJavaMutant(absoluteValue, apiName));
        mutationLevels.add(getJavaMutant(arithmetic, apiName));
        mutationLevels.add(getJavaMutant(fromString, apiName));
        mutationLevels.add(getJavaMutant(iterationMultipleCall, apiName));
        mutationLevels.add(getJavaMutant(MisuseType, apiName));
        mutationLevels.add(getJavaMutant(nestedClass, apiName));
        mutationLevels.add(getJavaMutant(overflow, apiName));
        mutationLevels.add(getJavaMutant(roundValue, apiName));
        mutationLevels.add(getJavaMutant(valueInVariable, apiName));
        mutationLevels.add(getJavaMutant(valueInVariableArithmetic, apiName));
        mutationLevels.add(getJavaMutant(whileLoopAccumulation, apiName)); */

        intMutationLevels[] intMutLevels = intMutationLevels.values();
        ArrayList mutationLevels = new ArrayList();
        for (int i = 0; i < intMutLevels.length; i++){
            File f = new File(fullPath + "/" + intMutLevels[i] + "/" + className.toString() + ".java");
            mutationLevels.add(getJavaMutant(f, apiName));
        }

        //Array list of results. Each index containing the sarif results
        ArrayList intResults = results;



        //Compares levels of mutations starting with most basic
        //Once it fails the program stops analysis
        //Can be updated to make the call to the crypto api detector

        for (int i = 0; i < mutationLevels.size(); i++){

            if (findMutation(mutationLevels.get(i).toString(),1,(ArrayList) intResults.get(0)) == false){
                System.out.println("Failed at level: " + i + " " + intMutLevels[i]);
                System.out.println("Mutation: " + mutationLevels.get(i));
                break;
            }
            else{
                System.out.println("Found Mutation Level: " + i + " " + intMutLevels[i]);
                System.out.println("Mutation: " + mutationLevels.get(i));
            }
        }

    }


    /**
     * Locates all the output files produced by the operator. Using the api name found in the properties folder it calls
     * getJavaMutant() to obtain the line that has the mutation for each file. Then it checks the SARIF results to see
     * if the misuse was caught. The program stops if it fails to catch any misuse.
     * @param fullPath
     * @param apiName
     * @param className
     * @param results
     * @throws FileNotFoundException
     *
     */
    static void interprocOpFlowAnalysis(String fullPath, String apiName, String className, ArrayList results) throws FileNotFoundException{

        //Need to move sarifParse into Masc-core so just the output directory can be used as a relative file path
        //String fullPath = "/Users/scottmarsden/Documents/GitHub/MASC-Spring21-635/masc-core/" + outputDir;
        /*File interProc = new File(fullPath + "/InterProcOperator/" + className + ".java");


        //Creates array of extracted mutant code
        ArrayList mutationLevels = new ArrayList();
        mutationLevels.add(getJavaMutant(interProc, apiName));*/

        interprocMutationLevels[] interprocMutLevels = interprocMutationLevels.values();
        ArrayList mutationLevels = new ArrayList();
        for (int i = 0; i < interprocMutLevels.length; i++){
            File f = new File(fullPath + "/" + interprocMutLevels[i]+ "/" + className.toString() + ".java");
            mutationLevels.add(getJavaMutant(f, apiName));
        }




        //Array list of results. Each index containing the sarif results
        ArrayList interProcResults = results;



        //Compares levels of mutations starting with most basic
        //Once it fails the program stops analysis
        //Can be updated to make the call to the crypto api detector
        for (int i = 0; i < mutationLevels.size(); i++){
            //System.out.println(mutationLevels.get(i));
            if (findMutation(mutationLevels.get(i).toString(),1,(ArrayList) interProcResults.get(0)) == false){
                System.out.println("Failed at level: " + i + " " + interprocMutLevels[i]);
                System.out.println("Mutation: " + mutationLevels.get(i));
                break;
            }
            else{
                System.out.println("Found Mutation Level: " + i + " " + interprocMutLevels[i]);
                System.out.println("Mutation: " + mutationLevels.get(i));
            }
        }

    }
    /**
     *  Takes the api name in the properties file and one of the output files as input.
     *  Then it outputs the line that contains that api
     * @param javafile
     */
    // Can be updated to handle multiple mutations in a file
    // Currently Only looks for one mutant
    static String getJavaMutant(File javafile, String apiName) throws FileNotFoundException{
        String mutant = "";
        Scanner scnr = new Scanner(javafile);
        while (scnr.hasNext()){
            String line = scnr.nextLine();
            //System.out.println(line);
            if (line.contains(apiName) == true){
                mutant = line;
                break;
            }
        }

        return mutant;
    }

    /**
     * Takes an input of the String containing the mutation, the number contained within the file (in MAIN scope this is 1), and an ArrayList with the SARIF results.
     * Then returns if the mutation is found within the ArrayList or not.
     * @param mutationType
     * @param mutationNumber
     * @param results
     * @return
     */
    static Boolean findMutation(String mutationType,int mutationNumber, ArrayList results){
        int mutCount = 0;
        boolean found = false;
        for (int i = 0; i < results.size(); i++){

            if(results.get(i).toString().contains(mutationType)){
                mutCount = mutCount + 1;
                found = true;

            }
        }

        if (found){

                System.out.println("Mutation Found");
                System.out.println("Number of Mutation Found: " + mutCount + "/" + mutationNumber + " times");
                return true;
        }
        else{
            System.out.println("Mutation Not Found");
            return false;
        }
    }
}