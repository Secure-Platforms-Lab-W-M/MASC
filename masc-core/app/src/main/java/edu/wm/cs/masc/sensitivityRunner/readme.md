# Sensitivity Evaluator

## Sensitivity Types and Definitions

This tool is designed to allow a user to run MASC and generate mutants
based solely on a sensitivity type. This was done to help lower the barrier of 
entry to MASC and allow users to produce mutants without having to understand each specific operator.
Research was conducted to find the most commonly agreed upon types of sensitivities. Below are the definitions of the sensitivities 
within the scope of MASC:

Flow Sensitivity - Flow sensitivity is an incredibly precise form of sensitivity. It recognizes the order that statements are performed and can keep track of the state of the program at that point in time. A flow sensitive analysis performs its analysis based on the sequence of statements. It can tell if two variables are assigned after line 23 while a flow insensitive analysis will only know that the two variables were assigned at some point within the scope of their analysis. A flow analysis will only take into consideration portions of the program that would be run based on the previous lines

Context Sensitivity - Context sensitivity takes into account the information throughout the program when method calls are made to determine if there is a vulnerability. It can differentiate between two different function calls to the same method with different variables. A context insensitive approach would flag both function calls if one of them was considered vulnerable while a context sensitive approach can differentiate between the two calls.

Path Sensitivity - Path sensitivity only takes into consideration paths through the program that are feasible. It has a heavy focus on things such as conditionals. Within programs some paths or statements can not be reached by the code, a path sensitive analysis would not flag a vulnerability that is unreachable. Path sensitive analysis is only concerned with the path of the program that is possible to be executed.

Field/Index Sensitivity - Field sensitivity is the ability to differentiate different fields that are a part of the same object. If an object contains two variables one tainted and one that is not tainted and the non tainted one is called a field sensitive analysis would not flag the object as a vulnerability. This requires keeping track of all the contents of objects separately and understanding when certain aspects of an object are called by the program.

Object Sensitivty - Object sensitivity takes into account different versions of the same object. It has the ability to understand the difference between a version of an object that contains a vulnerability and one that does not contain a vulnerability. If we create two versions of object FOO called f1 and f2 and place a vulnerability in f2 but only interact with f1, an object sensitive tool would be able to recognize that there is not vulnerability taking place.

Alias Sensitivity - Alias sensitivity is typically a version of context or flow sensitivity. In Java alias sensitivity is typically type based meaning since Java is a type safe language. Alias sensitivity is the ability to keep track of a variable that has been aliased to another variable and still keeping track of the value. If there is a variable named x that equals 1 and we pass this into a method this passed value would be an alias of x.


## How to run

To run the sensitivity evaluator please provide a configuration file containing all the values that would normally be
contained in a properties file. Below an example of the contents necessary within the file:

````
sensitivity=alias
outputDir=app/outputs
scope=MAIN
mutantGeneration = true

StringApiName = javax.crypto.Cipher
StringInvocation = getInstance
StringSecureParam = AES/GCM/NoPadding
StringInsecureParam = AES
StringNoise = ~
StringVariableName = cryptoVariable
StringClassName = CryptoTest
StringPropertyName = propertyName

IntApiName = javax.crypto.spec
IntInvocation = PBEKeySpec
IntPassword = very_secure
IntSalt = {80\\,45\\,56}
IntAlgorithm = RSA
IntKeyGenVarName = keyGen
IntVariableName = iterCount
IntIterationCount = 50
IntClassName = CryptoTest
IntMisuse = PBE

InterprocApiName = javax.crypto.Cipher
InterprocInvocation = getInstance
InterprocSecureParam = AES/GCM/NoPadding
InterprocInsecureParam = AES
InterprocNoise = ~
InterprocIterations = 20
InterprocVariableName = cryptoVariable
InterprocClassName = CryptoTest
InterprocPropertyName = propertyName
InterprocOtherClassName = CipherPack
InterprocTry_catch = False

ByteApiName = javax.crypto.spec.IvParameterSpec
ByteClassName = CryptoTest
ByteTempVariableName = cryptoTemp
ByteApiVariable = ivSpec
ByteInvocation = getInstance
ByteSecureParam = AES/GCM/NoPadding
ByteInsecureParam = AES
ByteNoise = ~
ByteVariableName = cryptoVariable
BytePropertyName = propertyName
````

You can specify each of the fields below and the outputs with be produced with MASC as you specify. The main differences
with the fields in this file and the properties files are that the operator type is specified before the field. In addition,
there are also a few options at the top. The sensitivity field is where you can specify which types of sensitivities you want
to generate. The options are: alias, flow, object, context, path, and field. As of right now this tool only works with the MAIN 
scope of MASC but should be expanded in the future to include additional functionality. This tool is also only designed to
currently work with the restrictive operators but should also be expanded in the future to include
the flexible operators.

## Operator Categories

Below is a list of which operators are included within each sensitivity field:
Flow - CurrentTime, AbsoluteValue, Arithmitic, FromString, Overflow, RoundValue, IntValueInVariable, ValueInVariableArithmetic, DifferenceCase, NoiseReplace, SafeReplaceWithUnsafe, StringCaseTransform, UnsafeReplaceWithUnsafe, StringValueInVariable, ByteCurrentTime, ByteStatic, StringValueOfChar, StringSubstring

Path - InterprocConditional, InterprocNestedConditional, InterprocObjectSensitive

Context - InterprocOperator, InterprocAddition, InterprocConditional, InterprocNestedConditional, ByteReuse, IterationMultipleCall

Field - Interproc, InterprocAddition, InterprocConditional, InterprocNestedConditional, InterprocObjectSensitive, StringSubstring, StringValueOfChar

Object - ByteReuse, InterprocObjectSensitive

Alias - InterprocObjectSensitive, IntValueInVariable, IntValueInVariableArithmetic, IntWhileLoopAccumulation, StringSubstring, StringValueInVariable, StringValueOfChar