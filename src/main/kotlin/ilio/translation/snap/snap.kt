package ilio.translation.snap

import java.awt.*
import java.awt.event.*
import java.awt.geom.RoundRectangle2D
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.WindowConstants
import kotlin.system.exitProcess

val init_block = run {
//    System.setProperty("apple.awt.UIElement", "true")
}
val backgroundColor = Color(128, 128, 128)
val backgroundColor2 = Color(64, 64, 64)

val toolkit = Toolkit.getDefaultToolkit()

fun main() {
    JFrame().apply {
        layout = null
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        isUndecorated = true
        isAlwaysOnTop = true
        setLocation(0, 0)

        size = toolkit.screenSize
        isResizable = false
//        extendedState = Frame.MAXIMIZED_BOTH

//        val buff_img = Robot().createScreenCapture(
//            Rectangle(
//                0, 0,
//                toolkit.screenSize.width,
//                toolkit.screenSize.height
//            )
//        )
        val label = object : JLabel() {
            override fun paintComponent(g: Graphics) {
                super.paintComponent(g)
//                g.drawImage(buff_img, 0, 0, this)
                val g2d = g as Graphics2D
                g2d.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
                )
                g2d.color = backgroundColor
                val composite = AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, 60 / 100.0f
                )
                g2d.composite = composite
                g2d.fill(
                    RoundRectangle2D.Float(
                        0f, 0f, this.width.toFloat(), this
                            .height.toFloat(), 0f, 0f
                    )
                )
            }
        }
        label.setBounds(0, 0, width, height)
        add(label)

        addKeyListener(object : KeyAdapter() {
            override fun keyPressed(e: KeyEvent) {
                if (e.keyCode == KeyEvent.VK_ESCAPE) {
                    exitProcess(0)
                }
            }
        })

        val point = Point()
        addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                point.x = e.x
                point.y = e.y
            }
        })

        addMouseMotionListener(object : MouseMotionAdapter() {
            override fun mouseDragged(e: MouseEvent) {
                val offsetX = e.xOnScreen - point.x
                val offsetY = e.yOnScreen - point.y
                setLocation(location.x + offsetX, location.y + offsetY)

                point.x = e.x
                point.y = e.y
                println("${e.xOnScreen}, ${e.yOnScreen}")
            }
        })

        isVisible = true
        requestFocus()
        Thread.sleep(500)
    }
}
