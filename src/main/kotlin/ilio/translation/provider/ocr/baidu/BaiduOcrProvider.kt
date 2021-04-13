package ilio.translation.provider.ocr.baidu

import com.baidu.aip.ocr.AipOcr
import ilio.translation.config.preference
import ilio.translation.utils.Clipboard.readImageClipboard
import ilio.translation.utils.async
import ilio.translation.utils.await
import ilio.translation.utils.toByteArray
import kotlinx.coroutines.Deferred

fun takeOcr(): Deferred<List<String>> {
    val ocr = AipOcr(preference.ocr.appId, preference.ocr.appKey, preference.ocr.secretKey)
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

fun main() {
    val list = await { takeOcr() }
    list?.forEach { println(it) }
}