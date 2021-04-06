package ilio.translation.provider.ocr.baidu

import com.baidu.aip.ocr.AipOcr
import ilio.translation.config.Configuration
import ilio.translation.utils.Clipboard.readImageClipboard
import ilio.translation.utils.async
import ilio.translation.utils.await
import kotlinx.coroutines.Deferred
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

fun takeOcr(): Deferred<List<String>> {
    val ocr = AipOcr(Configuration.Ocr.APP_ID, Configuration.Ocr.APP_KEY, Configuration.Ocr.SECRET_KEY)
    val image = readImageClipboard() ?: return async { listOf("No image in clipboard, please take a screenshot or copy an image") }
    return async {
        val response = ocr.basicGeneral(image.toByteArray(), hashMapOf(
            "detect_language" to "true"
        ))
        val words = (response.toMap()["words_result"] as? List<*>)
        return@async words?.map { (it as Map<*, *>)["words"] as String }
            ?.toList() ?: listOf("No content in image, please take a screenshot or copy an image")
    }
}

fun Image.toByteArray(): ByteArray {
    val out = ByteArrayOutputStream()
    val width = this.getWidth(null)
    val height = this.getHeight(null)
    val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    bufferedImage.createGraphics()
        .drawImage(this, 0, 0, width, height, null)
    ImageIO.write(bufferedImage, "png", ImageIO.createImageOutputStream(out))
    return out.toByteArray()
}

fun main() {
    val list = await { takeOcr() }
    list?.forEach { println(it) }
}