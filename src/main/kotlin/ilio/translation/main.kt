package ilio.translation

import androidx.compose.desktop.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.plus
import androidx.compose.ui.input.key.shortcuts
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.window.Notifier
import java.awt.Color
import java.awt.SystemTray
import java.awt.TrayIcon
import java.awt.event.*
import java.awt.image.BufferedImage

val init: Unit = run {
    System.setProperty("apple.awt.UIElement", "true")
}

var context: ThreadLocal<AppWindow> = ThreadLocal()
var notifier: Notifier = Notifier()

fun getContext(): AppWindow {
    return context.get()
}

fun setVisible(visible: Boolean) {
    getContext().window.isVisible = visible
}

fun main() = Window(
    size = IntSize(300, 450),
    location = IntOffset.Zero,
    centered = false,
    undecorated = true,
    resizable = false,
) {
    val ctx = LocalAppWindow.current
    context = ThreadLocal.withInitial { ctx }
    setVisible(false)

    getContext().events.onFocusLost = { setVisible(false) }

    SystemTray.getSystemTray().add(TrayIcon(getTrayIcon(), "tooltip").apply {
        addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent?) {
                if (e != null) {
                    getContext().window.setLocation(e.x, e.y)
                    setVisible(true)
                }
            }
        })
    })

    MaterialTheme {
        TextField(
            value = "",
            onValueChange = { }
        )
    }
}

fun getTrayIcon(): BufferedImage {
    val size = 256
    val image = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)
    val graphics = image.createGraphics()
    graphics.color = Color.orange
    graphics.fillOval(0, 0, size, size)
    graphics.dispose()
    return image
}
