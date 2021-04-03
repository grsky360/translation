package ilio.translation.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import ilio.translation.support.ui.View


class TranslationUI(
    val output: MutableState<String> = mutableStateOf("")
) : View {

    override fun onCreate() {
        super.onCreate()
    }

    override fun render() {
        TODO("Not yet implemented")
    }

}
