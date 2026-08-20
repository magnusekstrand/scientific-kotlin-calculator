package se.oakbeach.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import se.oakbeach.ScientificCalculator

/**
 * Drives the calculator UI's button/keyboard state machine. Unlike [ScientificCalculator.handleInput],
 * which parses one complete line of text, this tracks the running left-hand operand and pending
 * operator across successive button presses, delegating the actual arithmetic to
 * [ScientificCalculator.calculate] / [ScientificCalculator.applyFunction].
 */
class CalculatorViewModel(private val calculator: ScientificCalculator = ScientificCalculator()) {

    var display by mutableStateOf("0")
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    /** The number currently being typed/edited (as opposed to [storedValue], the operand left of [pendingOperator]). */
    private var entry: String = "0"
    private var storedValue: Double? = null
    private var pendingOperator: String? = null
    private var startNewEntry: Boolean = false

    /** True right after an operator is pressed, before any digit/function has produced a value for the right-hand operand. */
    private var entrySuppressed: Boolean = false

    fun onDigit(digit: Char) {
        errorMessage = null
        entry = when {
            startNewEntry -> digit.toString()
            entry == "0" -> digit.toString()
            else -> entry + digit
        }
        startNewEntry = false
        entrySuppressed = false
        refreshDisplay()
    }

    fun onDecimalPoint() {
        errorMessage = null
        if (startNewEntry) {
            entry = "0."
            startNewEntry = false
        } else if (!entry.contains('.')) {
            entry += "."
        }
        entrySuppressed = false
        refreshDisplay()
    }

    fun onOperator(operator: String) {
        errorMessage = null
        runCatching {
            if (pendingOperator != null && !startNewEntry) {
                storedValue = calculator.calculate(requireNotNull(storedValue), requireNotNull(pendingOperator), currentValue())
            } else if (pendingOperator == null) {
                storedValue = currentValue()
            }
        }.onFailure {
            handleError(it)
            return
        }
        pendingOperator = operator
        startNewEntry = true
        entrySuppressed = true
        refreshDisplay()
    }

    fun onEquals() {
        errorMessage = null
        val operator = pendingOperator ?: return
        val lhs = storedValue ?: return
        runCatching { calculator.calculate(lhs, operator, currentValue()) }
            .onSuccess { entry = formatResult(it) }
            .onFailure { handleError(it); return }
        storedValue = null
        pendingOperator = null
        startNewEntry = true
        entrySuppressed = false
        refreshDisplay()
    }

    fun onFunction(name: String) {
        errorMessage = null
        runCatching { calculator.applyFunction(name, currentValue()) }
            .onSuccess {
                entry = formatResult(it)
                startNewEntry = true
                entrySuppressed = false
                refreshDisplay()
            }
            .onFailure { handleError(it) }
    }

    /**
     * Converts the current entry to a percentage in place (divides by 100), e.g. "5" -> "0.05",
     * so "5" [onPercent] "×" "40" "=" reads as 5% of 40 = 2 rather than 5 modulo 40.
     */
    fun onPercent() {
        errorMessage = null
        runCatching { currentValue() / 100.0 }
            .onSuccess {
                entry = formatResult(it)
                startNewEntry = true
                entrySuppressed = false
                refreshDisplay()
            }
            .onFailure { handleError(it) }
    }

    fun onToggleSign() {
        errorMessage = null
        entry = when {
            entry == "0" -> entry
            entry.startsWith("-") -> entry.removePrefix("-")
            else -> "-$entry"
        }
        entrySuppressed = false
        refreshDisplay()
    }

    fun onBackspace() {
        errorMessage = null
        if (startNewEntry) return
        entry = if (entry.length > 1) entry.dropLast(1) else "0"
        refreshDisplay()
    }

    fun onClear() {
        errorMessage = null
        entry = "0"
        storedValue = null
        pendingOperator = null
        startNewEntry = false
        entrySuppressed = false
        refreshDisplay()
    }

    private fun currentValue(): Double =
        entry.toDoubleOrNull() ?: throw IllegalArgumentException("Invalid number: $entry")

    private fun handleError(error: Throwable) {
        errorMessage = error.message ?: "Error"
        entry = "0"
        storedValue = null
        pendingOperator = null
        startNewEntry = false
        entrySuppressed = false
        refreshDisplay()
    }

    /** Renders the full running expression, e.g. "9 × 9", not just the operand currently being typed. */
    private fun refreshDisplay() {
        val operator = pendingOperator
        display = if (operator == null) {
            entry
        } else {
            val left = formatResult(requireNotNull(storedValue))
            if (entrySuppressed) "$left ${operatorSymbol(operator)}" else "$left ${operatorSymbol(operator)} $entry"
        }
    }

    companion object {
        fun formatResult(value: Double): String {
            if (value.isNaN() || value.isInfinite()) return value.toString()
            // Rounded to 6 decimal places (not 10+) so results like ln(50) or tan(x) stay short
            // enough to fit the single-line display instead of being clipped off the edge.
            val rounded = kotlin.math.round(value * 1e6) / 1e6
            return if (rounded == kotlin.math.floor(rounded) && kotlin.math.abs(rounded) < 1e15) {
                rounded.toLong().toString()
            } else {
                rounded.toString()
            }
        }

        private fun operatorSymbol(operator: String): String = when (operator) {
            "*" -> "×"
            "/" -> "÷"
            else -> operator
        }
    }
}
