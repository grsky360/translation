package ilio.translation.component

import androidx.compose.desktop.AppWindow
import androidx.compose.desktop.ComposeWindow
import androidx.compose.desktop.WindowEvents
import androidx.compose.foundation.border
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import ilio.translation.support.component.Component
import ilio.translation.support.component.ComponentWindow
import ilio.translation.support.component.ComponentWindowConfiguration
import ilio.translation.support.extention.hideMe
import ilio.translation.support.extention.on
import ilio.translation.support.ui.Block
import ilio.translation.support.ui.Row
import ilio.translation.support.ui.Spacer8dp
import ilio.translation.utils.async

class TranslationComponent : Component {

    private var state by mutableStateOf(Model())

    @Composable
    override fun render() {
        Block {
            Row.verticalCenter {
                TextField(value = state.input, onValueChange = {
                    state = state.copy(input = it)
                    println(it)
                })
            }
            Spacer8dp()

            Row.verticalCenter {
                Button({
                    async {
//                            val response = translate(TranslateRequest(input.value))
                        state = state.copy(output = state.input)
                    }
                }) {
                    Text("Translate it")
                }
            }
            Spacer8dp()

            Row.verticalCenter {
                Text(state.output, modifier = Modifier.border(2.dp, Color.Gray))
            }
        }
    }

    private data class Model(val input: String = "", val output: String = "")
}

object TranslationWindow : ComponentWindow<TranslationComponent>(::TranslationComponent) {

    override fun afterInitialize(context: AppWindow, window: ComposeWindow) {
        context.window.isAlwaysOnTop = true
        context.on(Key.Escape) { context.hideMe() }
    }

    override fun configuration(): ComponentWindowConfiguration = ComponentWindowConfiguration(
        title = "Translation Popup",
        size = IntSize(300, 450),
        undecorated = true,
        resizable = false,
        events = WindowEvents(
            onFocusLost = {
                this.hideMe()
            }
        )
    )
}
