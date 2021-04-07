package ilio.translation.component

import androidx.compose.desktop.AppWindow
import androidx.compose.foundation.layout.Row
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.unit.IntSize
import ilio.translation.support.extention.enableTop
import ilio.translation.support.injectContext

val closeOnSave: AppWindow by lazy {
    injectContext(
        "close",
        title = "Save or Close",
        size = IntSize(250, 90),
        resizable = false,
        init = {
            it.enableTop()
        }
    ) {
        AlertDialog({}, buttons = {
            Row {
                Button({}) { Text("Save") }
                Button({}) { Text("Cancel") }
            }
        })
//        Row {
//            Button({
//                closeOnSave.hideMe()
//                closeOnSave.pushEvent("closeWithSave", true)
//            }) {
//                Text("Save")
//            }
//
//            Button({
//                closeOnSave.hideMe()
//                closeOnSave.pushEvent("closeWithSave", false)
//            }) {
//                Text("Cancel")
//            }
//        })
    }
}
