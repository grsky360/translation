package ilio.translation.support.component

import androidx.compose.desktop.AppWindow
import androidx.compose.desktop.ComposeWindow
import androidx.compose.desktop.WindowEvents
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.window.MenuBar
import ilio.translation.support.extention.topMe
import java.awt.image.BufferedImage
import javax.swing.SwingUtilities
import javax.swing.WindowConstants

abstract class ComponentWindow<T: Component>(val factory: () -> T) {
    private var initialized: Boolean = false
    val context: AppWindow by lazy { initialize() }
    val window: ComposeWindow get() = context.window
    val component by container(factory)
    private val configuration: ComponentWindowConfiguration by lazy { configuration() }

    protected open fun afterInitialize(context: AppWindow, window: ComposeWindow) {
        /** Do nothing **/
    }

    protected open fun configuration(): ComponentWindowConfiguration = ComponentWindowConfiguration()

    private fun initialize(): AppWindow {
        val context = AppWindow(
            configuration.title,
            configuration.size,
            configuration.location,
            configuration.centered,
            configuration.icon,
            configuration.menuBar,
            configuration.undecorated,
            configuration.resizable,
            configuration.events,
            configuration.onDismissRequest
        )
        afterInitialize(context, context.window)
        return context
    }

    fun showMeStandalone() {
        SwingUtilities.invokeAndWait {
            showMe()
            window.defaultCloseOperation = WindowConstants.HIDE_ON_CLOSE
        }
    }

    fun showMe() {
        if (!initialized) {
            context.show {
                factory().render()
            }
            initialized = true
        } else {
            window.isVisible = true
        }
        context.topMe()
    }

    fun hideMe() {
        window.isVisible = false
    }
}

data class ComponentWindowConfiguration(
    val title: String = "JetpackDesktopWindow",
    val size: IntSize = IntSize(800, 600),
    val location: IntOffset = IntOffset.Zero,
    val centered: Boolean = true,
    val icon: BufferedImage? = null,
    val menuBar: MenuBar? = null,
    val undecorated: Boolean = false,
    val resizable: Boolean = true,
    val events: WindowEvents = WindowEvents(),
    val onDismissRequest: (() -> Unit)? = null
)
