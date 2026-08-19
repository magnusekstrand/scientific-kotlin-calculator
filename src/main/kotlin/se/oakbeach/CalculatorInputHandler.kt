package se.oakbeach

object CalculatorInputHandler {

    fun runInteractiveSession(inputHandler: (String) -> String) {
        println("Scientific calculator")
        println("Binary:   <value> <+|-|*|/|%|^> <value>   e.g. 5 + 10")
        println("Unary:    <function>(<value>)             e.g. sqrt(16)")
        println("Functions: sqrt, sin, cos, tan, log, ln, exp, abs, fact")
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
