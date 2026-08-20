package se.oakbeach.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val BackgroundColor = Color(0xFF1E1E1E)
private val DisplayColor = Color(0xFF2B2B2B)
private val DigitColor = Color(0xFF3A3A3A)
private val FunctionColor = Color(0xFF33475B)
private val NeutralColor = Color(0xFF505050)
private val AccentColor = Color(0xFFFF9F0A)
private val ErrorColor = Color(0xFFFF6B6B)

@Composable
fun CalculatorApp(viewModel: CalculatorViewModel = remember { CalculatorViewModel() }) {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = BackgroundColor) {
            Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
                DisplayPanel(viewModel)
                Spacer(Modifier.height(12.dp))
                FunctionPad(viewModel)
                Spacer(Modifier.height(8.dp))
                KeyPad(viewModel)
            }
        }
    }
}

// Shrinks the display font as the running expression grows, so a long result (e.g. from
// ln/tan) stays fully visible instead of being clipped off the edge of the single-line display.
private fun displayFontSize(text: String): androidx.compose.ui.unit.TextUnit = when {
    text.length <= 10 -> 40.sp
    text.length <= 14 -> 32.sp
    text.length <= 18 -> 26.sp
    text.length <= 24 -> 20.sp
    else -> 16.sp
}

@Composable
private fun DisplayPanel(viewModel: CalculatorViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(DisplayColor, RoundedCornerShape(8.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.End,
    ) {
        Text(
            text = viewModel.display,
            color = Color.White,
            fontSize = displayFontSize(viewModel.display),
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.End,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis,
        )
        viewModel.errorMessage?.let { message ->
            Text(text = message, color = ErrorColor, fontSize = 14.sp)
        }
    }
}

@Composable
private fun FunctionPad(viewModel: CalculatorViewModel) {
    val functionRows = listOf(
        listOf("sqrt", "sin", "cos"),
        listOf("tan", "log", "ln"),
        listOf("exp", "abs", "fact"),
    )
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        functionRows.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                row.forEach { name ->
                    CalcButton(
                        label = name,
                        modifier = Modifier.weight(1f),
                        backgroundColor = FunctionColor,
                        onClick = { viewModel.onFunction(name) },
                    )
                }
            }
        }
    }
}

@Composable
private fun KeyPad(viewModel: CalculatorViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            CalcButton("C", Modifier.weight(1f), NeutralColor) { viewModel.onClear() }
            CalcButton("⌫", Modifier.weight(1f), NeutralColor) { viewModel.onBackspace() }
            CalcButton("%", Modifier.weight(1f), NeutralColor) { viewModel.onPercent() }
            CalcButton("^", Modifier.weight(1f), NeutralColor) { viewModel.onOperator("^") }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            CalcButton("7", Modifier.weight(1f), DigitColor) { viewModel.onDigit('7') }
            CalcButton("8", Modifier.weight(1f), DigitColor) { viewModel.onDigit('8') }
            CalcButton("9", Modifier.weight(1f), DigitColor) { viewModel.onDigit('9') }
            CalcButton("÷", Modifier.weight(1f), AccentColor) { viewModel.onOperator("/") }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            CalcButton("4", Modifier.weight(1f), DigitColor) { viewModel.onDigit('4') }
            CalcButton("5", Modifier.weight(1f), DigitColor) { viewModel.onDigit('5') }
            CalcButton("6", Modifier.weight(1f), DigitColor) { viewModel.onDigit('6') }
            CalcButton("×", Modifier.weight(1f), AccentColor) { viewModel.onOperator("*") }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            CalcButton("1", Modifier.weight(1f), DigitColor) { viewModel.onDigit('1') }
            CalcButton("2", Modifier.weight(1f), DigitColor) { viewModel.onDigit('2') }
            CalcButton("3", Modifier.weight(1f), DigitColor) { viewModel.onDigit('3') }
            CalcButton("-", Modifier.weight(1f), AccentColor) { viewModel.onOperator("-") }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            CalcButton("±", Modifier.weight(1f), DigitColor) { viewModel.onToggleSign() }
            CalcButton("0", Modifier.weight(1f), DigitColor) { viewModel.onDigit('0') }
            CalcButton(".", Modifier.weight(1f), DigitColor) { viewModel.onDecimalPoint() }
            CalcButton("+", Modifier.weight(1f), AccentColor) { viewModel.onOperator("+") }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            CalcButton("=", Modifier.fillMaxWidth(), AccentColor) { viewModel.onEquals() }
        }
    }
}

@Composable
private fun CalcButton(
    label: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = DigitColor,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor),
    ) {
        Text(text = label, color = Color.White, fontSize = 18.sp)
    }
}
