package ilio.translation.support.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.mouse.mouseScrollFilter

@Composable
fun Modifier.scrollCallback(block: () -> Unit): Modifier {
    return this.mouseScrollFilter { _, _ ->
        block()
        false
    }
}

