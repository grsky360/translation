package ilio.translation.utils

import com.beust.klaxon.Klaxon

fun jsonParser(): Klaxon = Klaxon()

inline fun <reified E> String.fromJson(): E? {
    return jsonParser().parse<E>(this)
}

fun <E> E.toJson(): String {
    return jsonParser().toJsonString(this)
}

fun main() {
    val map: Map<String, String>? = """
        {"a": 1, "b": 2, "c": true, "d": false}
    """.trimIndent().fromJson()
    print(map)
    print(map.toJson())
}
