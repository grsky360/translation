package ilio.translation.component

fun launch(init: () -> Unit) {
    init()
    registerTray()
}
