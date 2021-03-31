package ilio.translation.utils

import androidx.compose.desktop.AppWindow
import androidx.compose.desktop.WindowEvents
import androidx.compose.runtime.Composable
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeysSet
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.window.MenuBar
import java.awt.Window
import java.awt.image.BufferedImage
import java.util.concurrent.ConcurrentHashMap
import kotlin.system.exitProcess

private val contextContainer: ConcurrentHashMap<String, AppWindow> = ConcurrentHashMap()
private val contentContainer: ConcurrentHashMap<AppWindow, Any> = ConcurrentHashMap()

fun context(id: String,
            title: String = "",
            size: IntSize = IntSize(800, 600),
            location: IntOffset = IntOffset.Zero,
            centered: Boolean = true,
            icon: BufferedImage? = null,
            menuBar: MenuBar? = null,
            undecorated: Boolean = false,
            resizable: Boolean = true,
            events: WindowEvents = WindowEvents(),
            onDismissRequest: (() -> Unit)? = null,
            init: (AppWindow) -> Unit = {},
            content: @Composable () -> Unit = {}
) : AppWindow {
    var context = contextContainer[id]
    if (context != null) {
        return context
    }
    context = AppWindow(
        title = title.ifEmpty { id },
        size = size,
        location = location,
        centered = centered,
        icon = icon,
        menuBar = menuBar,
        undecorated = undecorated,
        resizable = resizable,
        events = events,
        onDismissRequest = onDismissRequest)

    init(context)

    contextContainer[id] = context
    contentContainer[context] = content as Any

    return context
}

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

fun AppWindow.on(key: Key, callback: () -> Unit) {
    this.keyboard.setShortcut(key, callback)
}

fun AppWindow.on(keysSet: KeysSet, callback: () -> Unit) {
    this.keyboard.setShortcut(keysSet, callback)
}

fun AppWindow.hideMe() {
    this.window.isVisible = false
}

fun exit() {
    exitProcess(0)
}

fun Window.topMe() {
    isAlwaysOnTop = true
    requestFocus()
    isAlwaysOnTop = false
}
