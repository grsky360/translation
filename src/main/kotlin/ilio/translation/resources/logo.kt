package ilio.translation.resources

import java.awt.Image
import javax.imageio.ImageIO

object Logo {
    val image: Image by lazy {
        this.javaClass.getResourceAsStream("/logo.png").use {
            ImageIO.read(it)
        }
    }
}
