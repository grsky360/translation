package ilio.translation.support

import androidx.compose.desktop.AppWindow
import ilio.translation.utils.data.BlockingMap
import java.util.concurrent.ConcurrentHashMap

val contextContainer: ConcurrentHashMap<String, AppWindow> = ConcurrentHashMap()

val contentContainer: ConcurrentHashMap<AppWindow, Any> = ConcurrentHashMap()

val eventContainer: ConcurrentHashMap<AppWindow, BlockingMap<String, Any>> = ConcurrentHashMap()
