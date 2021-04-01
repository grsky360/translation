package ilio.translation.ui

import androidx.compose.desktop.AppWindow
import androidx.compose.desktop.WindowEvents
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.unit.IntSize
import ilio.translation.ui.support.injectContext
import ilio.translation.ui.support.shortcut.hideMe
import ilio.translation.ui.support.shortcut.on
import ilio.translation.utils.async

fun translationContext(): AppWindow = injectContext("popup",
    title = "Translation Popup",
    size = IntSize(300, 450),
    undecorated = true,
    resizable = false,
    events = WindowEvents(
        onFocusLost = {
            translationContext().hideMe()
        }
    ),
    init = {
        it.window.isAlwaysOnTop = true
        it.on(Key.Escape) { it.hideMe() }
    }
) {
    val input = remember { mutableStateOf("") }
    val output = remember { mutableStateOf("asdf") }

    MaterialTheme {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TextField(value = input.value, onValueChange = { input.value = it })

            Button({
                async {
//                    val response = translate(TranslateRequest(input.value))
                    output.value = input.value
                }
            }) {
                Text("Translate it")
            }

            Text(output.value)
        }
    }
}