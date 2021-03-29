package ilio.translation

import androidx.compose.desktop.AppWindow
import androidx.compose.desktop.WindowEvents
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.window.MenuBar
import java.awt.image.BufferedImage
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

fun context(name: String,
            title: String = "JetpackDesktopWindow",
            size: IntSize = IntSize(800, 600),
            location: IntOffset = IntOffset.Zero,
            centered: Boolean = true,
            icon: BufferedImage? = null,
            menuBar: MenuBar? = null,
            undecorated: Boolean = false,
            resizable: Boolean = true,
            events: WindowEvents = WindowEvents(),
            onDismissRequest: (() -> Unit)? = null,
            content: @Composable () -> Unit) : AppWindow {
    var appWindow = contextContainer[name]
    if (appWindow != null) {
        return appWindow
    }
    appWindow = AppWindow(
        title = title,
        size = size,
        location = location,
        centered = centered,
        icon = icon,
        menuBar = menuBar,
        undecorated = undecorated,
        resizable = resizable,
        events = events,
        onDismissRequest = onDismissRequest
    )

    appWindow.show {
        content()
    }
    contextContainer[name] = appWindow
    return appWindow
}
