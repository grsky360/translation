package ilio.translation.support.component

import androidx.compose.desktop.AppWindow
import androidx.compose.desktop.WindowEvents
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.window.MenuBar
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import java.awt.Window
import java.awt.image.BufferedImage
import javax.swing.SwingUtilities

abstract class ComponentWindow {
    private var initialized: Boolean = false
    private val context: AppWindow by lazy { initialize() }
    private val configuration: ComponentWindowConfiguration by lazy { configuration() }

    val window: Window get() = context.window

    protected open fun afterInitialize(context: AppWindow) {}

    protected abstract fun factory(): (ComponentContext) -> Component

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
        afterInitialize(context)
        return context
    }

    fun showMeStandalone() {
        SwingUtilities.invokeAndWait {
            showMe()
        }
    }

    fun showMe() {
        if (!initialized) {
            context.show {
                rememberRootComponent(factory = factory()).render()
            }
            initialized = true
        } else {
            window.isVisible = true
        }
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
