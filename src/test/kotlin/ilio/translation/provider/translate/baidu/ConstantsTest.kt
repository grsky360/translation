package ilio.translation.provider.translate.baidu

import io.mockk.every
import io.mockk.mockkStatic

internal val init_mock = run {
    mockkStatic(::translate)
}

fun main() {
    mockkStatic(::translate)
    val request = TranslateRequest("apple")
    every {
        translate(request)
    } returns "{}"

    println(translate(request))
}
