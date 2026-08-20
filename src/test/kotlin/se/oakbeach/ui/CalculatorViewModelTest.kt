package se.oakbeach.ui

import kotlin.test.*
import org.junit.jupiter.api.Test

class CalculatorViewModelTest {

    @Test
    fun `should build up a number from digit presses`() {
        val viewModel = CalculatorViewModel()
        viewModel.onDigit('1')
        viewModel.onDigit('2')
        viewModel.onDigit('3')
        assertEquals("123", viewModel.display)
    }

    @Test
    fun `should replace the leading zero when a digit is pressed`() {
        val viewModel = CalculatorViewModel()
        viewModel.onDigit('5')
        assertEquals("5", viewModel.display)
    }

    @Test
    fun `should only allow a single decimal point`() {
        val viewModel = CalculatorViewModel()
        viewModel.onDigit('1')
        viewModel.onDecimalPoint()
        viewModel.onDecimalPoint()
        viewModel.onDigit('5')
        assertEquals("1.5", viewModel.display)
    }

    @Test
    fun `should evaluate a simple binary expression on equals`() {
        val viewModel = CalculatorViewModel()
        viewModel.onDigit('5')
        viewModel.onOperator("+")
        viewModel.onDigit('3')
        viewModel.onEquals()
        assertEquals("8", viewModel.display)
    }

    @Test
    fun `should chain operators without pressing equals`() {
        val viewModel = CalculatorViewModel()
        viewModel.onDigit('5')
        viewModel.onOperator("+")
        viewModel.onDigit('3')
        viewModel.onOperator("*")
        viewModel.onDigit('2')
        viewModel.onEquals()
        // (5 + 3) * 2, matching a running left-to-right calculator, not operator precedence.
        assertEquals("16", viewModel.display)
    }

    @Test
    fun `should start a fresh entry for the next digit after equals`() {
        val viewModel = CalculatorViewModel()
        viewModel.onDigit('5')
        viewModel.onOperator("+")
        viewModel.onDigit('3')
        viewModel.onEquals()
        viewModel.onDigit('9')
        assertEquals("9", viewModel.display)
    }

    @Test
    fun `should apply a unary function to the current display value`() {
        val viewModel = CalculatorViewModel()
        viewModel.onDigit('1')
        viewModel.onDigit('6')
        viewModel.onFunction("sqrt")
        assertEquals("4", viewModel.display)
    }

    @Test
    fun `should surface an error and reset state on division by zero`() {
        val viewModel = CalculatorViewModel()
        viewModel.onDigit('5')
        viewModel.onOperator("/")
        viewModel.onDigit('0')
        viewModel.onEquals()
        assertEquals("Division by zero", viewModel.errorMessage)
        assertEquals("0", viewModel.display)
    }

    @Test
    fun `should reset to a clean state on clear`() {
        val viewModel = CalculatorViewModel()
        viewModel.onDigit('7')
        viewModel.onOperator("+")
        viewModel.onClear()
        assertEquals("0", viewModel.display)
        viewModel.onDigit('2')
        assertEquals("2", viewModel.display)
    }

    @Test
    fun `should remove the last character on backspace`() {
        val viewModel = CalculatorViewModel()
        viewModel.onDigit('1')
        viewModel.onDigit('2')
        viewModel.onDigit('3')
        viewModel.onBackspace()
        assertEquals("12", viewModel.display)
    }

    @Test
    fun `should toggle the sign of the current entry`() {
        val viewModel = CalculatorViewModel()
        viewModel.onDigit('4')
        viewModel.onToggleSign()
        assertEquals("-4", viewModel.display)
        viewModel.onToggleSign()
        assertEquals("4", viewModel.display)
    }

    @Test
    fun `should show the operand and operator while awaiting the second number`() {
        val viewModel = CalculatorViewModel()
        viewModel.onDigit('9')
        viewModel.onOperator("*")
        assertEquals("9 ×", viewModel.display)
    }

    @Test
    fun `should show the full running expression as the second number is typed`() {
        val viewModel = CalculatorViewModel()
        viewModel.onDigit('9')
        viewModel.onOperator("*")
        viewModel.onDigit('9')
        assertEquals("9 × 9", viewModel.display)
    }

    @Test
    fun `should show the divide operator as its symbol in the running expression`() {
        val viewModel = CalculatorViewModel()
        viewModel.onDigit('8')
        viewModel.onOperator("/")
        viewModel.onDigit('2')
        assertEquals("8 ÷ 2", viewModel.display)
    }

    @Test
    fun `should show a function result applied mid-expression`() {
        val viewModel = CalculatorViewModel()
        viewModel.onDigit('9')
        viewModel.onOperator("+")
        viewModel.onFunction("sqrt")
        assertEquals("9 + 3", viewModel.display)
    }

    @Test
    fun `should collapse back to a bare result after equals`() {
        val viewModel = CalculatorViewModel()
        viewModel.onDigit('9')
        viewModel.onOperator("*")
        viewModel.onDigit('9')
        viewModel.onEquals()
        assertEquals("81", viewModel.display)
    }

    @Test
    fun `should show a short, visible result for an irrational function value like ln`() {
        val viewModel = CalculatorViewModel()
        viewModel.onDigit('3')
        viewModel.onOperator("*")
        viewModel.onDigit('5')
        viewModel.onDigit('0')
        viewModel.onFunction("ln")
        assertEquals("3 × 3.912023", viewModel.display)
    }

    @Test
    fun `should show a short, visible result for an irrational function value like tan`() {
        val viewModel = CalculatorViewModel()
        viewModel.onDigit('5')
        viewModel.onOperator("+")
        viewModel.onDigit('5')
        viewModel.onDigit('0')
        viewModel.onFunction("tan")
        assertEquals("5 + 1.191754", viewModel.display)
    }

    @Test
    fun `should convert the entry to a percentage rather than modulo`() {
        val viewModel = CalculatorViewModel()
        viewModel.onDigit('5')
        viewModel.onPercent()
        assertEquals("0.05", viewModel.display)
    }

    @Test
    fun `should compute 5 percent of 40 as 2, not 5 modulo 40`() {
        val viewModel = CalculatorViewModel()
        viewModel.onDigit('5')
        viewModel.onPercent()
        viewModel.onOperator("*")
        viewModel.onDigit('4')
        viewModel.onDigit('0')
        viewModel.onEquals()
        assertEquals("2", viewModel.display)
    }
}
