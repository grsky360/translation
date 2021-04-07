package ilio.translation.component

import ilio.translation.utils.exit
import ilio.translation.utils.tray
import java.awt.Toolkit
import java.awt.image.BufferedImage

fun registerTray() = tray(getTrayIcon()) {
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

fun getTrayIcon(): BufferedImage {
    val size = 256
    val image = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)
    val graphics = image.createGraphics()
    graphics.color = java.awt.Color.green
    graphics.fillOval(0, 0, size, size)
    graphics.dispose()
    return image
}
