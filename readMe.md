# Custom Programming Language

## Introduction
This programming language supports variable assignments, conditional expressions, loops, function definitions, list operations, and string formatting. It aims to provide a clean and concise syntax for common programming constructs while supporting features such as function calls, list manipulation, and control flow.

## Syntax Overview

### Variable Assignment
Variables are assigned using the `VAR` keyword followed by an identifier, the equals sign `=`, and an expression.

~~~plaintext
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
IF x == 10 THEN PRINT("x is 10") 
    ELIF x > 10 THEN PRINT("x is greater than 10")
    ELSE PRINT("x is less than 10")
~~~

### Loops

For Loops

FOR <var_name> = <start_value> TO <end_value> (OPTIONAL STEP <step_value>) THEN <expr>

~~~
FOR i = 0 TO 10 THEN PRINT(i)
FOR i = 0 TO 10 STEP 2 THEN PRINT(i)
~~~

While Loops

WHILE <condition> THEN <expression>

~~~
WHILE x < 10 THEN VAR x = x + 1
~~~

### Functions
Functions are defined using the FUNC keyword followed by an optional identifier, a parameter list, and an arrow -> leading to the expression.

FUNC <identifier>? (param1, param2, ...) -> <expression>

~~~
FUNC add(x, y) -> x + y
~~~

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

### Built in Functions
The PRINT function outputs anything to the console
The LENGTH function returns the length of a list or string

## Future Features
Use of :) and :( in syntax
~~~
VAR foo = 1 :)
~~~
