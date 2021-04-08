package ilio.translation.support.extention

import androidx.compose.desktop.AppWindow
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeysSet
import ilio.translation.utils.async
import ilio.translation.utils.data.BlockingMap
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

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

val eventContainer: ConcurrentHashMap<AppWindow, BlockingMap<String, Any>> = ConcurrentHashMap()

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
