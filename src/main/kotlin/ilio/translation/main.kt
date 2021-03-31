package ilio.translation

import androidx.compose.desktop.AppWindow
import androidx.compose.desktop.WindowEvents
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.window.Notifier
import ilio.translation.provider.translate.baidu.TranslateRequest
import ilio.translation.provider.translate.baidu.translate
import ilio.translation.utils.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.awt.Toolkit
import java.awt.image.BufferedImage

private val init_block: Unit = run {
    // It will cause no auto focus while debugging
    if (System.getProperty("debugging") != "true") {
        System.setProperty("apple.awt.UIElement", "true")
    }
}

val notifier: Notifier = Notifier()

fun main() {
    tray(getTrayIcon()) {
        item("Debug") {
            val toolkit = Toolkit.getDefaultToolkit()
            popup().window.setLocation(
                toolkit.screenSize.width - 100 - popup().width,
                150
            )
            popup().showMe()
        }
        separator()

        item("Preferences") {
            ctx().showMe()
        }
        separator()

        item("Input Translate")
        item("OCR Translate")
        separator()

        item("Exit") { exit() }
    }
}

fun ctx(): AppWindow = context("main",
    size = IntSize(700, 450),
    resizable = false,
    undecorated = false,
    init = {
        it.window.defaultCloseOperation = 0
        it.on(Key.Escape) {
            context("close", title = "Save or Close", size = IntSize(250, 90), resizable = false) {
                Row {
                    Button({
                        it.hideMe()
                        context("close").hideMe()
                    }) {
                        Text("Save")
                    }

                    Button({
                        it.hideMe()
                        context("close").hideMe()
                    }) {
                        Text("Cancel")
                    }
                }
            }.showMe()
        }
    }
) {
    MaterialTheme {
        val selectedIndex = remember { mutableStateOf(0) }
        TabRow(selectedTabIndex = selectedIndex.value) {
            Tab(selected = true, onClick = {

            }, text = {
                Text("abc")
            })
            Tab(selected = false, onClick = {
            }, text = {
                Text("abc")
            })
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
        it.on(Key.Escape) { it.hideMe() }
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
                    output.value = response
//                    val map = gson.fromJson(response, Map::class.java)
//                    @Suppress("UNCHECKED_CAST")
//                    output.value = (map["trans_result"] as List<Map<String, *>>)[0]["dst"] as String
                }
            }) {
                Text("Translate it")
            }

            Text(output.value)
        }
    }
}

fun getTrayIcon(): BufferedImage {
    val size = 256
    val image = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)
    val graphics = image.createGraphics()
    graphics.color = java.awt.Color.green
    graphics.fillOval(0, 0, size, size)
    graphics.dispose()
    return image
}
