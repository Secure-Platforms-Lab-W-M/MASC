# MASC User Manual <!-- omit from toc -->

This tool is an implementation of the MASC framework, as described in [this paper](https://arxiv.org/abs/2107.07065). MASC enables
a systematic and data-driven evaluation of crypto-detectors using mutation testing. MASC is grounded in a comprehensive view of the problem space by developing a data-driven taxonomy of existing crypto-API misuse, containing 105 misuse cases. The goal is to generate, with very little manual effort, thousands of insecure mutants by injecting instantiations of these misuse cases in Java or Android apps. These mutants can be used to test and debug cryptographic API misuse detectors.

## Table of contents <!-- omit from toc -->
- [Concepts](#concepts)
  - [The goal](#the-goal)
  - [Misuse cases](#misuse-cases)
  - [Mutation operators](#mutation-operators)
  - [Mutation scopes](#mutation-scopes)
- [Installation](#installation)
  - [Environment setup](#environment-setup)
    - [Windows](#windows)
    - [Ubuntu](#ubuntu)
  - [Running the executable jar](#running-the-executable-jar)
- [Creating a properties file](#creating-a-properties-file)
- [Writing and using your own plugins](#writing-and-using-your-own-plugins)
  - [The operator types](#the-operator-types)
  - [Step 1. Writing the code for a plugin](#step-1-writing-the-code-for-a-plugin)
    - [String operator](#string-operator)
    - [Int operator](#int-operator)
    - [Byte operator](#byte-operator)
    - [Interprocedural operator](#interprocedural-operator)
    - [Flexible operator](#flexible-operator)
    - [Custom operator](#custom-operator)
    - [Sample code](#sample-code)
  - [About the `mutation()` method](#about-the-mutation-method)
  - [About `CustomGenericOperator`](#about-customgenericoperator)
  - [Step 2. Compiling the code](#step-2-compiling-the-code)
  - [Step 3. Placing the class file in /plugins/ folder](#step-3-placing-the-class-file-in-plugins-folder)
  - [Step 4. Running the jar](#step-4-running-the-jar)
  - [Step 5. The output](#step-5-the-output)
  - [Step 6. Sample operators](#step-6-sample-operators)
- [Automated Analysis](#automated-analysis)
  - [How it works](#how-it-works)
  - [The extra info in the properties file](#the-extra-info-in-the-properties-file)
  - [Running MASC with automated analysis](#running-masc-with-automated-analysis)
  - [Errors and missing values](#errors-and-missing-values)
  - [Running only for mutant generation without automated analysis](#running-only-for-mutant-generation-without-automated-analysis)
  - [Running only for automated analysis without mutant generation](#running-only-for-automated-analysis-without-mutant-generation)
  - [Support for CogniCrypt users](#support-for-cognicrypt-users)
- [Running other MASC modules](#running-other-masc-modules)
  - [Running MASC for MDroidPlus Extension](#running-masc-for-mdroidplus-extension)
  - [Running MASC for mSE Extension](#running-masc-for-mse-extension)


## Concepts
Before proceeding any further, it is very important to understand the concept and some key terms used throughout the user manual. Let us start with what we are trying to achieve.

### The goal
What we are trying to achieve with MASC is to intelligently generate mutants to test and debug your  cryptographic API misuse detectors, (Crypto-detector). MASC makes generating quality mutants for mutation testing very easy, thanks to its **taxonomy of misuse cases**, **mutation operators**, and **mutation scopes**. Let us see in detail what each of these items in bold are. 

### Misuse cases
MASC is backed up by a taxonomy of 105 misuse cases derived from the last 20 years of research papers, industry tools and advisories. These misuse cases represent ways in which the security of an application could be compromised. For example, using a weak encryption algorithm like DES instead of AES, using RSA with a small key size, not using salts when hashing passwords, are all examples of misuse cases.

### Mutation operators 
Misuse cases can appear in a variety of different forms. Take the example of the misuse case, weak encryption algorithm i.e., using DES instead of AES. In java, one way to call AES encryption securely is this -
```
javax.crypto.Cipher.getInstance(“AES/GCM/NoPadding”);
```
But there are many different ways to make the call insecurely. In other words, there are many ways for the misuse case to be seen in real code. For example -
```
javax.crypto.Cipher.getInstance(“DES”);
```
```
javax.crypto.Cipher.getInstance(“des”);
```
```
String encryptionAlgorithm = “DES”;
```
```
javax.crypto.Cipher.getInstance(encryptionAlgorithm);
```
```
javax.crypto.Cipher.getInstance(“AES”.replace(“A”, “D”));
```
All these represent the same misuse case. From this, we can understand that there are many ways to instantiate the misuse cases. To represent all such instances without having to hard-code instantiations for every misuse case, we have **mutation operators**, i.e., functions or rules of transformations that can create misuse instances (i.e., mutants) by instantiating one or more misuse cases from the taxonomy.

### Mutation scopes
Upon creating mutants, the next challenge is to insert them in real Java applications. This is where mutation scopes come into play. Mutation scopes are abstractions that place the instantiated mutants at strategic locations within code. There are three mutation scopes -

1. Main Scope: The main scope is the simplest of the three, and seeds mutants at the beginning of the main method of a simple Java app developed by the authors. 
2. Selective / Similarity Scope: The similarity scope seeds security operators at locations in a target application’s source code where a similar API is already being used, i.e., akin to modifying existing API usages and making them vulnerable. 
3. Exhaustive Scope: As the name suggests, this scope exhaustively seeds mutants at all locations in the target app’s code allowed by Java syntax rules, i.e., class definitions, conditional segments, method bodies as well as anonymous inner class object declarations. 

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

### Running the executable jar
1. Clone the [Masc Repository](https://github.com/Secure-Platforms-Lab-W-M/MASC) from GitHub. Take note of where the cloned repository is saved on your machine. 
2. Open the clone repository, go to `masc-core`, and run  `gradlew shadowJar` to create a JAR file for MASC. The output JAR can be found at `masc-core > app > build > libs > app-all.jar`.
3. Test run with `java -jar masc.jar`. If you see the message “No properties file supplied”, it means installation has been completed. 

## Creating a properties file
The previous section ended with a message saying something about a properties file. In this section, we will see what this properties file is, and how to create one. 
Properties file is analogous to a configuration file. All inputs are given to the tool through the properties file. Here is a sample properties file - 
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
The first 6 keys are mandatory. Here is what they mean - 
- MutantGeneration: Whether you want mutants to be generated
- Type: The type of operators needed. Possible values are StringOperator, IntOperator, ByteOperator, Interproc, Flexible, and Custom.
- OutputDir: Output directory
- Scope: Main, selective, or exhaustive scope
- ClassName: Name of the class of output mutated apps (for main scope)
- ApiName: Name of the API to be invoked

The next 5 keys are not mandatory. They depend on the type of operator specified in line 2. The keys shown here are for StringOperator type only. The keys are self explanatory -
- Invocation: Name of the method invoked. 
- SecureParam: Secure parameter
- InsecureParam: Insecure parameter. 
- Noise: One operator may add this noise to the secure/insecure parameter. 
- VariableName: Name of a variable (needed by an operator).

Depending on the type of operator (line 2) and the scope (line 4) the structure of the properties file may be very different. Examples of many different properties files are available in the artifacts. To keep things simple, we will be using the properties file shown above throughout the rest of the manual.

Save the file as `Cipher.properties`. Next, run MASC with the following command - 
```
java -jar masc.jar Cipher.properties
```
Check the output folder (as specified in the properties file). You will find 6 folders, each containing a mutated application. Next, try other properties file from the artifacts to generate more mutants. You can now analyze these mutants by static analyzers of your choice. 

## Writing and using your own plugins
You can code and run your own operators as plugins for MASC for **main scope**. Let's see how it's done!

MASC supports 6 types of operators - 5 predefined operator types plus one more for any custom operator type that does not fall within these five. You can write your own operators for each of the 6 types.

To write your own operators as plugins, these are the general steps you will need to follow - 

1. Write the code in a .java file 
2. Compile the code to get the .class file 
3. Place the .class file in /plugins/
4. Run the jar normally
5. Find the generated mutated apps in /app/outputs

Let us see each of these steps in detail.

### The operator types

Running the jar requires normally requires a properties file to be supplied as command line argument. The properties file specifies the type of operator to be run like this - 
```
type = StringOperator
```

StringOperator is just one of the total 6 operator types. The other five are - IntOperator, ByteOperator, Interproc, Flexible, and Custom.

### Step 1. Writing the code for a plugin
The general structure for the code of a custom plugin looks like this - 

1. package plugins
2. Two mandatory import statements
3. Class definition extending/inheriting a specific Abstraction
4. One and only one specific constructor
5. Overriding  `mutation()` method - 


**About the imports -** Apart from the two mandatory imports, the class can have any other import statement as required by the user. 
**About the inheritance -** The plugin can be a part of a more complex inheritance tree if the user so wishes. It is fine as long as the plugin is a sub class of the specified Abstraction, even if it is deep down the inheritance tree from the abstraction.  

The import statements, the Abstraction and the constructor is different for each operator type. Let us see what they are - 

#### String operator

**Import statements -**
``` 
import edu.wm.cs.masc.mutation.operators.restrictive.stringoperator.AStringOperator;
import edu.wm.cs.masc.mutation.properties.StringOperatorProperties;
```
**Abstract class to extend -** `AStringOperator`

**Constructor -**
```
public ClassName(StringOperatorProperties properties) {
        super(properties);
}
```



#### Int operator

**Import statements -**
``` 
import edu.wm.cs.masc.mutation.operators.restrictive.intoperator.AIntOperator;
import edu.wm.cs.masc.mutation.properties.IntOperatorProperties;
```
**Abstract class to extend -** `AIntOperator`

**Constructor -**
```
public ClassName(IntOperatorProperties p) {
        super(p);
}
```



#### Byte operator

**Import statements -**
``` 
import edu.wm.cs.masc.mutation.operators.restrictive.byteoperator.AByteOperator;
import edu.wm.cs.masc.mutation.properties.ByteOperatorProperties;
```
**Abstract class to extend -** `AByteOperator`

**Constructor -**
```
public ClassName(ByteOperatorProperties p) {
        super(p);
}
```



#### Interprocedural operator

**Import statements -**
``` 
import edu.wm.cs.masc.mutation.operators.restrictive.interprocoperator.AInterProcOperator;
import edu.wm.cs.masc.mutation.properties.InterprocProperties;
```
**Abstract class to extend -** `AInterProcOperator`

**Constructor -**
```
public ClassName(InterprocProperties p) {
        super(p);
}
```



#### Flexible operator

**Import statements -**
``` 
import edu.wm.cs.masc.mutation.operators.flexible.AFlexibleOperator;
import edu.wm.cs.masc.mutation.properties.FlexibleOperatorProperties;
```
**Abstract class to extend -** ` AFlexibleOperator `

**Constructor -**
```
public ClassName(FlexibleOperatorProperties p) {
        super(p);
}
```



#### Custom operator

**Import statements -**
``` 
import edu.wm.cs.masc.mutation.operators.custom.ACustomGenericOperator;
import edu.wm.cs.masc.mutation.properties.CustomOperatorProperties;
```
**Abstract class to extend -** ` ACustomGenericOperator `

**Constructor -**
```
public ClassName(CustomOperatorProperties p) {
        super(p);
}
```

#### Sample code
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
        return "javax.crypto.Cipher.getInstance(\"aes\");";
    }
}
```
### About the `mutation()` method
Whatever this method returns in the mutation that will be injected in the output mutated apps. In most cases, the return value might not be as simple as the one shown in the example code above. It is very common, recommended even, to be fetching values from properties file. That is the purpose of the properties file, after all. The code of an operator should not be tied to the details of a specific API. Instead, the operator should make use of the values as provided by the properties file so that the operator can be used for virtually any API just by changing the values of the properties file. The structure for properties file for the 5 operator types (except custom type) is fixed, and the values are already available and ready to use. So the `mutation()` function in the above sample should look like this:
```
@Override
    public String mutation() {
        StringBuilder s = new StringBuilder();
        s.append(api_name)
                .append(".")
                .append(invocation)
                .append("(\"")
                .append(insecureParam.toLowerCase())
                .append("\");");
        return s.toString();
    }
```

### About `CustomGenericOperator` 
Custom operator type are the operators that extend `CustomGenericOperator` abstract class. These operators have no fixed structure for the properties file. Hence no values are preloaded and all values can be manually loaded like this - 
`getAttribute("key")`

If a matching key is found in the properties file, then its corresponding value will be retrieved. Else if no matching keys are found, the user will be asked to input its value once only. The input will be saved and the values will be used for every subsequent query until the program ends. 

 

### Step 2. Compiling the code
You code for plugins refer to classes already within MASC's source code. Hence it won't compile without reference to those classes. This can be easily solved by adding the MASC.jar to classpath as such. Open a command prompt in folder where your code is, and run this command: 
``` 
javac -cp directory/MASC.jar *.java   
```

### Step 3. Placing the class file in /plugins/ folder
Place the  `.class` files in /plugins/ folder. Alternatively, you can place your .java files here and compile them here so that the class files are generated here. MASC will ignore all files with extension other than `.class`. 

You can place as many plugins of different types in /plugins/. But only the plugins of the same type as specified in properties file will be run.


### Step 4. Running the jar
To run the jar, execute the command: 
```
java -jar MASC.jar propertiesFileName.properties
```

### Step 5. The output
Generated mutated apps will be produces in `app/output/`
Output from plugins will have `plugins.` prefixed in their names.

### Step 6. Sample operators
Some sample code for plugins are given with the documentation. The code for some of the plugins are simple and some are complicated. Such examples are deliberately chosen to give you an idea of how the plugins can be of varying complexity. But please note that these operators are examples only, so your plugins may be of higher or lower complexity and size. 

## Automated Analysis
You have already seen how to use MASC for mutant generation. In addition to that, you can also have MASC automatically compile and test the generated mutated apps from **main scope** with any static analysis tool of your choice. MASC can then analyze the report and tell you which mutants have been killed and which have not. This feature, automated analysis, is available for the Crypto-detectors that can output in SARIF format. With time, more and more Crypto-detectors are extending their support for SARIF format. But out of those that don't support SARIF format yet, MASC's automated analysis is available for CogniCrypt. 

### How it works
In order to use this feature, you have to - 
1. Write the necessary information in the properties file. 
2. Run MASC normally.

With the necessary information, MASC will - 

1. Generate mutated apps.
2. Compile the mutated apps. 
3. Run your specified static analysis tool for each mutated app and read the output.
4. Show a list showing which mutants have been killed and which have not. 

### The extra info in the properties file
As mentioned earlier, MASC requires a some extra info in the properties file for this feature. The extra fields can be added at the end of the properties file. The extra fields are shown below - 

1. automatedAnalysis = true
1. toolName = Name of the tool
2. toolLocation = Folder directory of the tool's executable
3. toolRunCommand = Command line command that you use to run the tool manually from the command line. 
4. codeCompileCommand = Command to compile the mutated apps. The recommended value for this is `javac *.java`
5. outputReportDirectory = Directory of the folder where the output report will be generated after the tool runs successfully.
6. outputFileName = File name of the output report in outputReportDirectory.
7. stopCondition = When to stop. The possible values are (case insensitive):
    - OnError: Whenever an error such as compilation error, output report not found, wrong command or runtime exception of the tool is encountered. 
    - OnUnkilledMutant - Whenever the tool fails to kill a mutant
    - OnErrorOrUnkilled - Whenever an error is encountered or the tool fails to kill a mutant. 
    - Default (or any other value) - Only stop when the attempt to analyze all mutated apps have completed.

The values will be different for each tool. In addition, it will be different for each operating system since it contains commands that will ultimately be run by MASC in the command line. 

Sample `.properties` file (when run on Windows) -

```
mutantGeneration = true
type = StringOperator
outputDir = app/outputs
apiName = javax.crypto.Cipher
invocation = getInstance
secureParam = AES/GCM/NoPadding
insecureParam = AES
scope = MAIN
noise = ~
variableName = cryptoVariable
className = CryptoTest
propertyName = propertyName

automatedAnalysis = true
toolName = find-sec-bugs
toolLocation = D:/Downloads/Compressed/findsecbugs-cli-1.12.0
toolRunCommand = findsecbugs.bat -high -sarif -output out.json {}
codeCompileCommand = javac *.java
outputReportDirectory = D:/Downloads/Compressed/findsecbugs-cli-1.12.0
outputFileName = out.json
stopCondition = OnError
```
Sample `.properties` file (when run on Ubuntu) -

```
mutantGeneration = true
type = StringOperator
outputDir = app/outputs
apiName = javax.crypto.Cipher
invocation = getInstance
secureParam = AES/GCM/NoPadding
insecureParam = AES
scope = MAIN
noise = ~
variableName = cryptoVariable
className = CryptoTest
propertyName = propertyName

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

**About the `toolRunCommand`** - Running most tools require a command in this format - 
``` 
toolExecutable --parameters folderDirectoryOfTheAppToTest
```
Notice how `folderDirectoryOfTheAppToTest` has been replaced by `{}` in the properties file. This is needed because MASC will have the tool analyze tens of mutated apps one after another. So the value of `folderDirectoryOfTheAppToTest` can't be fixed. Instead, placing `{}` will tell MASC where to insert the folder directory for a particular mutated app. 

**About the keys `automatedAnalysis` and `mutantGeneration`**

More on this [here](#running-only-for-mutant-generation-without-automated-analysis) and [here](#running-only-for-automated-analysis-without-mutant-generation)

**Order of these items in the properties file DO NOT matter**

### Running MASC with automated analysis
This is how you normally run MASC (replacing Cipher.properties with properties file of your choice) - 
```
java -jar MASC.jar Cipher.properties
```
And this is how you run MASC with automated analysis -
```
java -jar MASC.jar Cipher.properties FindSecBugs.properties
```

### Errors and missing values
Don't worry if you forget to include any necessary field in the properties file. MASC will ask for input if you do. 

Apart from that, don't panic if you face errors along the way. If an error is encountered, you will be shown what the error (as would be seen on the command line) was, and what command MASC was trying to execute which resulted in this error. Reading the error message and the attempted command will immensely help you write correct input in the second properties file. 

### Running only for mutant generation without automated analysis
If you want to only run mutant generation, do either of the following - 
 - Set automatedAnalysis = false in the properties file
 - Or, comment out or omit the value of automatedAnalysis in the properties file
 - OR, set the value of automatedAnalysis in the properties file anything other than 'true'. 
 
 If you are not running automated analysis, it does not matter whether you include or omit the other key-value pairs that are exclusively required for automated analysis. These include - toolName, toolLocation, toolRunCommand, mutatedAppsLocation, codeCompileCommand, outputReportDirectory, outputFileName,  stopCondition.

 We recommend that you only set automatedAnalysis = false and leave everything else as is. This way, the properties file will not require much change if you later choose to run automated analysis again. 

### Running only for automated analysis without mutant generation
If you think you have generated the mutants that you needed, and don't need to generate them again, then you can skip mutant generation and go directly to automated analysis. To do that, do either of the following - 
 - Set mutantGeneration = false in the properties file
 - Or, comment out or omit the value of mutantGeneration in the properties file
 - OR, set the value of mutantGeneration in the properties file anything other than 'true'. 
 
 If you are not running mutant generation, it does not matter whether you include or omit the other key-value pairs that are exclusively required for mutant generation. 

 We recommend that you only set mutantGeneration = false and leave everything else as is. This way, the properties file will not require much change if you later choose to run mutant generation again. 

 ### Support for CogniCrypt users
 At this moment, CogniCrypt does not output in the SARIF format. Fortunately, MASC's automated analysis extends its support for CogniCrypt too. Just add an extra field - 
 ```
 wrapper = CogniCrypt
 ```
 And you are done! Here is a sample properties file - 
```
mutantGeneration = true
type = StringOperator
outputDir = app/outputs
apiName = javax.crypto.Cipher
invocation = getInstance
secureParam = AES/GCM/NoPadding
insecureParam = AES
scope = MAIN
noise = ~
variableName = cryptoVariable
className = CryptoTest
propertyName = propertyName

automatedAnalysis = true
toolName = CogniCrypt
toolLocation = F:/IIT/Projects/SPL3/CryptoAnalysis-2.7.2/CryptoAnalysis-2.7.2/CryptoAnalysis/build
toolRunCommand = java -cp CryptoAnalysis-2.7.2-jar-with-dependencies.jar crypto.HeadlessCryptoScanner --rulesDir F:/IIT/Projects/SPL3/CryptoAnalysis-2.7.2/CryptoAnalysis-2.7.2/CryptoAnalysis/src/main/resources/JavaCryptographicArchitecture --applicationCp {} > out.txt
codeCompileCommand = javac *.java
outputReportDirectory = F:/IIT/Projects/SPL3/CryptoAnalysis-2.7.2/CryptoAnalysis-2.7.2/CryptoAnalysis/build
outputFileName = out.txt
stopCondition = OnError
wrapper = CogniCrypt
```

## Running other MASC modules

Apart from the core module discussed above, MASC consists of two more modules.

- MDroidPlus was extended to create the selective scope of MASC. select crypto API specific locations. It then "injects" mutants. These mutants are from masc-core.

- Muse was extended to create the exhaustive scope of MASC, while considering additional considerations in terms of crypto APIs and locations.

Running MASC and its modules is straightforward.

We did our best effort to document the source code and follow proper coding conventions, which should make this easy to navigate through the source code.

We share sample runtime arguments we used for running the two mentioned MASC modules.

### Running MASC for MDroidPlus Extension

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

### Running MASC for mSE Extension

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

<!-- 
# Source code of MASC Prototype

MASC Prototype's source code consists of three modules.

- MASC's core `masc-core` contains builders and generators that work with crypto APIs and mutation operators to generate API use instances (mutants).
It relies on JavaPoet library and Reflection API to extract properties from a given crypto API, uses the values specified by user, and creates crypto API use instances. Depending on the values specified, the instances will be misuse. It can also be used to create barebone mutants.

- MDroidPlus was extended to create the selective scope of MASC. select crypto API specific locations. It then "injects" mutants. These mutants are from masc-core.

- Muse was extended to create the exhaustive scope of MASC, while considering additional considerations in terms of crypto APIs and locations.


## Running MASC modules

Running MASC and its modules is straightforward.

We did our best effort to document the source code and follow proper coding conventions, which should make this easy to navigate through the source code.

## Running MASC modules

We share sample runtime arguments we used for running MASC modules.

### Running MASC Core

```sh
MASCBarebone <propertiesfile.properties path>
## contents of a properties file
type = StringOperator
outputDir = /Users/XXX/workspaces/mutationbackyard/reproduce
apiName = javax.crypto.Cipher
invocation = getInstance
secureParam = AES/GCM/NoPadding
insecureParam = AES
noise = ~
variableName = cryptoVariable
className = CryptoTest
propertyName = propertyName
```

### for MDroidPlus Extension

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

### for mSE Extension

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

-->