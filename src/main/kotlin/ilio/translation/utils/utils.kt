@file:JvmName("Utils")

package ilio.translation.utils

import com.google.common.hash.Hashing
import kotlinx.coroutines.*
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import kotlin.system.exitProcess

//val json: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

fun exit(code: Int = 0) {
    exitProcess(code)
}

@Suppress("deprecation")
fun md5(payload: String, charset: Charset = StandardCharsets.UTF_8) =
    Hashing.md5().hashString(payload, charset).toString()

fun Int.randomString(): String {
    val dictChars = mutableListOf<Char>().apply { "123456789zxcvbnmasdfghjklqwertyuiop".forEach { this.add(it) } }
    val randomStr = StringBuilder().apply {
        (1..this@randomString).onEach {
            append(dictChars.random())
        }
    }
    return randomStr.toString()
}

//fun <T> String.parseJson(clazz: Class<T>): T {
////    if (!clazz.isAnnotationPresent(JsonClass::class.java)) {
////        TODO("NOT SUPPORT without @JsonClass")
////    }
//    return json.adapter(clazz).fromJson(this)!!
//}
