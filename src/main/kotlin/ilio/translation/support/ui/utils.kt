package ilio.translation.support.ui

import androidx.compose.desktop.DesktopTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class RowBuilder(
    val modifier: Modifier = Modifier,
    val horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    val verticalAlignment: Alignment.Vertical = Alignment.Top)

object Row {
    @Composable
    fun fillMaxWidth(
        modifier: Modifier = Modifier,
        horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
        verticalAlignment: Alignment.Vertical = Alignment.Top,
        content: (@Composable RowScope.() -> Unit)? = null
    ): RowBuilder {
        val row = RowBuilder(modifier.fillMaxWidth(), horizontalArrangement, verticalAlignment)
        if (content != null) {
            Row(row.modifier, row.horizontalArrangement, row.verticalAlignment, content)
        }
        return row
    }
    @Composable
    fun verticalCenter(
        modifier: Modifier = Modifier,
        verticalAlignment: Alignment.Vertical = Alignment.Top,
        content: (@Composable RowScope.() -> Unit)? = null
    ): RowBuilder {
        return fillMaxWidth(modifier, Arrangement.Center, verticalAlignment, content)
    }
}

@Composable
fun Root(scrollable: Boolean = false, content: @Composable ColumnScope.() -> Unit) = MaterialTheme {
    DesktopTheme {
//    val context = LocalAppWindow.current
        val modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(8.dp)
        if (scrollable) {
            Block.scrollable(modifier, content)
        } else {
            Block(modifier, content)
        }
    }
}

@Composable
fun Block(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = modifier.fillMaxWidth().padding(8.dp)) {
        content()
    }
}

object Block {
    @Composable
    fun scrollable(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
        val verticalScrollState = rememberScrollState(0)
        Box(Modifier.fillMaxSize()) {
            Block(modifier = modifier.verticalScroll(verticalScrollState).verticalScrollbar(verticalScrollState)) {
                content()
            }
        }
    }
}

@Composable
fun Divider() = androidx.compose.material.Divider(modifier = Modifier.padding(4.dp))

@Composable
fun Spacer(n: Int) = Spacer(Modifier.height(n.dp))

@Composable
fun Spacer4() = Spacer(4)

@Composable
fun Spacer8() = Spacer(8)
