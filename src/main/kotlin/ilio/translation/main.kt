package ilio.translation

import androidx.compose.desktop.*
import androidx.compose.material.Text
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
    undecorated = true,
    resizable = false,
) {
    val ctx = LocalAppWindow.current
    context = ThreadLocal.withInitial { ctx }
    setVisible(false)

    getContext().events.onFocusLost = { setVisible(false) }

    SystemTray.getSystemTray().add(TrayIcon(getTrayIcon(), "tooltip").apply {
        addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                notifier.notify("aa", "bb")
                setVisible(true)
                getContext().window.requestFocus()
            }
        })
    })
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
