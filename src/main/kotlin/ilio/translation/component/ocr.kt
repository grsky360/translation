package ilio.translation.component

import androidx.compose.desktop.LocalAppWindow
import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import ilio.translation.provider.ocr.baidu.takeOcr
import ilio.translation.utils.onSuccess

fun ocr() = Window("ocr_popup", undecorated = true) {
    val ctx = LocalAppWindow.current
    var ocrResult by remember { mutableStateOf("TextFieldValue(text = value") }
    takeOcr().onSuccess { list ->
        ocrResult = list?.joinToString("\n") ?: ""
    }
    Row {
        Button({
            ctx.window.dispose()
        }) {
            Text("Hide me")
        }
        Button({
            ocrResult += "avc"
        }) {
            Text("Click me")
        }
        TextField(value = ocrResult, onValueChange = {
            ocrResult = it
        })
    }
}