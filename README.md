# scientific-kotlin-calculator

A small interactive scientific calculator written in Kotlin. It reads one
expression per line from a standard input and prints the result until you
press Enter on an empty line to quit.

## Running

Using the Gradle wrapper:

```shell
./gradlew run
```

Or build a standalone distribution and run it directly:

```shell
./gradlew installDist
./build/install/scientific-kotlin-calculator/bin/scientific-kotlin-calculator
```

## Usage

Two input forms are supported:

**Binary operations** — `<value> <operator> <value>`

| Operator | Meaning        |
|----------|----------------|
| `+`      | Addition       |
| `-`      | Subtraction    |
| `*`      | Multiplication |
| `/`      | Division       |
| `%`      | Modulo         |
| `^`      | Power          |

**Unary functions** — `<function>(<value>)`

| Function | Meaning                          |
|----------|-----------------------------------|
| `sqrt`   | Square root                       |
| `sin`    | Sine (argument in degrees)        |
| `cos`    | Cosine (argument in degrees)      |
| `tan`    | Tangent (argument in degrees)     |
| `log`    | Base-10 logarithm                 |
| `ln`     | Natural logarithm                 |
| `exp`    | e raised to the given power       |
| `abs`    | Absolute value                    |
| `fact`   | Factorial (non-negative integers) |

### Example session

```
$ ./gradlew run
Scientific calculator
Binary:   <value> <+|-|*|/|%|^> <value>   e.g. 5 + 10
Unary:    <function>(<value>)             e.g. sqrt(16)
Functions: sqrt, sin, cos, tan, log, ln, exp, abs, fact
Press Enter on an empty line to quit.
5 + 10
15.0
2 ^ 10
1024.0
sqrt(16)
4.0
sin(90)
1.0
log(100)
2.0
fact(5)
120.0
10 / 0
Error: Division by zero

Good bye! 👋🏼
```

## Testing

```shell
./gradlew test
```
