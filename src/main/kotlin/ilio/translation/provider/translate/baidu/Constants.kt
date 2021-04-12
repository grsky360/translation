package ilio.translation.provider.translate.baidu;

import ilio.translation.config.preference
import ilio.translation.utils.md5
import ilio.translation.utils.randomString
import jodd.http.HttpRequest

const val HTTP_API = "http://api.fanyi.baidu.com/api/trans/vip/translate"
const val HTTPS_API = "https://fanyi-api.baidu.com/api/trans/vip/translate"

fun translate(request: TranslateRequest): String {
    val httpRequest = HttpRequest.post(HTTPS_API)
        .form("q", request.q)
        .form("from", request.from.code)
        .form("to", request.to.code)
        .form("appid", preference.translate.appId)
        .form("salt", request.salt)
        .form("sign", request.sign())
        .contentType("application/x-www-form-urlencoded")
    val response = httpRequest.send()
    return response.bodyText()
}

data class TranslateRequest(val q: String,
                            val from: CountryCodeEnum = CountryCodeEnum.auto,
                            val to: CountryCodeEnum = CountryCodeEnum.auto,
                            val salt: String = 16.randomString()) {
    fun sign(): String {
        return md5(preference.translate.appId + q + salt + preference.translate.appSecret)
    }
}

data class TranslateResult(val src: String, val dst: String)

data class TranslateResponse(val from: String, val to: String,
                             val error_code: Int,
                             val trans_result: List<TranslateResult>)
