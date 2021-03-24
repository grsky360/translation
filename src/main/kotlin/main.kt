import androidx.compose.desktop.LocalAppWindow
import androidx.compose.desktop.Window
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.window.Tray
import java.awt.Color
import java.awt.image.BufferedImage

fun main() = Window {
    val context = LocalAppWindow.current
    context.window.isVisible = false

    DisposableEffect {
        val tray = Tray().apply {
            icon(getTrayIcon())
            menu(
                MenuItem(
                    name = "Increment value",
                    onClick = {
                        count.value++
                    }
                ),
                MenuItem(
                    name = "Send notification",
                    onClick = {
                        notify("Notification", "Message from MyApp!")
                    }
                ),
                MenuItem(
                    name = "Exit",
                    onClick = {
                        AppManager.exit()
                    }
                )
            )
        }
        onDispose {
            tray.remove()
        }
    }
}

fun getTrayIcon(): BufferedImage {
    val size = 256
    val image = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)
    val graphics = image.createGraphics()
    graphics.setColor(Color.orange)
    graphics.fillOval(0, 0, size, size)
    graphics.dispose()
    return image
}