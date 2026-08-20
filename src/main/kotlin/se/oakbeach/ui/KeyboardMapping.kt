package se.oakbeach.ui

import androidx.compose.ui.input.key.Key

/** A calculator action triggered by either a button click or a keyboard press. */
sealed interface CalculatorAction {
    data class Digit(val value: Char) : CalculatorAction
    data object DecimalPoint : CalculatorAction
    data class Operator(val symbol: String) : CalculatorAction
    data object Percent : CalculatorAction
    data object Equals : CalculatorAction
    data object Backspace : CalculatorAction
    data object Clear : CalculatorAction
}

private val DIGIT_KEYS: Map<Key, Char> = mapOf(
    Key.Zero to '0', Key.One to '1', Key.Two to '2', Key.Three to '3', Key.Four to '4',
    Key.Five to '5', Key.Six to '6', Key.Seven to '7', Key.Eight to '8', Key.Nine to '9',
    Key.NumPad0 to '0', Key.NumPad1 to '1', Key.NumPad2 to '2', Key.NumPad3 to '3', Key.NumPad4 to '4',
    Key.NumPad5 to '5', Key.NumPad6 to '6', Key.NumPad7 to '7', Key.NumPad8 to '8', Key.NumPad9 to '9',
)

// On a US-QWERTY layout these operator symbols share a physical key with a digit/equals key,
// typed by holding Shift; the raw key event reports the unshifted key regardless of the symbol.
private val SHIFTED_OPERATOR_KEYS: Map<Key, CalculatorAction> = mapOf(
    Key.Five to CalculatorAction.Percent,
    Key.Six to CalculatorAction.Operator("^"),
    Key.Eight to CalculatorAction.Operator("*"),
    Key.Equals to CalculatorAction.Operator("+"),
)

/** Maps a raw key press to the [CalculatorAction] it should trigger, or `null` if unmapped. */
fun resolveKeyAction(key: Key, isShiftPressed: Boolean): CalculatorAction? {
    if (isShiftPressed) {
        SHIFTED_OPERATOR_KEYS[key]?.let { return it }
    }

    DIGIT_KEYS[key]?.let { return CalculatorAction.Digit(it) }

    return when (key) {
        Key.Period, Key.NumPadDot -> CalculatorAction.DecimalPoint
        Key.Plus, Key.NumPadAdd -> CalculatorAction.Operator("+")
        Key.Minus, Key.NumPadSubtract -> CalculatorAction.Operator("-")
        Key.Slash, Key.NumPadDivide -> CalculatorAction.Operator("/")
        Key.Multiply, Key.NumPadMultiply -> CalculatorAction.Operator("*")
        Key.Equals, Key.NumPadEquals, Key.Enter, Key.NumPadEnter -> CalculatorAction.Equals
        Key.Backspace -> CalculatorAction.Backspace
        Key.Delete, Key.Escape -> CalculatorAction.Clear
        else -> null
    }
}

fun CalculatorAction.applyTo(viewModel: CalculatorViewModel) {
    when (this) {
        is CalculatorAction.Digit -> viewModel.onDigit(value)
        is CalculatorAction.DecimalPoint -> viewModel.onDecimalPoint()
        is CalculatorAction.Operator -> viewModel.onOperator(symbol)
        is CalculatorAction.Percent -> viewModel.onPercent()
        is CalculatorAction.Equals -> viewModel.onEquals()
        is CalculatorAction.Backspace -> viewModel.onBackspace()
        is CalculatorAction.Clear -> viewModel.onClear()
    }
}
