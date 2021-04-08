package ilio.translation.support.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
inline fun RowFillMaxWidth(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    content: @Composable RowScope.() -> Unit
) = Row(modifier.fillMaxWidth(), horizontalArrangement, verticalAlignment, content)

object RowFillMaxWidth {
    @Composable
    inline fun verticalCenter(
        modifier: Modifier = Modifier,
        verticalAlignment: Alignment.Vertical = Alignment.Top,
        content: @Composable RowScope.() -> Unit
    ) = RowFillMaxWidth(modifier, Arrangement.Center, verticalAlignment, content)
}

@Composable
fun SpacerNdp(n: Int) = Spacer(Modifier.padding(n.dp))

@Composable
fun Spacer4dp() = SpacerNdp(4)

@Composable
fun Spacer8dp() = SpacerNdp(8)
