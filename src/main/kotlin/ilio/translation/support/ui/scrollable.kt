package ilio.translation.support.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.ScrollbarStyleAmbient
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.mouse.mouseScrollFilter
import androidx.compose.ui.zIndex
import ilio.translation.utils.RefreshableScheduler

@Composable
fun Modifier.verticalScrollbar(scrollState: ScrollState): Modifier {
    val style = ScrollbarStyleAmbient.current
    var scrolling by remember { mutableStateOf(false) }
    val task = remember {
        RefreshableScheduler().newTask(1000) {
            scrolling = false
        }
    }

    Box(modifier = Modifier.fillMaxSize().zIndex(Float.MAX_VALUE)) {
        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(scrollState),
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            style = style.copy(
                unhoverColor = if (scrolling) style.unhoverColor else Color.Transparent
            )
        )
    }

    return this.mouseScrollFilter { _, _ ->
        scrolling = true
        task.refresh()
        false
    }
}

