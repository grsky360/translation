package ilio.translation

import androidx.compose.desktop.AppWindow
import java.util.concurrent.ConcurrentHashMap

private val contextContainer = ConcurrentHashMap<String, AppWindow>()

fun Context(name: String, block: () -> AppWindow): AppWindow {
    var appWindow = contextContainer[name]
    if (appWindow != null) {
        return appWindow
    }
    appWindow = block()
    contextContainer[name] = appWindow
    return appWindow
}
