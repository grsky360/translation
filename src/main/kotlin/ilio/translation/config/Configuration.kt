package ilio.translation.config

object Configuration {
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