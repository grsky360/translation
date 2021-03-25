package ilio.translation

import androidx.compose.desktop.ComposePanel
import androidx.compose.desktop.LocalAppWindow
import androidx.compose.desktop.Window
import androidx.compose.material.Text
import androidx.compose.ui.window.Notifier
import java.awt.Color
import java.awt.SystemTray
import java.awt.TrayIcon
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.image.BufferedImage

fun main() {
    preInit()
    window()
}

fun preInit() {
    System.setProperty("apple.awt.UIElement", "true")
}

fun window() = Window {
    val notifier = Notifier()
    val context = LocalAppWindow.current
    context.window.isVisible = false

//    val popup = Popup() {
//        Text("this is popup")
//    }

    val trayIcon = TrayIcon(getTrayIcon(), "tooltip")
    trayIcon.addMouseListener(object: MouseAdapter() {
        override fun mouseClicked(e: MouseEvent?) {
            notifier.notify("aa", "bb")
            ComposePanel().apply {
                setContent {
                    Text("this is compose panal")
                }

                isVisible = true
            }

        }
    })

    SystemTray.getSystemTray().add(trayIcon)
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
