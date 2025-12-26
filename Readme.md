## 1. The Kyr compiler project

This repository contains a jumpstart code for my students, it won't evolve to a compiler.
The compiler must be developed in **Java**, using the parser generators **JFlex** and **JavaCup**.
It generates **MIPS assembly code**.

To allow easy automation of tests, the following constraints must be respected:

* The compiler is delivered as the archive `kyrN.jar`, where `N` is the kernel version.
  Execution requires exactly one argument: the name of the file containing the program to compile, with the `.kyr` suffix.
  If compilation succeeds, a file with the same prefix and the `.mips` suffix is created, containing the generated target code.
* Execution is non-interactive; aside from the generated `.mips` file, it must leave the caller’s environment unchanged.
* Depending on the case, execution produces **exactly one** of the following outputs on standard output (and nothing else):

```
ERREUR LEXICALE :  no ligne d'erreur suivie d'un message d'erreur explicite
ERREUR SYNTAXIQUE :  no ligne d'erreur suivie d'un message d'erreur explicite
ERREUR SEMANTIQUE : no ligne d'erreur suivie d'un message d'erreur explicite
COMPILATION OK
```

* A lexical or syntactic error stops compilation immediately. All detected semantic errors must be reported.

---

## 2. Project Organization

The compiler must be developed through successive **language kernels**.
Once a kernel compiler is finished and fully tested, you must immediately start developing the next kernel.
Conversely, it is useless to start a new kernel before the previous one is fully completed and tested.

---

## 2.1 Kyr0 Grammar – printing only

The compiler only processes programs with printing instructions; only the grammar excerpt below is required.

```ebnf
<programme> ::= 'debut' { <instruction> } 'fin'

<instruction> ::= 'ecrire' <expression> ';'
                | 'ecrire' string ';'

<expression> ::= integer | 'vrai' | 'faux'
```

---

## 2.2 Language Kernels and Submission Deadlines

For the following kernels, it is your responsibility to extract the appropriate rules from the full grammar.

| **Kernel** | **Language Constructs Supported**                                                                      | **Submission Deadline (kyrN.jar)**       |
| ---------- | ------------------------------------------------------------------------------------------------------ | ---------------------------------------- |
| Kyr0       | Output instruction<br>Comments                                                                         | Week 3 – Friday, January 16 – 8:00 PM    |
| Kyr1       | Variable declarations (integer/boolean)<br>Assignment<br>Expressions reduced to constants or variables | Week 5 – Thursday, January 29 – 8:00 PM  |
| Kyr2       | Arbitrary expressions without function calls<br>Conditional instruction<br>Loop instruction            | Week 8 – Thursday, February 19 – 8:00 PM |
| Kyr3       | Function without parameters or local variables<br>Expressions with function calls                      | Week 10 – Thursday, March 5 – 8:00 PM    |
| Kyr4       | Functions with parameters and local integer and boolean variables                                      | Week 13 – Thursday, March 26 – 8:00 PM   |

---


## 3. Language Definition

The **Kyr** (short for kyrielle) language is a rudimentary statically typed programming language that includes integer and boolean variables, basic control structures, and functions.

Here is a short example program written in Kyr:

```c++
variables
    // constantes
    entier l
    entier w
    entier h
    entier s

    // variables
    entier x
    entier y
    entier r
    entier v
    entier d
    entier e
    entier a
    entier z

debut
    l = 19;
    w = 80;
    h = 25;
    s = 8192;

    y = 0;
    repeter
        r = -(125*s)/100 + ((25*s/10)*y)/h;
        x = 0;
        repeter
            v = -2*s + ((25*s/10)*x)/w;
            d = 0;
            e = 0;
            a = -1;
            repeter
                z = (d*d-e*e)/s+v;
                e = (d+d)*e/s + r;
                d = z;
                a = a + 1;
            jusqua a>=l ou d*d+e*e>=4*s*s ;

            si a==0  alors ecrire "."; finsi
            si a==1  alors ecrire ","; finsi
            si a==2  alors ecrire "'"; finsi
            si a==3  alors ecrire "~"; finsi
            si a==4  alors ecrire "="; finsi
            si a==5  alors ecrire "+"; finsi
            si a==6  alors ecrire ":"; finsi
            si a==7  alors ecrire ";"; finsi
            si a==8  alors ecrire "["; finsi
            si a==9  alors ecrire "/"; finsi
            si a==10 alors ecrire "<"; finsi
            si a==11 alors ecrire "&"; finsi
            si a==12 alors ecrire "?"; finsi
            si a==13 alors ecrire "o"; finsi
            si a==14 alors ecrire "x"; finsi
            si a==15 alors ecrire "O"; finsi
            si a==16 alors ecrire "X"; finsi
            si a==17 alors ecrire "#"; finsi
            si a>=18 alors ecrire " "; finsi

            x = x + 1;
        jusqua x>=w ;
        ecrire "\n";
        y = y + 1;
    jusqua y>=h ;
fin
```
And the corresponding output:

