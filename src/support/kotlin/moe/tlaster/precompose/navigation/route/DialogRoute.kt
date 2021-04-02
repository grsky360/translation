package moe.tlaster.precompose.navigation.route

import androidx.compose.runtime.Composable
import moe.tlaster.precompose.navigation.BackStackEntry

internal class DialogRoute(
    route: String,
    content: @Composable (BackStackEntry) -> Unit
) : ComposeRoute(route, content)
