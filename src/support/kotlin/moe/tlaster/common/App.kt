package moe.tlaster.common

import androidx.compose.desktop.Window
import androidx.compose.desktop.WindowEvents
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.window.MenuBar
import moe.tlaster.common.scene.NoteDetailScene
import moe.tlaster.common.scene.NoteEditScene
import moe.tlaster.common.scene.NoteListScene
import moe.tlaster.precompose.lifecycle.LifecycleOwner
import moe.tlaster.precompose.lifecycle.LifecycleRegistry
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import moe.tlaster.precompose.navigation.transition.fadeScaleCreateTransition
import moe.tlaster.precompose.navigation.transition.fadeScaleDestroyTransition
import moe.tlaster.precompose.ui.*
import moe.tlaster.precompose.viewmodel.ViewModelStore
import moe.tlaster.precompose.viewmodel.ViewModelStoreOwner
import java.awt.image.BufferedImage

@ExperimentalMaterialApi
fun main() = PreComposeWindow {
    App()
}

fun PreComposeWindow(
    title: String = "JetpackDesktopWindow",
    size: IntSize = IntSize(800, 600),
    location: IntOffset = IntOffset.Zero,
    centered: Boolean = true,
    icon: BufferedImage? = null,
    menuBar: MenuBar? = null,
    undecorated: Boolean = false,
    resizable: Boolean = true,
    events: WindowEvents = WindowEvents(),
    onDismissRequest: (() -> Unit)? = null,
    content: @Composable () -> Unit = { }
) {
    Window(
        title,
        size,
        location,
        centered,
        icon,
        menuBar,
        undecorated,
        resizable,
        events,
        onDismissRequest,
    ) {
        ProvideDesktopCompositionLocals {
            content.invoke()
        }
    }
}

@Composable
private fun ProvideDesktopCompositionLocals(
    content: @Composable () -> Unit,
) {
    val holder = remember {
        PreComposeWindowHolder()
    }
    CompositionLocalProvider(
        LocalLifecycleOwner provides holder,
        LocalViewModelStoreOwner provides holder,
        LocalBackDispatcherOwner provides holder,
    ) {
        content.invoke()
    }
}

private class PreComposeWindowHolder : LifecycleOwner, ViewModelStoreOwner, BackDispatcherOwner {
    override val lifecycle by lazy {
        LifecycleRegistry()
    }
    override val viewModelStore by lazy {
        ViewModelStore()
    }
    override val backDispatcher by lazy {
        BackDispatcher()
    }
}

@ExperimentalMaterialApi
@Composable
fun App() {
    val navigator = rememberNavigator()
    BoxWithConstraints {
        MaterialTheme {
            NavHost(
                navigator = navigator,
                initialRoute = "/home"
            ) {
                scene("/home") {
                    NoteListScene(
                        onItemClicked = {
                            navigator.navigate("/detail/${it.id}")
                        },
                        onAddClicked = {
                            navigator.navigate("/edit")
                        },
                        onEditClicked = {
                            navigator.navigate("/edit/${it.id}")
                        }
                    )
                }
                scene("/detail/{id:[0-9]+}") { backStackEntry ->
                    backStackEntry.path<Int>("id")?.let {
                        NoteDetailScene(
                            id = it,
                            onEdit = {
                                navigator.navigate("/edit/$it")
                            },
                            onBack = {
                                navigator.goBack()
                            },
                        )
                    }
                }
                scene(
                    "/edit/{id:[0-9]+}?",
                    navTransition = NavTransition(
                        createTransition = {
                            translationY = constraints.maxHeight * (1 - it)
                            alpha = it
                        },
                        destroyTransition = {
                            translationY = constraints.maxHeight * (1 - it)
                            alpha = it
                        },
                        pauseTransition = fadeScaleDestroyTransition,
                        resumeTransition = fadeScaleCreateTransition,
                    )
                ) { backStackEntry ->
                    val id = backStackEntry.path<Int>("id")
                    NoteEditScene(
                        id = id,
                        onDone = {
                            navigator.goBack()
                        },
                        onBack = {
                            navigator.goBack()
                        }
                    )
                }
            }
        }
    }
}
