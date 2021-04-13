package ilio.translation.component

import ilio.translation.resources.Logo
import ilio.translation.utils.exit
import ilio.translation.utils.tray
import java.awt.Toolkit

fun registerTray() = tray(Logo.image) {
    item("Debug") {
        val toolkit = Toolkit.getDefaultToolkit()
        TranslationWindow.window.setLocation(
            toolkit.screenSize.width - 100 - TranslationWindow.window.width,
            150
        )
        TranslationWindow.showMe()
    }
    separator()

    item("OCR") {
        ocr()/*.showMe()*/
    }
    separator()

    item("Preferences") {
        PreferenceComponent.instance.showMe()
    }
    separator()

    item("Input Translate")
    item("OCR Translate")
    separator()

    item("Exit") { exit() }
}
