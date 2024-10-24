# Custom Programming Language

## Introduction
This programming language supports variable assignments, conditional expressions, loops, function definitions, list operations, and string formatting. It aims to provide a clean and concise syntax for common programming constructs while supporting features such as function calls, list manipulation, and control flow.

## Syntax Overview

### Types
Happy consists of the following types
~~~
Number
String
List
~~~
Booleans are Numbers, `1` for True and `0` for False

### Variable Assignment
Variables are assigned using the `VAR` keyword followed by a name, then the equals sign `=`, and an expression.

~~~
VAR variable_name = expr

ex.
VAR x = 10
VAR x = PI
VAR isEqual = (5 == 5)
VAR myString = "Hello World"
~~~


### Conditional Expressions
There is no limit on amount of ELIF's

IF <condition> THEN <expression> 
    ELIF <condition> THEN <expression> 
    ELSE <expression>

ex.
~~~
IF x == 10 THEN PRINT("x is 10") ELIF x > 10 THEN PRINT("x is greater than 10") ELSE PRINT("x is less than 10")
~~~

### Loops

For Loops

FOR <var_name> = <start_value> TO <end_value> (OPTIONAL STEP <step_value>) THEN <expr>

~~~
FOR i = 0 TO 5 THEN PRINT(i) -> [0,1,2,3,4]
FOR i = 0 TO 5 STEP 2 THEN PRINT(i) - > [0,2,3,4]
~~~

While Loops

WHILE <condition> THEN <expression>

~~~
WHILE x < 5 THEN VAR x = x + 1
~~~

### Functions
Built in Functions
The `PRINT` function takes in any Type in prints that Type as a String.
The `LENGTH` function returns the length of a List or a String.

~~~
PRINT("Be happy") -> "Be happy"
LENGTH([1,2,3]) -> 3
~~~

Happy comes with some built in functions, but as programmers true happiness comes from creating our own functions.

FUNC <name> (param1, param2, ...) -> <expression>

~~~
FUNC add(x, y) -> x + y
VAR addTest = add(3, 4)
PRINT(addTest)
~~~
output: 7

### List Operations
Lists are enclosed in square brackets and support operations such as addition and accessing elements by index.
~~~
[1,2,3] + 4         => [1,2,3,4]
[1,2] + [3,4]       => [1,2,3,4]
[1,2,3] ! 0         => 1
~~~

### String Formatting
Strings are enclosed in double quotes and support escape characters such as \" for quotes, \\ for backslashes, and \n for new lines.
~~~
"Text"
"Text with \"quotes\""
"Text with \\ backslashes \\"
"Text \n with \n new lines"
~~~

## String Operations
"Sup" * 5 => "SupSupSupSupSup"


## Future Features
Use of :) and :( in syntax
~~~
VAR foo = 1 :)
~~~
