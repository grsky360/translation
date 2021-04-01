package ilio.translation.ui.support.shortcut

import androidx.compose.desktop.AppWindow
import androidx.compose.runtime.Composable
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeysSet
import ilio.translation.ui.support.contentContainer
import ilio.translation.ui.support.eventContainer
import ilio.translation.utils.async
import ilio.translation.utils.data.BlockingMap
import java.util.concurrent.TimeUnit

fun AppWindow.showMe() {
    val content = contentContainer[this]
    if (content != null) {
        this.show {
            @Suppress("UNCHECKED_CAST")
            (content as @Composable () -> Unit)()
        }
        contentContainer.remove(this)
    } else {
        this.window.apply {
            isVisible = true
            topMe()
        }
    }
}

fun AppWindow.topMe() {
    window.apply {
        isAlwaysOnTop = true
        requestFocus()
        isAlwaysOnTop = false
    }
}

fun AppWindow.enableTop() {
    window.apply {
        isAlwaysOnTop = true
        requestFocus()
    }
}

fun AppWindow.disableTop() {
    window.isAlwaysOnTop = false
}

fun AppWindow.hideMe() {
    this.window.isVisible = false
}

fun AppWindow.on(key: Key, callback: () -> Unit) {
    this.keyboard.setShortcut(key, callback)
}

fun AppWindow.on(keysSet: KeysSet, callback: () -> Unit) {
    this.keyboard.setShortcut(keysSet, callback)
}

fun AppWindow.pushEvent(key: String, value: Any) {
    eventContainer
        .computeIfAbsent(this) { BlockingMap<String, Any>() }
        .push(key, value)
}

@Suppress("UNCHECKED_CAST")
fun <T> AppWindow.takeEvent(key: String): T? {
    return eventContainer
        .computeIfAbsent(this) { BlockingMap<String, Any>() }
        .take(key, 0, TimeUnit.SECONDS) as T
}

@Suppress("UNCHECKED_CAST")
fun <T> AppWindow.awaitEvent(key: String): T? {
    return eventContainer
        .computeIfAbsent(this) { BlockingMap<String, Any>() }
        .take(key) as T
}

fun <T> AppWindow.awaitEvent(key: String, block: (T?) -> Unit) = async { block(awaitEvent<T>(key)) }