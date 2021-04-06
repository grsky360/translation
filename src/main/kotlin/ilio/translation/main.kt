package ilio.translation

import androidx.compose.ui.window.Notifier
import ilio.translation.component.launch
import java.awt.Toolkit


val notifier: Notifier by lazy { Notifier() }
val toolkit: Toolkit by lazy { Toolkit.getDefaultToolkit() }

fun main() = launch {
    if (System.getProperty("debugging") != "true") {
        System.setProperty("apple.awt.UIElement", "true")
    }
}
