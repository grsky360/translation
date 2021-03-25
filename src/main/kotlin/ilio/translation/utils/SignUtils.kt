package ilio.translation.utils

import com.google.common.hash.Hashing
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

@Suppress("deprecation")
fun md5(payload: String, charset: Charset = StandardCharsets.UTF_8) = Hashing.md5().hashString(payload, charset).toString()

fun main() {
}