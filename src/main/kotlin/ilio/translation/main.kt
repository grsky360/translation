package ilio.translation

import androidx.compose.desktop.AppWindow
import androidx.compose.desktop.WindowEvents
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.window.Notifier
import com.google.gson.Gson
import ilio.translation.provider.baidu.TranslateRequest
import ilio.translation.provider.baidu.translate
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.awt.SystemTray
import java.awt.TrayIcon
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.image.BufferedImage

private val init_block: Unit = run {
    // It will cause no auto focus while debugging
    if (System.getProperty("debugging") != "true") {
        System.setProperty("apple.awt.UIElement", "true")
    }
}

val notifier: Notifier = Notifier()
val gson: Gson = Gson()

fun main() {
    val defaultIcon = getTrayIcon()
    val pressIcon = getTrayIcon(
        foreground = java.awt.Color.red,
        background = java.awt.Color(0x4C86ED))
    val tray = TrayIcon(pressIcon)
    tray.addMouseListener(object : MouseAdapter() {
        override fun mousePressed(e: MouseEvent?) {
            tray.image = pressIcon
            if (e != null) {
                popup().window.setLocation(e.x, e.y)
                popup().showMe()
            }
        }

        override fun mouseReleased(e: MouseEvent?) {
            tray.image = defaultIcon
        }
    })

    SystemTray.getSystemTray().add(tray)
}

fun ctx(): AppWindow = context("main",
    size = IntSize(700, 450),
    resizable = false,
    undecorated = false,
    init = {
        it.window.defaultCloseOperation = 0
        it.keyboard.setShortcut(Key.Escape) {
            // TODO: Confirm to save or close
            it.hideMe()
        }
    }
) {
    MaterialTheme {
        Button(
            onClick = { ctx().exit() },
            colors = buttonColors(backgroundColor = Color.Red)
        ) {
            Text("Exit")
        }
    }
}

fun popup(): AppWindow = context("popup",
    size = IntSize(300, 450),
    undecorated = true,
    resizable = false,
    events = WindowEvents(
        onFocusLost = {
            popup().hideMe()
        }
    ),
    init = {
        it.window.isAlwaysOnTop = true
    }
) {
    val input = remember { mutableStateOf("") }
    val output = remember { mutableStateOf("asdf") }

    MaterialTheme {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TextField(value = input.value, onValueChange = { input.value = it })

            Button({
                GlobalScope.async {
                    val response = translate(TranslateRequest(input.value))
                    val map = gson.fromJson(response, Map::class.java)
                    @Suppress("UNCHECKED_CAST")
                    output.value = (map["trans_result"] as List<Map<String, *>>)[0]["dst"] as String
                }
            }) {
                Text("Translate it")
            }

            Text(output.value)
        }
    }
}

fun getTrayIcon(foreground: java.awt.Color = java.awt.Color.green, background: java.awt.Color ?= null): BufferedImage {
    val size = 256
    val image = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)
    val graphics = image.createGraphics()
    graphics.color = foreground
    if (background != null) {
        graphics.background = background
    }
    graphics.fillOval(0, 0, size, size)
    graphics.dispose()
    return image
}
