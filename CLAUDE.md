# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

Build with the Gradle wrapper (Gradle 9.3.0, Kotlin JVM plugin 2.3.20):

```shell
./gradlew build              # compile + test
./gradlew run                # run the interactive console calculator (stdin-driven)
./gradlew runUi               # run the Compose Desktop calculator UI
./gradlew test                # run all tests
./gradlew test --tests "se.oakbeach.ScientificCalculatorTest.should handle valid addition input"  # run a single test
./gradlew installDist         # build a standalone distribution
./build/install/scientific-kotlin-calculator/bin/scientific-kotlin-calculator  # run the distribution directly
```

`tasks.test` uses JUnit Platform. `tasks.named<JavaExec>("run")` wires `System.in` to the `run` task's stdin so interactive input works when invoked via Gradle (see `8e8d206 Fix run task not reading interactive stdin`) — don't remove that wiring. `runUi` is a separate `JavaExec` task (not the `application` plugin's `run`) so it doesn't disturb that stdin wiring; it launches `se.oakbeach.ui.MainUiKt` instead.

## Architecture

### Console calculator — `src/main/kotlin/se/oakbeach/`

- **`Main.kt`** — entry point; constructs a `ScientificCalculator` and starts the session.
- **`ScientificCalculator.kt`** — the arithmetic core:
  - `handleInput(String): String` trims one line of text and evaluates it via `evaluateExpression` (see below), converting the resulting `Double` to a `String` for display.
  - `calculate(lhs, operator, rhs): Double` and `applyFunction(name, arg): Double` are the pieces to extend when adding a new operator or function — each is a `when` expression that throws `IllegalArgumentException`/`ArithmeticException` on invalid input rather than returning error values. They are the *sole* place operator/function validity is decided — the parser and the Compose UI's `CalculatorViewModel` both call into them rather than duplicating that logic.
  - `getInput()` is the only I/O-facing method, delegating the interactive loop to `CalculatorInputHandler`.
- **`ExpressionParser.kt`** — a recursive-descent tokenizer/parser (`evaluateExpression(input, calculator)`) supporting full expressions, not just a single flat operator: nested function calls (`2 + ln(2)`), parentheses (`3 * (2 + 1)`), standard precedence (`^` tighter than `* / %` tighter than `+ -`, `^` right-associative), and leading unary minus. It tokenizes into numbers/identifiers/operators/parens and calls `calculate`/`applyFunction` for every operation, so error messages (e.g. `Invalid operator: ?`, `Unknown function: foo`) come from `ScientificCalculator`, not the parser itself.
- **`CalculatorInputHandler.kt`** — an `object` that owns the interactive REPL: prints usage banner, reads lines via `generateSequence(::readlnOrNull).takeWhile { it.isNotBlank() }` (blank line quits), and catches `RuntimeException` from the handler to print `Error: <message>` instead of crashing.

The split exists so the parsing/math logic stays testable without stdin, while the REPL loop (`CalculatorInputHandler`) stays free of calculator-specific logic — `handleInput` is passed in as a plain `(String) -> String` function reference.

Errors are communicated via exceptions with human-readable `message`s, not sentinel return values; `CalculatorInputHandler` is the single place that converts them to output text for the console app.

Supported binary operators: `+ - * / % ^`. Supported unary functions: `sqrt sin cos tan log ln exp abs fact` (trig functions take degrees, not radians).

### Compose Desktop UI — `src/main/kotlin/se/oakbeach/ui/`

A button/keyboard-driven calculator UI, separate from the text-based console app above:

- **`MainUi.kt`** — `application { Window(...) }` entry point; wires `onPreviewKeyEvent` to `resolveKeyAction`.
- **`CalculatorViewModel.kt`** — the button-press state machine (running left-hand operand, pending operator, digit entry). Unlike `ExpressionParser`, it doesn't parse text — button clicks/key presses drive it directly — but it delegates all arithmetic to `ScientificCalculator.calculate`/`applyFunction`, so operator/function behavior stays defined once. `display` is the full rendered expression (e.g. `"9 × 9"`), not just the current operand.
- **`KeyboardMapping.kt`** — pure `resolveKeyAction(key, isShiftPressed): CalculatorAction?` mapping keyboard `Key`s to calculator actions; kept separate from `CalculatorViewModel` and free of Compose `KeyEvent` internals so it's unit-testable with plain `Key` constants.
- **`CalculatorApp.kt`** — the Composable layout: display panel, function grid, keypad.

Test the UI by actually running it (`./gradlew runUi`) — `CalculatorViewModelTest` and `KeyboardMappingTest` cover the state machine and key mapping, but nothing in the test suite renders the Composables.

## Add Unit tests

- Whenever you add any changes, add unit tests and run and make sure the tests passes.
