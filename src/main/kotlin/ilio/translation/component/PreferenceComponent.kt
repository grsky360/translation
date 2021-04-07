package ilio.translation.component

import androidx.compose.desktop.AppWindow
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import ilio.translation.support.component.Component
import ilio.translation.support.component.ComponentWindow
import ilio.translation.support.component.ComponentWindowConfiguration
import ilio.translation.support.component.container

class PreferenceComponent : Component {

    private val dialog: PreferenceComponentCloseDialog by container

    @Composable
    override fun render() {
        MaterialTheme {
            val selectedIndex = remember { mutableStateOf(0) }
            TabRow(selectedTabIndex = selectedIndex.value) {
                Tab(selected = true, onClick = {
                }, text = {
                    Text("abc")
                })
                Tab(selected = false, onClick = {
                }, text = {
                    Text("abc")
                })
            }
            dialog.render()
        }
    }

    object instance : ComponentWindow<PreferenceComponent>(::PreferenceComponent) {

        override fun configuration(): ComponentWindowConfiguration = ComponentWindowConfiguration(
            title = "Preferences",
            size = IntSize(700, 450),
            resizable = false,
            undecorated = false
        )

        override fun afterInitialize(context: AppWindow) {
            context.window.defaultCloseOperation = 0
//            context.on(Key.Escape) {
//                closeOnSave.showMe()
//                context.window.isEnabled = false
//                context.window.isAlwaysOnTop = false
//                closeOnSave.awaitEvent<Boolean>("closeWithSave") {
//                    this.hideMe()
//
//                    context.window.isEnabled = true
//                }
//            }
        }
    }
}

class PreferenceComponentCloseDialog : Component {

    @Composable
    override fun render() {
        AlertDialog({},
            buttons = {
                Row(horizontalArrangement = Arrangement.Center) {
                    Button({

                    }) { Text("Save") }
                    Spacer(Modifier.padding(5.dp))
                    Button({}) { Text("Cancel") }
                }
            },
            title = { Text("Save or close") },
            text = { Text("Pl") },
            properties = DialogProperties(
                size = IntSize(280, 150),
                resizable = false,
                undecorated = true
            )
        )
    }

}

fun main() {
    PreferenceComponent.instance.showMeStandalone()
}
