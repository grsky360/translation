package ilio.translation.provider.baidu;

import ilio.translation.utils.SignUtils
import jodd.http.HttpRequest

const val HTTP_API = "http://api.fanyi.baidu.com/api/trans/vip/translate"
const val HTTPS_API = "https://fanyi-api.baidu.com/api/trans/vip/translate"

const val APP_ID = ""
const val APP_SECRET = ""

fun translate(request: TranslateRequest): String {
    val httpRequest = HttpRequest.get(HTTPS_API)
        .query("q", request.q)
        .query("from", request.from.code)
        .query("to", request.to.code)
        .query("appid", APP_ID)
        .query("salt", request.salt)
        .query("sign", request.sign())
    val response = httpRequest.send()
    return response.bodyText()
}

data class TranslateRequest(val q: String,
                            val from: CountryCodeEnum = CountryCodeEnum.auto, val to: CountryCodeEnum = CountryCodeEnum.en,
                            val salt: String = 16.randomString()) {
    fun sign(): String {
        return SignUtils.md5(APP_ID + q + salt + APP_SECRET)
    }
}

fun Int.randomString(): String {
    val dictChars = mutableListOf<Char>().apply { "123456789zxcvbnmasdfghjklqwertyuiop".forEach { this.add(it) } }
    val randomStr = StringBuilder().apply {
        (1..this@randomString).onEach {
            append(dictChars.random())
        }
    }
    return randomStr.toString()
}
