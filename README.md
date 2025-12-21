# Happy

Program in Happy: https://happy-lang.live

## Introduction
This programming language supports variable assignments, conditional expressions, loops, function definitions, list operations, and string formatting. It aims to provide a clean and concise syntax for common programming constructs while supporting features such as function calls, list manipulation, and control flow.

## CI/CD Pipeline

The Happy language uses a fully automated CI/CD pipeline to ensure language correctness and continuous deployment.  
All code changes are validated through syntax and REPL tests before being containerized and deployed to production.

<p align="center">
  <img src="diagrams/ci-cd-pipeline.svg" alt="Happy CI/CD Pipeline Diagram">
</p>

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
Variables are assigned using the `var` keyword followed by a name, then the equals sign `=`, and an expression.

~~~
VAR variable_name = expr

ex.
var x = 10
var x = PI
var isEqual = (5 == 5)
var myString = "Hello World"
var test = if 3 >= 1 then "math works" else "math broken"
~~~


### Conditional Expressions
There is no limit on amount of ELIF's

if <condition> then <expression> 
    ELIF <condition> THEN <expression> 
    ELSE <expression>

ex.
~~~
if x == 10 then print("x is 10") elif x > 10 then print("x is greater than 10") else print("x is less than 10")
~~~

### Loops

For Loops

for <var_name> = <start_value> to <end_value> (Optional<step_value>) then <expr>

~~~
for i = 0 to 5 then print(i) -> [0,1,2,3,4]
for i = 0 to 5 step 2 then print(i) - > [0,2,3,4]
~~~

While Loops

while <condition> then <expression>

~~~
while x < 5 then var x = x + 1
~~~

### Functions
Built in Functions
The `print` function takes in any Type in prints that Type as a String.
The `length` function returns the length of a List or a String.
the `fac` function returns the factorial of a number (floored if non-int)

~~~
print("Be happy") -> "Be happy"
length([1,2,3]) -> 3
~~~

Happy comes with some built in functions, but as programmers true happiness comes from creating our own functions.

func <name> (param1, param2, ...) -> <expression>

~~~
func add(x, y) -> x + y
var addTest = add(3, 4)
print(addTest)
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
var foo = 1 :)
~~~
