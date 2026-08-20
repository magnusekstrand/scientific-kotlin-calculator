package se.oakbeach

/**
 * Recursive-descent parser for calculator input, supporting arbitrary nesting of functions,
 * parentheses, and operators with standard precedence (^ binds tighter than * / %, which bind
 * tighter than + -), e.g. "2 + ln(2)" or "3 * (2 + 1)". All arithmetic is delegated to
 * [ScientificCalculator.calculate] / [ScientificCalculator.applyFunction] so operator/function
 * validation and error messages stay defined in one place.
 */
internal fun evaluateExpression(input: String, calculator: ScientificCalculator): Double {
    val tokens = tokenize(input)
    if (tokens.isEmpty()) throw IllegalArgumentException("Empty input")
    return Parser(tokens, calculator).parse()
}

private sealed interface Token {
    data class Number(val value: Double) : Token
    data class Operator(val symbol: String) : Token
    data class Identifier(val name: String) : Token
    data object LeftParen : Token
    data object RightParen : Token
}

private fun tokenize(input: String): List<Token> {
    val tokens = mutableListOf<Token>()
    var pos = 0
    while (pos < input.length) {
        val c = input[pos]
        when {
            c.isWhitespace() -> pos++
            c.isDigit() || c == '.' -> {
                val start = pos
                var sawDot = false
                while (pos < input.length && (input[pos].isDigit() || (input[pos] == '.' && !sawDot))) {
                    if (input[pos] == '.') sawDot = true
                    pos++
                }
                val text = input.substring(start, pos)
                val value = text.toDoubleOrNull() ?: throw IllegalArgumentException("Invalid number: $text")
                tokens += Token.Number(value)
            }
            c.isLetter() -> {
                val start = pos
                while (pos < input.length && input[pos].isLetter()) pos++
                tokens += Token.Identifier(input.substring(start, pos))
            }
            c == '(' -> { tokens += Token.LeftParen; pos++ }
            c == ')' -> { tokens += Token.RightParen; pos++ }
            else -> { tokens += Token.Operator(c.toString()); pos++ }
        }
    }
    return tokens
}

// Only * / % and ^ need dedicated precedence tiers; + and - (and any symbol the tokenizer
// didn't recognize as digits/letters/parens) fall through to parseExpression, which hands
// them to ScientificCalculator.calculate so operator validation stays in one place.
private val TERM_OPERATORS = setOf("*", "/", "%")
private const val POWER_OPERATOR = "^"

private class Parser(private val tokens: List<Token>, private val calculator: ScientificCalculator) {
    private var pos = 0

    fun parse(): Double {
        val result = parseExpression()
        val trailing = peek()
        if (trailing != null) throw IllegalArgumentException("Unexpected token: $trailing")
        return result
    }

    private fun parseExpression(): Double {
        var value = parseTerm()
        while (true) {
            val op = peekOperator() ?: break
            advance()
            value = calculator.calculate(value, op, parseTerm())
        }
        return value
    }

    private fun parseTerm(): Double {
        var value = parsePower()
        while (true) {
            val op = peekOperator() ?: break
            if (op !in TERM_OPERATORS) break
            advance()
            value = calculator.calculate(value, op, parsePower())
        }
        return value
    }

    private fun parsePower(): Double {
        val base = parseUnary()
        val op = peekOperator()
        return if (op == POWER_OPERATOR) {
            advance()
            calculator.calculate(base, op, parsePower())
        } else {
            base
        }
    }

    private fun parseUnary(): Double {
        return if (peekOperator() == "-") {
            advance()
            -parseUnary()
        } else {
            parsePrimary()
        }
    }

    private fun parsePrimary(): Double = when (val token = peek()) {
        is Token.Number -> {
            advance()
            token.value
        }
        is Token.Identifier -> {
            advance()
            if (peek() == Token.LeftParen) {
                advance()
                val arg = parseExpression()
                expectRightParen()
                calculator.applyFunction(token.name, arg)
            } else {
                throw IllegalArgumentException("Invalid number: ${token.name}")
            }
        }
        Token.LeftParen -> {
            advance()
            val value = parseExpression()
            expectRightParen()
            value
        }
        else -> throw IllegalArgumentException("Unexpected end of input")
    }

    private fun expectRightParen() {
        if (peek() != Token.RightParen) throw IllegalArgumentException("Expected ')'")
        advance()
    }

    private fun peek(): Token? = tokens.getOrNull(pos)

    private fun peekOperator(): String? = (peek() as? Token.Operator)?.symbol

    private fun advance() {
        pos++
    }
}
