# MASC

MASC (Mutation Analysis for Evaluating Static Crypto-API Misuse Detectors) is a framework for systematic and data-driven evaluation of static analysis based crypto-API misuse detectors (*Crypto-detectors*).
MASC does this by (1) using realistic, expressive crypto-API specific mutation-operators to create expressions of crypto API misuse (misuse instances), (2) creating mutant apps by injecting misuse instances in open source Java or Android applications, and (3) evaluating crypto-detectors by analyzing the reports produced from scanning the mutated apps.

![MASC Process Diagram. It describes the following, 3-step process: (1) using realistic, expressive crypto-API specific mutation-operators to create expressions of crypto API misuse (misuse instances), (2) creating mutant apps by injecting misuse instances in open source Java or Android applications, and (3) analyzing crypto-detectors by running those on mutated apps](assets/masc-process.png)

## Demonstration Video
For a more detailed explanation of the concepts behind MASC and a step-by-step guide to its features, check out this video demonstration.

[![MASC Demonstration](http://img.youtube.com/vi/ZKzUnBXGla0/hqdefault.jpg)](https://www.youtube.com/watch?v=ZKzUnBXGla0)

## Dependencies/Environment Setup

MASC is built to run using Java LTS  version 11. Please refer to your operating system specific instructions (e.g., [Oracle](https://www.java.com/en/download/help/download_options.html)) to install Java. Please make sure that it is available in the system `PATH`, and can be accessed from terminal. For example, running `java -version` in terminal/cmd should show a message similar to the following:

```sh
java -version
openjdk version "11.0.19" 2023-04-18 LTS
OpenJDK Runtime Environment Corretto-11.0.19.7.1 (build 11.0.19+7-LTS)
OpenJDK 64-Bit Server VM Corretto-11.0.19.7.1 (build 11.0.19+7-LTS, mixed mode)
```

Moreover, MASC needs [gradle](https://gradle.org/) to be built. Please refer to your OS specific instructions to install gradle.

## Configuring MASC

To get started, we need to do the following:

1. Start by cloning the [MASC Repository](https://github.com/Secure-Platforms-Lab-W-M/MASC) from GitHub.
2. Open the cloned repository, go to `masc-core` directory, and run  `gradlew shadowJar` to create a JAR file for MASC. The output JAR can be found at `masc-core > app > build > libs > app-all.jar`.
3. Test run with `java -jar masc.jar`. If you see the message “No properties file supplied”, it means MASC has been successfully built.

MASC is run by specifying the parameters in a text-based configuration (`.properties`) file, consisting of multiple `key = value` pairs. Some keys are required, whereas the others are optional. Since there can be several combinations of keys, to simplify the familiarization, we will use the following sample configuration file to run MASC.

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
Here, the first six keys are *required*. Note that the order of the keys in the configuration file does not matter.

For more examples of configuration files, go [here](https://github.com/Secure-Platforms-Lab-W-M/MASC/tree/main/masc-core/app/src/main/resources).

We also have a detailed guide on writing configuration files and with explanation of each `key = value` pairs in the [user manual](https://github.com/Secure-Platforms-Lab-W-M/MASC/blob/main/Documentation/user_manual.md#creating-a-properties-file).

MASC can be run using both Command Line Interface and Browser-based User Interface, which we discuss next.

## Running MASC with the web interface
MASC web interface provides an user the opportunity to explore MASC along with its various scopes and operators.The web interface shall give the user the privildge to run Masc lab and Masc engine.Using MASC Lab an user can inspect the functionality of different operators for different configuration and through MASC Engine they can can mutate their source code using both Exhaustive or Selective Scope.
### Environments
The project is based on django frame work.
```sh
python => 3.10.4
Django => (4, 0, 5, 'final', 0)
```

### Run MASC web interface
- Create virtual environment
```sh
python3 -m venv venv
source venv/bin/activate
```
for windows 
```sh
python3 -m venv venv
.\venv\Scripts\activate
```

- Install dependecies
```sh
pip install -r requirements.txt
```

- Make Migration
```sh
python manage.py makemigrations
python manage.py migrate
```
- Run server
```sh
python manage.py runserver
```


## Running MASC with the CLI

### Running MASC with the Main Scope

In the example configuration file, we have specified that we want to create crypto-API misuse instances in a bare-bone Java source file that contains a `main` method. Due to the bare-bone nature, MASC does not need an input app to mutate, rather it creates a single source code file and then introduces the misuse instance inside it for convenience.

Assuming that the above sample configuration file is saved in a file called `Cipher.properties`, please run the following:

```sh
java -jar masc.jar Cipher.properties
```

### Output

Check the output folder (as specified in the configuration file). You will find `n` folders, each containing the output of an operator, which is a mutated application. Now, you can analyze these mutants by static analyzers of your choice manually or by using the automated analysis module of MASC as described [here](#automated-analysis).

### Running MASC with Scopes

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

## Related work

A. S. Ami, N. Cooper, K. Kafle, K. Moran, D. Poshyvanyk and A. Nadkarni, "Why Crypto-detectors Fail: A Systematic Evaluation of Cryptographic Misuse Detection Techniques," 2022 IEEE Symposium on Security and Privacy (SP), San Francisco, CA, USA, 2022, pp. 614-631, doi: [10.1109/SP46214.2022.9833582](
https://doi.org/10.1109/SP46214.2022.9833582).

```bibtex
@inproceedings{ami-masc-oakland22,
  author = {Ami, {Amit Seal} and Cooper, Nathan and Kafle, Kaushal and Moran, Kevin and Poshyvanyk, Denys and Nadkarni, Adwait},
  booktitle = {2022 IEEE Symposium on Security and Privacy (S\&P)},
  title = {{Why Crypto-detectors Fail: A Systematic Evaluation of Cryptographic Misuse Detection Techniques}},
  year = {2022},
  address = {San Francisco, CA, USA},
  month = may,
  pages = {397--414},
  publisher = {IEEE Computer Society},
  issn = {2375-1207},
  pdf = {https://arxiv.org/pdf/2107.07065.pdf},
  sourcecode = {https://github.com/Secure-Platforms-Lab-W-M/masc-artifact},
  url = {https://ieeexplore.ieee.org/document/9833582},
  doi = {10.1109/SP46214.2022.9833582}
}
```
## Developer documentation
To access the user manuals and High Level Architectural diagrams, please go [here](https://github.com/Secure-Platforms-Lab-W-M/MASC/tree/main/Documentation).
