package ilio.translation.ui.support

import androidx.compose.desktop.AppWindow
import androidx.compose.desktop.WindowEvents
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.window.MenuBar
import java.awt.image.BufferedImage

fun injectContext(
    id: String,
    title: String = "",
    size: IntSize = IntSize(800, 600),
    location: IntOffset = IntOffset.Zero,
    centered: Boolean = true,
    icon: BufferedImage? = null,
    menuBar: MenuBar? = null,
    undecorated: Boolean = false,
    resizable: Boolean = true,
    events: WindowEvents = WindowEvents(),
    onDismissRequest: (() -> Unit)? = null,
    init: (AppWindow) -> Unit = {},
    content: @Composable () -> Unit = {}
) : AppWindow {
    var context = contextContainer[id]
    if (context != null) {
        return context
    }
    context = AppWindow(
        title = title.ifEmpty { id },
        size = size,
        location = location,
        centered = centered,
        icon = icon,
        menuBar = menuBar,
        undecorated = undecorated,
        resizable = resizable,
        events = events,
        onDismissRequest = onDismissRequest)

    init(context)

    contextContainer[id] = context
    contentContainer[context] = content as Any

    return context
}
