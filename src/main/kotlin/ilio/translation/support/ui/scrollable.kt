package ilio.translation.support.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.mouse.mouseScrollFilter

@Composable
fun scrollCallback(modifier: Modifier, block: () -> Unit): Modifier {
    return modifier.mouseScrollFilter { _, _ ->
        block()
        false
    }
}

