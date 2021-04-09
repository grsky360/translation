package ilio.translation.component

import androidx.compose.desktop.AppWindow
import androidx.compose.desktop.ComposeWindow
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ilio.translation.config.PreferenceConfig
import ilio.translation.config.PreferenceConfigData
import ilio.translation.notifier
import ilio.translation.support.component.Component
import ilio.translation.support.component.ComponentWindow
import ilio.translation.support.component.ComponentWindowConfiguration
import ilio.translation.support.extention.hideMe
import ilio.translation.support.extention.on
import ilio.translation.support.ui.*
import ilio.translation.utils.async
import ilio.translation.utils.onError
import ilio.translation.utils.toJson

class PreferenceComponent : Component {

    private var preference by mutableStateOf(PreferenceConfigData())

    @Composable
    override fun render() = Root {
        Block {
            Row.fillMaxWidth(horizontalArrangement = Arrangement.End) {
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
        Block.scrollable {
            Text("Translate API")
            Block {
                Text("Baidu")
                Divider()
                TextField(
                    value = preference.translateBaiduAppId,
                    onValueChange = {
                        preference = preference.copy(translateBaiduAppId = it)
                    },
                    singleLine = true,
                    label = { Text("appId", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer8dp()
                TextField(
                    value = preference.translateBaiduAppSecret,
                    onValueChange = {
                        preference = preference.copy(translateBaiduAppSecret = it)
                    },
                    singleLine = true,
                    label = { Text("appSecret", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(Modifier.padding(16.dp))

            Text("OCR API")
            Block {
                Text("Baidu")
                Divider()
                TextField(
                    value = preference.ocrBaiduAppId,
                    onValueChange = {
                        preference = preference.copy(ocrBaiduAppId = it)
                    },
                    singleLine = true,
                    label = { Text("appId", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer8dp()
                TextField(
                    value = preference.ocrBaiduAppKey,
                    onValueChange = {
                        preference = preference.copy(ocrBaiduAppKey = it)
                    },
                    singleLine = true,
                    label = { Text("appKey", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer8dp()
                TextField(
                    value = preference.ocrBaiduSecretKey,
                    onValueChange = {
                        preference = preference.copy(ocrBaiduSecretKey = it)
                    },
                    singleLine = true,
                    label = { Text("secretKey", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    object instance : ComponentWindow<PreferenceComponent>(::PreferenceComponent) {

        internal fun savePreference(preferenceConfigData: PreferenceConfigData) {
            async {
                println(preferenceConfigData.toJson())
            }.onError { e ->
                notifier.notify("Error", "$e")
            }
        }

        override fun configuration(): ComponentWindowConfiguration = ComponentWindowConfiguration(
            title = "Preferences",
            size = IntSize(700, 450),
            resizable = false,
            undecorated = false
        )

        override fun afterInitialize(context: AppWindow, window: ComposeWindow) {
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
