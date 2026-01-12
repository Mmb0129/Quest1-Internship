# Lisp Interpreter in Java

## By Micheal Berdinanth M

A simple Lisp interpreter built in Java that can parse and evaluate Lisp-style expressions.

## What is this?

This is a basic Lisp interpreter that lets you write and run simple Lisp code. You can do math, define variables, and use conditional logic. 

## Features

- **Arithmetic operations**: Add, subtract, multiply, divide, and modulo
- **Variable definitions**: Store values in variables
- **Conditional logic**: Use if statements to make decisions
- **Comparisons**: Compare numbers with greater than, less than, and equals
- **Nested expressions**: Build complex expressions like `(+ 1 (* 2 3))`

## Getting Started

### Prerequisites

- Java 17 or higher
- Gradle (9.2.1)

### Running the Interpreter

```bash
gradlew.bat run
```

### Using the Interpreter

Once running, you'll see a prompt like this:

```
Lisp Interpreter
Type 'exit' to quit
lisp> 
```

You can now type Lisp expressions:

```lisp
lisp> (+ 1 2)
3

lisp> (define x 10)
10

lisp> (* x 2)
20

lisp> (if (> x 5) 100 200)
100

lisp> (+ 1 (* 2 3))
7

lisp> exit
Goodbye!
```

## How It Works

The interpreter works in three main steps:

1. **Parsing**: Takes ymy text input and turns it into a tree structure (called an Abstract Syntax Tree or AST)
2. **Evaluation**: Walks through the tree and figures out what the expression means
3. **Output**: Shows you the result

Think of it like this: when you write `(+ 1 2)`, the parser sees it as a list with three parts: the `+` operator, the number `1`, and the number `2`. Then the evaluator says "okay, I need to add 1 and 2" and gives you `3`.

## Project Structure

```
app/src/main/java/com/micheal/lisp/
├── ast/              # The tree nodes (NumberNode, SymbolNode, ListNode)
├── parser/            # Converts text into a tree
├── visitor/           # Walks the tree and evaluates expressions
├── environment/       # Stores variables (like x = 10)
├── factory/           # Creates tree nodes
├── exception/         # Custom error types
└── repl/              # The interactive command line interface
```

## Design Patterns Used

This project uses three important design patterns. Here's why each one matters:

### 1. Visitor Pattern

**What it does**: Separates the tree structure from the operations we perform on it.

**Why we use it**: 

Imagine if every tree node knew how to evaluate itself. That would mean if you wanted to add a new operation (like printing the tree, or type checking), you'd have to modify every single node class. That's messy and breaks the "open for extension, closed for modification" principle.

With the Visitor pattern, the nodes just say "here I am, do what you want with me" and the visitor does the actual work. This means:

- **We can add new operations easily**: Want to add a pretty printer? Just create a `PrettyPrintVisitor`. No need to touch the node classes.
- **The code is cleaner**: Each node class is simple and focused on just being a node.
- **It's easier to test**: You can test evaluation logic separately from the tree structure.

**In my code**: The `EvaluationVisitor` visits each node and decides what to do. A `NumberNode` just returns its value, a `SymbolNode` looks up a variable, and a `ListNode` evaluates an expression.

### 2. Factory Pattern

**What it does**: Provides a single place to create tree nodes instead of using `new` everywhere.

**Why we use it**:

Why not just write `new NumberNode(5)` directly in the parser? Here's why the factory is better:

- **Flexibility**: What if we want to change how numbers are stored? Maybe we switch from `int` to `BigDecimal` for better precision. With a factory, we change one place. Without it, we'd have to find and change every `new NumberNode()` call.
- **Validation**: We can add checks when creating nodes. For example, we could validate that symbol names aren't empty.

**In my code**: The `LispParser` never calls `new NumberNode()` directly. Instead, it calls `nodeFactory.createNumber(value)`. This keeps the parser simple and gives us room to grow.

### 3. Singleton Pattern

**What it does**: Ensures there's only one global environment (variable storage) in the entire program.

**Why we use it**:

Variables need to be stored somewhere. We could create a new environment every time, but then variables defined in one place wouldn't be available elsewhere. We need one shared storage space.

- **Consistency**: No matter where in the code we need to look up a variable, we're always looking at the same place.
- **Simplicity**: We don't have to pass the environment around everywhere. We just call `GlobalEnvironment.getInstance()` and we're done.
- **It matches reality**: In a real interpreter, there really is just one global scope, that's what I learnt in my Compiler Design Course, not fully sure.

**In my code**: When you write `(define x 10)`, it goes into the singleton `GlobalEnvironment`. Later, when you use `x` in an expression, the evaluator looks it up in that same environment.


## Error Handling

The interpreter has custom exceptions for different types of errors:

- `UndefinedSymbolException`: When you use a variable that doesn't exist
- `InvalidOperatorException`: When you use an operator that doesn't exist
- `InvalidArgumentCountException`: When you give an operator the wrong number of arguments
- `LispArithmeticException`: When you do something like divide by zero

All errors are caught and displayed in a user-friendly way.

## Example Expressions

Here are some more examples of what you can do:

```lisp
; Basic arithmetic
(+ 1 2 3)                    ; => 6
(- 10 3)                     ; => 7
(* 2 3 4)                    ; => 24
(/ 20 4)                     ; => 5
(% 10 3)                     ; => 1

; Variables
(define x 10)                ; => 10
(define y 20)                ; => 20
(+ x y)                      ; => 30

; Comparisons
(> 10 5)                     ; => true
(< 3 7)                      ; => true
(= 5 5)                      ; => true

; Conditionals
(if (> x 5) 100 200)          ; => 100 (if x > 5, else 200)
(if (= x 10) (* x 2) 0)      ; => 20

; Nested expressions
(+ 1 (* 2 3))                ; => 7
(if (> (* 2 3) 5) 10 20)     ; => 10
```

## Building 

To build the project:

```bash
./gradlew.bat build
```

## Limitations

It has some limitations:

- Only supports integers (no decimals or strings)
- No functions or lambdas
- No loops
- Simple error messages
- No file loading

---
