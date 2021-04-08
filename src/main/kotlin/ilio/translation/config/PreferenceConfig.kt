package ilio.translation.config

object PreferenceConfig {
    object Ocr {
        val APP_ID: String = System.getenv("c.o.appid")
        val APP_KEY: String = System.getenv("c.o.appkey")
        val SECRET_KEY: String = System.getenv("c.o.secretkey")
    }

    object Translate {
        val APP_ID: String = System.getenv("c.t.appid")
        val APP_SECRET: String = System.getenv("c.t.appsecret")
    }
}

data class PreferenceConfigData(
    val translateBaiduAppId: String = PreferenceConfig.Translate.APP_ID,
    val translateBaiduAppSecret: String = PreferenceConfig.Translate.APP_SECRET,
    val ocrBaiduAppId: String = PreferenceConfig.Ocr.APP_ID,
    val ocrBaiduAppKey: String = PreferenceConfig.Ocr.APP_KEY,
    val ocrBaiduSecretKey: String = PreferenceConfig.Ocr.SECRET_KEY
)
