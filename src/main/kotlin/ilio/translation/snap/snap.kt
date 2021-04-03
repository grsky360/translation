package ilio.translation.snap

import ilio.translation.ui.getTrayIcon
import ilio.translation.support.extention.minus
import ilio.translation.support.extention.plus
import ilio.translation.utils.Tray
import ilio.translation.utils.exit
import java.awt.Color
import java.awt.Frame
import java.awt.Point
import java.awt.event.*
import javax.swing.JFrame
import javax.swing.WindowConstants
import kotlin.system.exitProcess

val init_block = run {
//    System.setProperty("apple.awt.UIElement", "true")
}
val backgroundColor = Color(128, 128, 128)
val backgroundColor2 = Color(64, 64, 64)

fun main() {
    Tray(getTrayIcon()).apply {
        item("Debug") {
            snap()
        }
        separator()
        item("Exit") {
            exit()
        }
    }
//    snap()
}

fun snap(): JFrame {
    return JFrame().apply {
        layout = null
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        isUndecorated = true
        isAlwaysOnTop = true
        setLocation(0, 0)
        setBounds(0, 0, toolkit.screenSize.width, toolkit.screenSize.height)

        size = toolkit.screenSize
        isResizable = false
        extendedState = Frame.MAXIMIZED_BOTH

        opacity = 0.8f

        addKeyListener(object : KeyAdapter() {
            override fun keyPressed(e: KeyEvent) {
                when (e.keyCode) {
                    KeyEvent.VK_ESCAPE, KeyEvent.VK_SPACE, KeyEvent.VK_ENTER, KeyEvent.VK_Q -> {
                        exitProcess(0)
                    }
                }
            }
        })

        var point: Point? = null

        addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                point = e.locationOnScreen
            }

            override fun mouseReleased(e: MouseEvent) {
                point = null
            }
        })

        addMouseMotionListener(object : MouseMotionAdapter() {
            override fun mouseDragged(e: MouseEvent) {
                if (point == null) {
                    point = e.locationOnScreen
                } else {
                    location += e.locationOnScreen - point!!
                    point = e.locationOnScreen
                }
            }
        })

        isVisible = true
        requestFocus()
    }
}
