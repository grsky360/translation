package ilio.translation.utils

enum class OS(vararg val keys: String) {
    WINDOWS("windows"),
    MACOS("mac os x", "darwin", "osx"),
    LINUX("linux"),
    FREEBSD("freebsd"),
    UNIX("unix"),
    SOLARIS("sunos", "solaris")
}

val os: OS get() {
    val osName = System.getProperty("os.name")
    return OS.values()
        .find { it.keys.any { key -> osName.toLowerCase().contains(key) } }
        ?: OS.UNIX
}
