package se.oakbeach

import kotlin.test.*
import org.junit.jupiter.api.Test

class ScientificCalculatorTest {

    @Test
    fun `should handle valid addition input`() {
        val calculator = ScientificCalculator()
        assertEquals("8.0", calculator.handleInput("5 + 3"))
    }

    @Test
    fun `should handle valid subtraction input`() {
        val calculator = ScientificCalculator()
        assertEquals("4.0", calculator.handleInput("10 - 6"))
    }

    @Test
    fun `should handle valid multiplication input`() {
        val calculator = ScientificCalculator()
        assertEquals("20.0", calculator.handleInput("4 * 5"))
    }

    @Test
    fun `should handle valid division input`() {
        val calculator = ScientificCalculator()
        assertEquals("5.0", calculator.handleInput("20 / 4"))
    }

    @Test
    fun `should handle valid modulo input`() {
        val calculator = ScientificCalculator()
        assertEquals("1.0", calculator.handleInput("10 % 3"))
    }

    @Test
    fun `should handle valid power input`() {
        val calculator = ScientificCalculator()
        assertEquals("8.0", calculator.handleInput("2 ^ 3"))
    }

    @Test
    fun `should throw exception for unsupported operator`() {
        val calculator = ScientificCalculator()
        val exception = assertFailsWith<IllegalArgumentException> {
            calculator.handleInput("8 ? 2")
        }
        assertEquals("Invalid operator: ?", exception.message)
    }

    @Test
    fun `should throw exception when dividing by zero`() {
        val calculator = ScientificCalculator()
        val exception = assertFailsWith<ArithmeticException> {
            calculator.handleInput("10 / 0")
        }
        assertEquals("Division by zero", exception.message)
    }

    @Test
    fun `should evaluate a bare number as itself`() {
        val calculator = ScientificCalculator()
        assertEquals("5.0", calculator.handleInput("5"))
    }

    @Test
    fun `should throw exception for a trailing operator with no right-hand operand`() {
        val calculator = ScientificCalculator()
        val exception = assertFailsWith<IllegalArgumentException> {
            calculator.handleInput("5 +")
        }
        assertEquals("Unexpected end of input", exception.message)
    }

    @Test
    fun `should throw exception for invalid operand`() {
        val calculator = ScientificCalculator()
        val exception = assertFailsWith<IllegalArgumentException> {
            calculator.handleInput("abc + 5")
        }
        assertEquals("Invalid number: abc", exception.message)
    }

    @Test
    fun `should compute square root`() {
        val calculator = ScientificCalculator()
        assertEquals("4.0", calculator.handleInput("sqrt(16)"))
    }

    @Test
    fun `should throw exception for square root of negative number`() {
        val calculator = ScientificCalculator()
        val exception = assertFailsWith<ArithmeticException> {
            calculator.handleInput("sqrt(-4)")
        }
        assertEquals("Cannot take square root of a negative number", exception.message)
    }

    @Test
    fun `should compute sine of ninety degrees`() {
        val calculator = ScientificCalculator()
        assertEquals(1.0, calculator.applyFunction("sin", 90.0), 1e-9)
    }

    @Test
    fun `should compute cosine of zero degrees`() {
        val calculator = ScientificCalculator()
        assertEquals(1.0, calculator.applyFunction("cos", 0.0), 1e-9)
    }

    @Test
    fun `should compute base ten logarithm`() {
        val calculator = ScientificCalculator()
        assertEquals("2.0", calculator.handleInput("log(100)"))
    }

    @Test
    fun `should throw exception for logarithm of non-positive number`() {
        val calculator = ScientificCalculator()
        val exception = assertFailsWith<ArithmeticException> {
            calculator.handleInput("log(0)")
        }
        assertEquals("Logarithm undefined for non-positive numbers", exception.message)
    }

    @Test
    fun `should compute natural logarithm`() {
        val calculator = ScientificCalculator()
        assertEquals(1.0, calculator.applyFunction("ln", Math.E), 1e-9)
    }

    @Test
    fun `should compute absolute value`() {
        val calculator = ScientificCalculator()
        assertEquals("5.0", calculator.handleInput("abs(-5)"))
    }

    @Test
    fun `should compute factorial`() {
        val calculator = ScientificCalculator()
        assertEquals("120.0", calculator.handleInput("fact(5)"))
    }

    @Test
    fun `should throw exception for factorial of negative number`() {
        val calculator = ScientificCalculator()
        val exception = assertFailsWith<IllegalArgumentException> {
            calculator.handleInput("fact(-1)")
        }
        assertEquals("Factorial is only defined for non-negative integers", exception.message)
    }

    @Test
    fun `should throw exception for unknown function`() {
        val calculator = ScientificCalculator()
        val exception = assertFailsWith<IllegalArgumentException> {
            calculator.handleInput("foo(1)")
        }
        assertEquals("Unknown function: foo", exception.message)
    }

    @Test
    fun `should evaluate a function call nested inside a binary expression`() {
        val calculator = ScientificCalculator()
        assertEquals((2.0 + kotlin.math.ln(2.0)).toString(), calculator.handleInput("2 + ln(2)"))
    }

    @Test
    fun `should evaluate a function call on the left-hand side of an operator`() {
        val calculator = ScientificCalculator()
        assertEquals("6.0", calculator.handleInput("sqrt(16) + 2"))
    }

    @Test
    fun `should respect standard operator precedence`() {
        val calculator = ScientificCalculator()
        assertEquals("14.0", calculator.handleInput("2 + 3 * 4"))
    }

    @Test
    fun `should support parentheses to override precedence`() {
        val calculator = ScientificCalculator()
        assertEquals("20.0", calculator.handleInput("4 * (2 + 3)"))
    }

    @Test
    fun `should evaluate power as right-associative`() {
        val calculator = ScientificCalculator()
        // 2 ^ (3 ^ 2) = 2 ^ 9 = 512, not (2 ^ 3) ^ 2 = 64
        assertEquals("512.0", calculator.handleInput("2 ^ 3 ^ 2"))
    }

    @Test
    fun `should apply a leading unary minus`() {
        val calculator = ScientificCalculator()
        assertEquals("-2.0", calculator.handleInput("-5 + 3"))
    }

    @Test
    fun `should propagate division by zero from a nested sub-expression`() {
        val calculator = ScientificCalculator()
        val exception = assertFailsWith<ArithmeticException> {
            calculator.handleInput("1 + 10 / 0")
        }
        assertEquals("Division by zero", exception.message)
    }
}
