package ilio.translation.ui

import androidx.compose.desktop.AppWindow
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.unit.IntSize
import ilio.translation.ui.support.injectContext
import ilio.translation.support.extention.*

val preferenceContext: AppWindow by lazy {
    injectContext("preference",
        title = "Preferences",
        size = IntSize(700, 450),
        resizable = false,
        undecorated = false,
        init = {
            it.window.defaultCloseOperation = 0
            it.on(Key.Escape) {
                closeOnSave.showMe()
                it.window.isEnabled = false
                it.window.isAlwaysOnTop = false
                closeOnSave.awaitEvent<Boolean>("closeWithSave") { _ ->
                    it.hideMe()

                    it.window.isEnabled = true
                }
            }
        }
    ) {
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
        }
    }
}

val closeOnSave: AppWindow by lazy {
    injectContext(
        "close",
        title = "Save or Close",
        size = IntSize(250, 90),
        resizable = false,
        init = {
            it.enableTop()
        }
    ) {
        Row {
            Button({
                closeOnSave.hideMe()
                closeOnSave.pushEvent("closeWithSave", true)
            }) {
                Text("Save")
            }

            Button({
                closeOnSave.hideMe()
                closeOnSave.pushEvent("closeWithSave", false)
            }) {
                Text("Cancel")
            }
        }
    }
}
