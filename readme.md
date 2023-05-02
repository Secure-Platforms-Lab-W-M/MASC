# MASC 
MASC (Mutation Analysis for Evaluating Static Crypto-API Misuse Detectors) is a framework that enables a systematic and data-driven evaluation of crypto-API misuse detectors (or Crypto-detectors) using mutation testing. Backed up by a data-driven taxonomy of 105 misuse cases of existing crypto-API, the tool can generate thousands of insecure mutants by injecting instantiations of these misuse cases in Java or Android apps. These mutants can be used to test and debug cryptographic API misuse detectors.

# Demonstration
For a more detailed explanation of the concepts behind MASC and a step-by-step guide to its features, check out this video demonstration [here](https://www.youtube.com/watch?v=ZKzUnBXGla0).

# Usage
## Installation
The following steps can be followed to setup and use MASC. 

### Environment setup
MASC runs on java 11. So the first step is to install Java 11.
 
#### Windows
1. Visit [this link](https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html) to download Java 11.0.14 from Oracle website.
2. Next, run the installer and follow the on screen instructions to complete the installation.
3. Now add java to environment variables by: Right Click -> My Computer(This PC) -> Properties -> Advanced System Settings
4. Now click on Environment Variables, select Path under System Variables section and click on Edit. 
5. Add the path of installed JDK to system Path. Save and exit. 
6. Open command prompt and run java -version to test installation.

#### Ubuntu
1. Simply run the command: sudo apt-get install openjdk-11-jdk
2. Run java -version to test installation. 

### Running MASC
1. Clone the [Masc Repository](https://github.com/Secure-Platforms-Lab-W-M/MASC) from GitHub. Take note of where the cloned repository is saved on your machine. 
2. Open the clone repository, go to `masc-core`, and run  `gradlew shadowJar` to create a JAR file for MASC. The output JAR can be found at `masc-core > app > build > libs > app-all.jar`.
3. Test run with `java -jar masc.jar`. If you see the message “No properties file supplied”, it means installation has been completed. 

## Sample Configuration
MASC can be configured by supplying a  `.properties` file. All inputs are given to the tool through the properties file. Here is a sample configuration provided in the properties file - 
```
mutantGeneration = true
type = StringOperator
outputDir = app/outputs
scope = MAIN
className = CryptoTest
apiName = javax.crypto.Cipher

invocation = getInstance
secureParam = AES/GCM/NoPadding
insecureParam = AES
noise = ~
variableName = cryptoVariable
```
Here, the first 6 keys are mandatory, while the next 5 keys are optional. They depend on the type of operator specified in line 2. Order of these items in the properties file DO NOT matter. 

Depending on the type of operator (line 2) and the scope (line 4) the structure of the properties file may be very different. Examples of many different properties files are available in the project artifacts. 

For more configuration files, go [here](https://github.com/Secure-Platforms-Lab-W-M/MASC/tree/main/masc-core/app/src/main/resources). 

For a detailed guide on writing configuration files and what each parameters mean, check [this section](https://github.com/Secure-Platforms-Lab-W-M/MASC/blob/main/Documentation/user_manual.md#creating-a-properties-file) of the user manual. 

## Running MASC with the web interface

## Running MASC with the CLI

### Running MASC with Main Scope
Masc core seeds mutants at the beginning of the main method of a simple Java app developed by the authors. To run MASC core, save the configuration given above in a ```.properties``` file and run the command
```
java -jar path_to_jar path_to_properties_file
```
For instance, if the configuration is saved in  `Cipher.properties`, run:
```
java -jar masc.jar Cipher.properties
```

### Output
Check the output folder (as specified in the properties file). You will find n folders, each containing the output of an operator, which is a mutated application. Now, you can analyze these mutants by static analyzers of your choice manually or by using the automated analysis module of MASC as described [here](#automated-analysis). 

### Running MSSC with Scopes

#### Running MASC with Similarity Scope
The MDroidPlus Extension uses abstract syntax tree to seed instances of misuse cases at locations in a target application’s source code where a similar API is already being used, i.e., akin to modifying existing API usages and making them vulnerable. 
```sh
/Users/XXX/git/XXX/MDroidPlus/libs4ast/ /Users/XXX/workspaces/mutationbackyard/sources/car-report car-report /Users/XXX/workspaces/mutationbackyard/mutations/ /Users/XXX/workspaces/Android/operator/ false
### contents of operator.properties inside operator dir
601 =	edu.wm.cs.mplus.operators.crypto.CipherInstance
602 = 	edu.wm.cs.mplus.operators.crypto.RandomInt
603 =   edu.wm.cs.mplus.operators.crypto.IvParameterSpec
604 =   edu.wm.cs.mplus.operators.crypto.SSLContextInstance
605 =   edu.wm.cs.mplus.operators.crypto.MessageDigest
606 =   edu.wm.cs.mplus.operators.crypto.HostnameVerifierInstance
607 =   edu.wm.cs.mplus.operators.crypto.HttpsURLHostnameVerifier
608 =   edu.wm.cs.mplus.operators.crypto.TrustManagerInstance
```

#### Running MASC with Exhaustive Scope
The mSE Extension exhaustively seeds mutants at all locations in the target app’s code allowed by Java syntax rules, i.e., class definitions, conditional segments, method bodies as well as anonymous inner class object declarations. 
```sh
# receives runtime argument

/Users/XXX/git/XXX/output/templates/ActivityLauncherreach.properties
## contents of properties file
lib4ast: /Users/XXX/git/MDroidPlus/libs4ast
appSrc: /Users/XXX/git/XXX/activitylauncher-reset
operatorType: REACHABILITY

//REQUIRED FOR MUTATE
appName: ActivityLauncher
output: /Users/XXX/git/XXX/output/ActivityLauncher/reach/
```




### Extending MASC Main Scope with custom plugins
You write your own operators as plugins for MASC for **main scope**. 

MASC supports 6 types of operators - 5 predefined operator types plus one more for any custom operator type that does not fall within these five. You can write your own operators for each of the 6 types.

To write your own operators as plugins, these are the general steps you will need to follow - 

1. Write the code in a .java file 
2. Compile the code to get the .class file 
3. Place the .class file in /plugins/
4. Run the jar normally
5. Find the generated mutated apps in /app/outputs

#### Step 1 code your plugin
Here is a sample code for a String type operator:
```
package plugins;

import edu.wm.cs.masc.mutation.operators.restrictive.stringoperator.AStringOperator;
import edu.wm.cs.masc.mutation.properties.StringOperatorProperties;

public class MyStrOperatorPlugin extends AStringOperator {

    public MyStrOperatorPlugin(StringOperatorProperties properties) {
        super(properties);
    }

    @Override
    public String mutation() {
        // insert your code to generate the mutation here

        // here is a simple example
        // return "javax.crypto.Cipher.getInstance(\"aes\");";

        // here is a more realistic example
        StringBuilder s = new StringBuilder();
        s.append(api_name)
                .append(".")
                .append(invocation)
                .append("(\"")
                .append(insecureParam.toLowerCase())
                .append("\");");
        return s.toString();
    }
}
```

For more sample codes for plugins of different types, go [here](https://github.com/Secure-Platforms-Lab-W-M/MASC/tree/main/Documentation/plugins). 

For a detailed guide on writing your own plugins, check the user manual [here](https://github.com/Secure-Platforms-Lab-W-M/MASC/blob/main/Documentation/plugins_readme.md). 

#### Step 2. Compiling the code
Compile your code by adding the MASC.jar to classpath as such. Open a command prompt in folder where your code for the plugin is, and run this command: 
``` 
javac -cp directory/MASC.jar *.java   
```

#### Step 3. Placing the class file in /plugins/ folder
Place the  `.class` files in /plugins/ folder. 

#### Step 4. Running the jar
Run the jar normally: 
```
java -jar MASC.jar propertiesFileName.properties
```

#### Step 5. The output
Generated mutated apps will be produces in `app/output/`
Output from plugins will have `plugins.` prefixed in their names.

### Automated Analysis
MASC's automated analysis module automatically compiles mutated apps produced from the main scope, tests them using chosen Crypto-detectors, then identifies the killed and unkilled mutants. Although this feature is currently limited to Crypto-detectors that can output in the SARIF format, MASC's automated analysis now supports CogniCrypt even though it uses a custom format instead of SARIF.

#### Sample configuration
To run automated analysis, add the following to your properties file and run normally:
```
automatedAnalysis = true
toolName = find-sec-bugs
toolLocation = /home/yusuf/Downloads
toolRunCommand = bash findsecbugs.sh -high -sarif -output out.json {}
mutatedAppsLocation = app/outputs
codeCompileCommand = javac *.java
outputReportDirectory = /home/yusuf/Downloads
outputFileName = out.json
stopCondition = OnError
```

To run automated analysis only without mutant generation, add this line to the properties file:
```
mutantGeneration = false
```

If you want to run MASC's automated analysis for CogniCrypt, just add an extra field - 
 ```
 wrapper = CogniCrypt
 ```

# Related work
A. S. Ami, N. Cooper, K. Kafle, K. Moran, D. Poshyvanyk and A. Nadkarni, "Why Crypto-detectors Fail: A Systematic Evaluation of Cryptographic Misuse Detection Techniques," 2022 IEEE Symposium on Security and Privacy (SP), San Francisco, CA, USA, 2022, pp. 614-631, doi: [10.1109/SP46214.2022.9833582](
https://doi.org/10.1109/SP46214.2022.9833582).

# Developer documentation
To access the user manuals and High Level Architectural diagrams, please go [here](https://github.com/Secure-Platforms-Lab-W-M/MASC/tree/main/Documentation). 

