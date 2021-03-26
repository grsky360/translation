package ilio.translation

import androidx.compose.desktop.AppManager
import androidx.compose.desktop.AppWindow
import androidx.compose.desktop.LocalAppWindow
import androidx.compose.desktop.Window
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.MenuItem
import androidx.compose.ui.window.Notifier
import androidx.compose.ui.window.Tray
import java.awt.Color
import java.awt.image.BufferedImage


val init: Unit = run {
    System.setProperty("apple.awt.UIElement", "true")
}

var context: ThreadLocal<AppWindow> = ThreadLocal()
val notifier: Notifier = Notifier()

fun getContext(): AppWindow {
    return context.get()
}

fun setVisible(visible: Boolean, ctx: AppWindow = getContext()) {
    ctx.window.isVisible = visible
}

fun main() = Window(
    size = IntSize.Zero,
    resizable = false,
    undecorated = true
) {
    val ctx = LocalAppWindow.current
    context = ThreadLocal.withInitial { ctx }
    setVisible(false)

    var dialog = remember { mutableStateOf(false) }

    Tray().apply {
        icon(getTrayIcon())
        menu(
            MenuItem("Open", {
                dialog.value = true
            }),
            MenuItem("Exit", { AppManager.exit() })
        )
    }

//    if (dialog.value) {
        Dialog({
            dialog.value = false
        }) {
            Text("asdf")
        }
//    }

}

fun getTrayIcon(): BufferedImage {
    val size = 256
    val image = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)
    val graphics = image.createGraphics()
    graphics.color = Color.green
    graphics.fillOval(0, 0, size, size)
    graphics.dispose()
    return image
}
