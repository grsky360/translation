package ilio.translation.config

import ilio.translation.utils.fromJson
import ilio.translation.utils.toJson
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

internal object Constants {
    private val USER_HOME: String = System.getProperty("user.home")
    val appHome: String get() = "$USER_HOME/.ilio/translation"
    val configFile: String get() = "$appHome/config.json"

    var preference: Preference? = null
}

val preference: Preference get() {
    if (Constants.preference == null) {
        Constants.preference = load()
    }
    return Constants.preference!!
}

fun Preference.reload(): Preference {
    Constants.preference = load()
    return Constants.preference!!
}

fun Preference.save() {
    File(Constants.appHome).apply {
        if (!exists()) {
            mkdirs()
        }
    }
    FileOutputStream(Constants.configFile).bufferedWriter().use {
        it.write(this.toJson())
    }
}

internal fun load(): Preference {
    File(Constants.configFile).apply {
        if (!exists()) {
            return Preference()
        }
        if (!isFile) {
            delete()
            return Preference()
        }
    }
    val config = FileInputStream(Constants.configFile).bufferedReader().use { it.readText() }
    return try {
        config.fromJson<Preference>() ?: Preference()
    } catch (e: Exception) {
        e.printStackTrace()
        Preference();
    }
}
