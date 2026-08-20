package se.oakbeach

object CalculatorInputHandler {

    fun runInteractiveSession(inputHandler: (String) -> String) {
        println("Scientific calculator")
        println("Enter an expression, e.g. 5 + 10, sqrt(16), or 2 + ln(2) * (3 - 1)")
        println("Operators: + - * / % ^   Functions: sqrt, sin, cos, tan, log, ln, exp, abs, fact")
        println("Press Enter on an empty line to quit.")

        generateSequence(::readlnOrNull)
            .takeWhile { it.isNotBlank() }
            .forEach { input ->
                try {
                    println(inputHandler(input))
                } catch (e: RuntimeException) {
                    println("Error: ${e.message}")
                }
            }

        println("Good bye! 👋🏼")
    }
}