![](https://haqr.eu/tinycompiler/home/mandelbrot.png)

---

## 3.1 Grammar

The grammar of the language is written in **EBNF (Extended Backus–Naur Form)** and uses the following conventions:

* Non-terminals are enclosed in angle brackets `<…>` (for example `<expression>`, `<instruction>`).
* Curly braces `{…}` indicate zero or more occurrences of an element.
* Square brackets `[…]` indicate that an element is optional (it may appear or not).
* Terminal symbols are either fixed text written in single quotes (for example `'entier'`, `'si'`, `'+'`), or lexical classes recognized by the lexer:

  * **`identifier`** denotes a valid variable or function name (it starts with a letter or an underscore `_`, and may contain letters, digits, or underscores after the first character).
  * A **`string`** is text enclosed in double quotes `"…"`, and may contain any character except an unescaped double quote.
  * **`integer`** denotes a sequence of digits (not to be confused with the reserved keyword `'entier'`).

The axiom of the grammar is `<programme>`.

Comments start with the character sequence `//` and end at the end of the line.

Spaces, tabs, newlines, and comments are ignored by the parser and are not part of the grammar; they are handled by the lexer.

Keywords are written in lowercase (exactly as in the grammar) and are reserved.
For example, a Kyr program may contain an identifier `SI`, which will not be confused with the reserved keyword `si`.

---

### Grammar (EBNF)

```ebnf
<programme> ::= [ <variables> ] [ <fonctions> ] 'debut' { <instruction> } 'fin'

<variables> ::= 'variables' { <variable> }

<variable> ::= <type> identifier

<type> ::= 'entier' | 'booleen'

<fonctions> ::= 'fonctions' { <fonction> }

<fonction> ::= <type> identifier '(' { <variable> } ')'
               [ <variables> ]
               'debut' { <instruction> } 'fin'

<instruction> ::= identifier '=' <expression> ';'
                | 'repeter' { <instruction> } 'jusqua' <expression> ';'
                | 'si' <expression> 'alors' { <instruction> }
                  [ 'sinon' { <instruction> } ] 'finsi'
                | 'retourne' <expression> ';'
                | 'ecrire' <expression> ';'
                | 'ecrire' string ';'

<expression> ::= identifier '(' [ <arguments> ] ')'
               | identifier
               | integer
               | 'vrai'
               | 'faux'
               | '(' <expression> ')'
               | 'non' <expression>
               | '-' <expression>
               | <expression> <operation> <expression>

<operation> ::= '+'
              | '*'
              | '-'
              | '/'
              | '%'
              | '<'
              | '<='
              | '>'
              | '>='
              | '=='
              | '!='
              | 'et'
              | 'ou'

<arguments> ::= <expression> { ',' <expression> }
```

---

## 3.2 Semantics

* A program consists of variable declarations, function declarations, and instructions.
  When the program is executed, instructions are executed in the order in which they are written.
* The scope of a variable or function declaration is the entire region that contains it, excluding nested regions where the same identifier is redeclared in the same namespace.
  A region is delimited by the keywords `debut` and `fin`.
* Variables are not initialized by default.
* Multiple declarations of the same variable are forbidden.
  Function overloading is allowed, provided that different signatures are defined (i.e., different numbers of parameters).
  A variable and a function cannot share the same name.
* In an assignment, the left-hand side and the right-hand side must have the same type.
* In the body of a function, the `retourne` instruction is mandatory.
  The type of its expression must match the return type of the function.
  Executing this instruction terminates the function and returns the value of the expression.
  The `retourne` instruction cannot appear outside a function body.
* In a conditional iteration, the expression must be of boolean type and is evaluated **after** execution of the repeated instructions.
* In a conditional instruction, the expression must be of boolean type.
* The predefined function `ecrire` outputs either a string, an integer, or a boolean.
  Booleans are written as `vrai` or `faux`.
  This function does not append a newline.
* The operands of operators `+`, `-`, `*`, `/`, `%`, and unary `-` are integers, and the result is an integer.
  The operands of `et`, `ou`, and `non` are booleans.
  The relational operator `<` takes integer operands and returns a boolean.
  Operators `==` and `!=` take operands of the same type and return a boolean.
* All binary operators are left-associative.
  Operator precedence is defined by the table below.
  Operators in the same cell have equal precedence; each cell has higher precedence than the one to its right.

| () | `non`, unary `-` | `*` `/` `%` | `-` `+` | `<` `<=` `>` `>=` | `==` `!=` | `et` `ou` |
| -- | ---------------- | ----------- | ------- | ----------------- | --------- | --------- |

* In a function call, the compiler selects the function signature based on the number of actual parameters.
  Parameters are passed by value.

