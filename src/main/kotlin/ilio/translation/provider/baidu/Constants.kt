package ilio.translation.provider.baidu;

import ilio.translation.utils.SignUtils;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import java.lang.Exception

import java.nio.charset.StandardCharsets;

object Constants {
    private const val HTTP_API = "http://api.fanyi.baidu.com/api/trans/vip/translate"
    private const val HTTPS_API = "https://fanyi-api.baidu.com/api/trans/vip/translate"
    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val request = HttpRequest.get(HTTPS_API)
            .query("q", "翻译")
            .query("from", "auto")
            .query("to", CountryCodeEnum.en.code)
            .query("appid", "")
            .query("salt", "ABCD")
            .query("sign", SignUtils.md5("", StandardCharsets.UTF_8))
        val response = request.send()
        println(response.bodyText())
    }
}
