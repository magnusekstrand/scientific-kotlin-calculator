# scientific-kotlin-calculator

A small scientific calculator written in Kotlin, with two front ends sharing
the same arithmetic core: an interactive console REPL and a Compose Desktop
button/keyboard UI.

## Running

**Console** — reads one expression per line from standard input and prints
the result until you press Enter on an empty line to quit:

```shell
./gradlew run
```

**Desktop UI** — a windowed calculator with clickable buttons and keyboard
support:

```shell
./gradlew runUi
```

Or build a standalone distribution of the console app and run it directly:

```shell
./gradlew installDist
./build/install/scientific-kotlin-calculator/bin/scientific-kotlin-calculator
```

## Console usage

Input is a full expression, not just a single operator or function call —
nested function calls, parentheses, and standard operator precedence are all
supported (`^` binds tighter than `* / %`, which bind tighter than `+ -`; `^`
is right-associative; a leading `-` is a unary minus).

| Operator | Meaning        |
|----------|----------------|
| `+`      | Addition       |
| `-`      | Subtraction    |
| `*`      | Multiplication |
| `/`      | Division       |
| `%`      | Modulo         |
| `^`      | Power          |

| Function | Meaning                            |
|----------|-------------------------------------|
| `sqrt`   | Square root                        |
| `sin`    | Sine (argument in degrees)         |
| `cos`    | Cosine (argument in degrees)       |
| `tan`    | Tangent (argument in degrees)      |
| `log`    | Base-10 logarithm                  |
| `ln`     | Natural logarithm                  |
| `exp`    | e raised to the given power        |
| `abs`    | Absolute value                     |
| `fact`   | Factorial (non-negative integers)  |

### Example session

```
$ ./gradlew run
Scientific calculator
Enter an expression, e.g. 5 + 10, sqrt(16), or 2 + ln(2) * (3 - 1)
Operators: + - * / % ^   Functions: sqrt, sin, cos, tan, log, ln, exp, abs, fact
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
2 + ln(2)
2.6931471805599454
3 * (2 + 1)
9.0
10 / 0
Error: Division by zero

Good bye! 👋🏼
```

## Desktop UI usage

The UI shows the running expression as you build it (e.g. `9 × 9`, not just
the last number typed) and collapses to the bare result once you press `=`.
Every button has a keyboard equivalent:

| Buttons                              | Keys                                             |
|---------------------------------------|---------------------------------------------------|
| Digits `0`–`9`, `.`                   | Number row or numpad, `.`                        |
| `+` `-` `×` `÷` `^`                   | `+` `-` `*` `/` `^` (number row or numpad)        |
| `=`                                   | `Enter`                                          |
| `C` (clear)                           | `Esc` or `Delete`                                |
| `⌫` (backspace)                       | `Backspace`                                      |
| `±` (toggle sign)                     | button only                                      |
| `%`                                   | `Shift`+`5` — converts the entry to a percentage, e.g. `5` `%` `×` `40` `=` gives `2` (5% of 40), not `5 mod 40` |
| `sqrt` `sin` `cos` `tan` `log` `ln` `exp` `abs` `fact` | button only, applied to whatever is currently entered |

## Testing

```shell
./gradlew test
```
