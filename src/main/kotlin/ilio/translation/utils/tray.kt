package ilio.translation.utils

import java.awt.*
import java.awt.event.ActionEvent

class Tray(image: Image) {
    private val trayIcon: TrayIcon = TrayIcon(image)

    init {
        trayIcon.isImageAutoSize = true
        trayIcon.popupMenu = PopupMenu()
        SystemTray.getSystemTray().add(trayIcon)
    }

    fun separator() {
        trayIcon.popupMenu.addSeparator()
    }

    fun item(text: String, shortcut: MenuShortcut? = null, onclick: (e: ActionEvent) -> Unit = {}) {
        val menuItem = MenuItem(text)
        if (shortcut != null) {
            menuItem.shortcut = shortcut
        }
        menuItem.addActionListener(onclick)
        trayIcon.popupMenu.add(menuItem)
    }
}

fun tray(image: Image, block: Tray.() -> Unit) {
    val tray = Tray(image)
    tray.block()
}
