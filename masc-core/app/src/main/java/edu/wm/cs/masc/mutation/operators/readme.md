# Operators

Contains flexible and restrictive operators.

Operators define the body / structure of the misuse based on mutation rules applied on how the API is intended to be used. 


## How Operators work

InterProcAddition - Takes the specified misuse and seperates each letter into its own method. These methods are then called to create a string of the misuse that is then passed into the unsafe method call.

InterprocNestedConditional - Takes user input in the iterations field and creates a number of nested conditionals based on that value. The unsafe value is placed in each if and the safe value in else. The else statements are not designed to evaluate. The variable value is then called and the unsafe value is passed.

InterprocOperator - Takes in a user input for the iteration field. Will then create the number of methods specified containing the safe value. The last method will contain an unsafe value. These are then called in order and the final call sets the variable to an unsafe value.

InterprocObjectSensitive - Creates two version of the base interproc object. It then creates two versions of the object where one is set to the unsafe value and the other to the safe value. The one that was initially set to the safe value is set equal to the unsafe before being passed into the vulnerable method.

InterprocConditional - Creates a base version of the conditionals where the value of the conditional is changed to access the unsafe value after a safe call

StaticKeystore - creates a static set of bytes that is then passed into a vulnerable API

StringSubstring - Extracts the misuse from the end of a string and passed it into a vulnerable method

StringCharArray - Creates a char array of the vulnerable string that is then converted back into a string and passed into the vulnerable API