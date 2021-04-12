package ilio.translation.utils

import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

class Tray(image: Image) {
    private val trayIcon: TrayIcon = TrayIcon(image)

    init {
        trayIcon.isImageAutoSize = true
        trayIcon.popupMenu = PopupMenu()
        if (os == OS.WINDOWS) {
            trayIcon.addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent?) {
                    println(e)
                }
            })
        }
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
