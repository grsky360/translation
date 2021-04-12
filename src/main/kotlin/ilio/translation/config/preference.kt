package ilio.translation.config

data class Preference(
    val ocr: OCRPreference = OCRPreference(),
    val translate: TranslatePreference = TranslatePreference()
)

data class OCRPreference(val appId: String = "", val appKey: String = "", val secretKey: String = "")

data class TranslatePreference(val appId: String = "", val appSecret: String = "")
