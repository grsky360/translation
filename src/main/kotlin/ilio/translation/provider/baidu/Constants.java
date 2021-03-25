package ilio.translation.provider.baidu;

import ilio.translation.utils.SignUtilsKt;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;

import java.nio.charset.StandardCharsets;

public class Constants {

    private static final String HTTP_API = "http://api.fanyi.baidu.com/api/trans/vip/translate";
    private static final String HTTPS_API = "https://fanyi-api.baidu.com/api/trans/vip/translate";

    public static void main(String[] args) throws Exception {
        HttpRequest request = HttpRequest.get(HTTPS_API)
            .query("q", "翻译")
            .query("from", "auto")
            .query("to", CountryCodeEnum.en.getCode())
            .query("appid", "")
            .query("salt", "ABCD")
            .query("sign", SignUtilsKt.md5("", StandardCharsets.UTF_8));

        HttpResponse response = request.send();
        System.out.println(response.bodyText());
    }
}
