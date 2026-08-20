package se.oakbeach.ui

import androidx.compose.runtime.remember
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main(): Unit = application {
    val viewModel = remember { CalculatorViewModel() }
    val windowState = rememberWindowState(size = DpSize(380.dp, 760.dp))

    Window(
        onCloseRequest = ::exitApplication,
        title = "Scientific Calculator",
        state = windowState,
        onPreviewKeyEvent = { event ->
            if (event.type == KeyEventType.KeyDown) {
                resolveKeyAction(event.key, event.isShiftPressed)?.let {
                    it.applyTo(viewModel)
                    true
                } ?: false
            } else {
                false
            }
        },
    ) {
        CalculatorApp(viewModel)
    }
}
