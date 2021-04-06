package ilio.translation.component

import androidx.compose.desktop.AppWindow
import androidx.compose.desktop.WindowEvents
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.unit.IntSize
import com.arkivanov.decompose.ComponentContext
import ilio.translation.support.component.Component
import ilio.translation.support.component.ComponentWindow
import ilio.translation.support.component.ComponentWindowConfiguration
import ilio.translation.support.extention.hideMe
import ilio.translation.support.extention.on
import ilio.translation.utils.async

class TranslationComponent(componentContext: ComponentContext) : Component, ComponentContext by componentContext {

    private var state by mutableStateOf(Model())

    @Composable
    override fun render() {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TextField(value = state.input, onValueChange = {
                state = state.copy(input = it)
                println(it)
            })

            Button({
                async {
//                    val response = translate(TranslateRequest(input.value))
                    state = state.copy(output = state.input)
                }
            }) {
                Text("Translate it")
            }

            Text(state.output)
        }
    }

    private data class Model(val input: String = "", val output: String = "")
}

object TranslationW : ComponentWindow() {

    override fun afterInitialize(context: AppWindow) {
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

    override fun factory(): (ComponentContext) -> Component = ::TranslationComponent
}

fun main() = TranslationW.showMeStandalone()
