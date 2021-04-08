package ilio.translation.component

import androidx.compose.desktop.AppWindow
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import ilio.translation.config.PreferenceConfig
import ilio.translation.config.PreferenceConfigData
import ilio.translation.support.component.Component
import ilio.translation.support.component.ComponentWindow
import ilio.translation.support.component.ComponentWindowConfiguration
import ilio.translation.support.extention.hideMe
import ilio.translation.support.extention.on
import ilio.translation.utils.toJson

class PreferenceComponent : Component {

    private var preference by mutableStateOf(PreferenceConfigData())

    @Composable
    override fun render() {
        MaterialTheme {
            Column(verticalArrangement = Arrangement.Center) {
                Text("Translate API")
                Box {
                    Column {
                        Text("Baidu")
                        Divider()
                        Row {
                            Text("appId")
                            TextField(preference.translateBaiduAppId, {
                                preference = preference.copy(translateBaiduAppId = it)
                            }, singleLine = true)
                        }
                        Row {
                            Text("appSecret")
                            TextField(preference.translateBaiduAppSecret, {
                                preference = preference.copy(translateBaiduAppSecret = it)
                            }, singleLine = true)
                        }
                    }
                }
                Spacer(Modifier.padding(16.dp))

                Text("OCR API")
                Box {
                    Column {
                        Text("Baidu")
                        Divider()
                        Row {
                            Text("appId")
                            TextField(preference.ocrBaiduAppId, {
                                preference = preference.copy(ocrBaiduAppId = it)
                            }, singleLine = true)
                        }
                        Row {
                            Text("appKey")
                            TextField(preference.ocrBaiduAppKey, {
                                preference = preference.copy(ocrBaiduAppKey = it)
                            }, singleLine = true)
                        }
                        Row {
                            Text("secretKey")
                            TextField(preference.ocrBaiduSecretKey, {
                                preference = preference.copy(ocrBaiduSecretKey = it)
                            }, singleLine = true)
                        }
                    }
                }

                Row {
                    Button({
                        instance.hideMe()
                    }) {
                        Text("Cancel")
                    }
                    Button({
                        instance.savePreference(preference)
                    }) {
                        Text("Apply")
                    }
                    Button({
                        instance.savePreference(preference)
                        instance.hideMe()
                    }) {
                        Text("OK")
                    }
                }
            }
        }
    }

    object instance : ComponentWindow<PreferenceComponent>(::PreferenceComponent) {

        internal fun savePreference(preferenceConfigData: PreferenceConfigData) {
            println(preferenceConfigData.toJson())
        }

        override fun configuration(): ComponentWindowConfiguration = ComponentWindowConfiguration(
            title = "Preferences",
            size = IntSize(700, 450),
            resizable = false,
            undecorated = false
        )

        override fun afterInitialize(context: AppWindow) {
            context.window.defaultCloseOperation = 0
            context.on(Key.Escape) {
                context.hideMe()
            }
        }
    }
}

data class PreferenceConfig(
    val translateBaiduAppId: String = PreferenceConfig.Translate.APP_ID,
    val translateBaiduAppSecret: String = PreferenceConfig.Translate.APP_SECRET,
    val ocrBaiduAppId: String = PreferenceConfig.Ocr.APP_ID,
    val ocrBaiduAppKey: String = PreferenceConfig.Ocr.APP_ID,
    val ocrBaiduSecretKey: String = PreferenceConfig.Ocr.APP_ID
)

fun main() {
    PreferenceComponent.instance.showMeStandalone()
}
