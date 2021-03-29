package ilio.translation

import androidx.compose.desktop.AppManager
import androidx.compose.desktop.AppWindow
import androidx.compose.desktop.WindowEvents
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.window.MenuItem
import androidx.compose.ui.window.Notifier
import androidx.compose.ui.window.Tray
import java.awt.Window
import java.awt.image.BufferedImage
import javax.swing.JFrame
import kotlin.system.exitProcess

private val init_block: Unit = run {
    // It will cause no auto focus while debugging
    System.setProperty("apple.awt.UIElement", "true")
}

val notifier: Notifier = Notifier()

fun main() {
    val frame = JFrame()
    frame.setSize(300, 450)
//    frame.isVisible = false
    Tray().apply {
        icon(getTrayIcon())
        menu(
            MenuItem("Open", {
                frame.isVisible = true
                frame.topMe()
//                ctx().show()
            }),
            MenuItem("Exit", { exitProcess(0) })
        )
    }

}

fun ctx(): AppWindow = Context("main") {
    val context = AppWindow(
        size = IntSize(700, 450),
        resizable = false,
        undecorated = false,
        events = WindowEvents(
            onClose = {
                print("abc")
            }
        )
    ).apply {
        window.apply {
////            isAutoRequestFocus = true
//            isFocusable = true
        }
    }

    context.show {
        MaterialTheme {
            Button(
                onClick = { AppManager.exit() },
                colors = buttonColors(backgroundColor = Color.Red)
            ) {
                Text("Exit")
            }

        }
    }
    context
}

fun getTrayIcon(): BufferedImage {
    val size = 256
    val image = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)
    val graphics = image.createGraphics()
    graphics.color = java.awt.Color.green
    graphics.fillOval(0, 0, size, size)
//    graphics.dispose()
    return image
}

fun Window.topMe() {
    isAlwaysOnTop = true
    isAlwaysOnTop = false
}

fun AppWindow.show() {
    this.window.apply {
        isVisible = true
        topMe()
        val focusRequester = FocusRequester()
        focusRequester.requestFocus()
    }
}

fun AppWindow.hide() {
    this.window.isVisible = false
}
