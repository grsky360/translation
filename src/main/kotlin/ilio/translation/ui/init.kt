package ilio.translation.ui

import androidx.compose.ui.window.Notifier
import java.awt.Toolkit

val init = {
    // It will cause no auto focus while debugging
    if (System.getProperty("debugging") != "true") {
        System.setProperty("apple.awt.UIElement", "true")
    }
}

val notifier: Notifier by lazy { Notifier() }

val toolkit: Toolkit by lazy { Toolkit.getDefaultToolkit() }
