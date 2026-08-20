package se.oakbeach

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

class ScientificCalculator {

    fun getInput() {
        CalculatorInputHandler.runInteractiveSession(::handleInput)
    }

    fun handleInput(input: String): String =
        evaluateExpression(input.trim(), this).toString()

    fun calculate(lhs: Double, operator: String, rhs: Double): Double = when (operator) {
        "+" -> lhs + rhs
        "-" -> lhs - rhs
        "*" -> lhs * rhs
        "/" -> {
            if (rhs == 0.0) throw ArithmeticException("Division by zero")
            lhs / rhs
        }
        "%" -> lhs % rhs
        "^" -> lhs.pow(rhs)
        else -> throw IllegalArgumentException("Invalid operator: $operator")
    }

    fun applyFunction(name: String, arg: Double): Double = when (name.lowercase()) {
        "sqrt" -> {
            if (arg < 0.0) throw ArithmeticException("Cannot take square root of a negative number")
            sqrt(arg)
        }
        "sin" -> sin(arg * DEGREES_TO_RADIANS)
        "cos" -> cos(arg * DEGREES_TO_RADIANS)
        "tan" -> tan(arg * DEGREES_TO_RADIANS)
        "log" -> {
            if (arg <= 0.0) throw ArithmeticException("Logarithm undefined for non-positive numbers")
            log10(arg)
        }
        "ln" -> {
            if (arg <= 0.0) throw ArithmeticException("Logarithm undefined for non-positive numbers")
            ln(arg)
        }
        "exp" -> exp(arg)
        "abs" -> abs(arg)
        "fact" -> factorial(arg)
        else -> throw IllegalArgumentException("Unknown function: $name")
    }

    private fun factorial(value: Double): Double {
        if (value < 0.0 || value != floor(value)) {
            throw IllegalArgumentException("Factorial is only defined for non-negative integers")
        }
        var result = 1.0
        var n = value
        while (n > 1.0) {
            result *= n
            n -= 1.0
        }
        return result
    }

    companion object {
        private val DEGREES_TO_RADIANS = PI / 180.0
    }
}
