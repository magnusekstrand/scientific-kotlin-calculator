package se.oakbeach.ui

import androidx.compose.ui.input.key.Key
import kotlin.test.*
import org.junit.jupiter.api.Test

class KeyboardMappingTest {

    @Test
    fun `should map digit keys to digit actions`() {
        assertEquals(CalculatorAction.Digit('7'), resolveKeyAction(Key.Seven, isShiftPressed = false))
        assertEquals(CalculatorAction.Digit('7'), resolveKeyAction(Key.NumPad7, isShiftPressed = false))
    }

    @Test
    fun `should map the multiplication sign to the multiply operator`() {
        assertEquals(CalculatorAction.Operator("*"), resolveKeyAction(Key.Eight, isShiftPressed = true))
        assertEquals(CalculatorAction.Operator("*"), resolveKeyAction(Key.NumPadMultiply, isShiftPressed = false))
    }

    @Test
    fun `should map shift-five to percent and shift-six to the caret operator`() {
        assertEquals(CalculatorAction.Percent, resolveKeyAction(Key.Five, isShiftPressed = true))
        assertEquals(CalculatorAction.Operator("^"), resolveKeyAction(Key.Six, isShiftPressed = true))
    }

    @Test
    fun `should map plus, minus, divide keys to their operators`() {
        assertEquals(CalculatorAction.Operator("+"), resolveKeyAction(Key.Equals, isShiftPressed = true))
        assertEquals(CalculatorAction.Operator("+"), resolveKeyAction(Key.NumPadAdd, isShiftPressed = false))
        assertEquals(CalculatorAction.Operator("-"), resolveKeyAction(Key.Minus, isShiftPressed = false))
        assertEquals(CalculatorAction.Operator("/"), resolveKeyAction(Key.Slash, isShiftPressed = false))
    }

    @Test
    fun `should map enter and equals keys to the equals action`() {
        assertEquals(CalculatorAction.Equals, resolveKeyAction(Key.Enter, isShiftPressed = false))
        assertEquals(CalculatorAction.Equals, resolveKeyAction(Key.NumPadEnter, isShiftPressed = false))
        assertEquals(CalculatorAction.Equals, resolveKeyAction(Key.Equals, isShiftPressed = false))
    }

    @Test
    fun `should map escape and delete to clear`() {
        assertEquals(CalculatorAction.Clear, resolveKeyAction(Key.Escape, isShiftPressed = false))
        assertEquals(CalculatorAction.Clear, resolveKeyAction(Key.Delete, isShiftPressed = false))
    }

    @Test
    fun `should map backspace to backspace`() {
        assertEquals(CalculatorAction.Backspace, resolveKeyAction(Key.Backspace, isShiftPressed = false))
    }

    @Test
    fun `should map period keys to decimal point`() {
        assertEquals(CalculatorAction.DecimalPoint, resolveKeyAction(Key.Period, isShiftPressed = false))
        assertEquals(CalculatorAction.DecimalPoint, resolveKeyAction(Key.NumPadDot, isShiftPressed = false))
    }

    @Test
    fun `should return null for unmapped keys`() {
        assertNull(resolveKeyAction(Key.A, isShiftPressed = false))
    }

    @Test
    fun `should apply a resolved action to the view model`() {
        val viewModel = CalculatorViewModel()
        CalculatorAction.Digit('9').applyTo(viewModel)
        assertEquals("9", viewModel.display)
    }

    @Test
    fun `should apply a resolved percent action to the view model`() {
        val viewModel = CalculatorViewModel()
        CalculatorAction.Digit('5').applyTo(viewModel)
        CalculatorAction.Percent.applyTo(viewModel)
        assertEquals("0.05", viewModel.display)
    }
}
