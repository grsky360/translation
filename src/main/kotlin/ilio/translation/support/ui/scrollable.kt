package ilio.translation.support.ui

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.LayoutDirection
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

open class ListenableScrollState(initial: Int, val listener: ListenableScrollState.() -> Unit = {}): ScrollableState {
    var value: Int by mutableStateOf(initial, structuralEqualityPolicy())
        private set

    var maxValue: Int
        get() = _maxValueState.value
        internal set(newMax) {
            _maxValueState.value = newMax
            if (value > newMax) {
                value = newMax
            }
        }

    val interactionSource: InteractionSource get() = internalInteractionSource

    internal val internalInteractionSource: MutableInteractionSource = MutableInteractionSource()

    private var _maxValueState = mutableStateOf(Int.MAX_VALUE, structuralEqualityPolicy())

    private var accumulator: Float = 0f

    private val scrollableState = ScrollableState {
        val absolute = (value + it + accumulator)
        val newValue = absolute.coerceIn(0f, maxValue.toFloat())
        val changed = absolute != newValue
        val consumed = newValue - value
        val consumedInt = consumed.roundToInt()
        value += consumedInt
        accumulator = consumed - consumedInt

        // Avoid floating-point rounding error
        if (changed) consumed else it
    }

    override suspend fun scroll(scrollPriority: MutatePriority, block: suspend ScrollScope.() -> Unit): Unit {
        scrollableState.scroll(scrollPriority) {
            listener()
            block()
        }
    }

    override fun dispatchRawDelta(delta: Float): Float = scrollableState.dispatchRawDelta(delta)


    override val isScrollInProgress: Boolean
        get() = scrollableState.isScrollInProgress

    suspend fun animateScrollTo(value: Int, animationSpec: AnimationSpec<Float> = SpringSpec()) {
        this.animateScrollBy((value - this.value).toFloat(), animationSpec)
    }

    suspend fun scrollTo(value: Int): Float = this.scrollBy((value - this.value).toFloat())
}

fun Modifier.listenableScroll(
    state: ListenableScrollState,
    reverseScrolling: Boolean = false,
    flingBehavior: FlingBehavior? = null,
    isScrollable: Boolean = true,
    isVertical: Boolean = true
) = composed(
    factory = {
        val coroutineScope = rememberCoroutineScope()
        val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
        // Add RTL to the mix: if horizontal and RTL, reverse reverseScrolling
        val resolvedReverseScrolling =
            if (!isVertical && isRtl) !reverseScrolling else reverseScrolling
        val semantics = Modifier.semantics {
            if (isScrollable) {
                val accessibilityScrollState = ScrollAxisRange(
                    value = { state.value.toFloat() },
                    maxValue = { state.maxValue.toFloat() },
                    reverseScrolling = resolvedReverseScrolling
                )
                if (isVertical) {
                    this.verticalScrollAxisRange = accessibilityScrollState
                } else {
                    this.horizontalScrollAxisRange = accessibilityScrollState
                }
                // when b/156389287 is fixed, this should be proper scrollTo with reverse handling
                scrollBy(
                    action = { x: Float, y: Float ->
                        coroutineScope.launch {
                            if (isVertical) {
                                (state as ScrollableState).scrollBy(y)
                            } else {
                                (state as ScrollableState).scrollBy(x)
                            }
                        }
                        return@scrollBy true
                    }
                )
            }
        }
        val scrolling = Modifier.scrollable(
            orientation = if (isVertical) Orientation.Vertical else Orientation.Horizontal,
            // reverse scroll to have a "natural" gesture that goes reversed to layout
            reverseDirection = !resolvedReverseScrolling,
            enabled = isScrollable,
//            interactionSource = state.origin.internalInteractionSource,
            interactionSource = MutableInteractionSource(),
            flingBehavior = flingBehavior,
            state = state
        )
        val layout = ScrollingLayoutModifier(state, reverseScrolling, isVertical)
        semantics.then(scrolling).clipToBounds().then(layout)
    },
    inspectorInfo = debugInspectorInfo {
        name = "scroll"
        properties["state"] = state
        properties["reverseScrolling"] = reverseScrolling
        properties["flingBehavior"] = flingBehavior
        properties["isScrollable"] = isScrollable
        properties["isVertical"] = isVertical
    }
)

data class ScrollingLayoutModifier(
    val scrollerState: ListenableScrollState,
    val isReversed: Boolean,
    val isVertical: Boolean
) : LayoutModifier {
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        constraints.assertNotNestingScrollableContainers(isVertical)
        val childConstraints = constraints.copy(
            maxHeight = if (isVertical) Constraints.Infinity else constraints.maxHeight,
            maxWidth = if (isVertical) constraints.maxWidth else Constraints.Infinity
        )
        val placeable = measurable.measure(childConstraints)
        val width = placeable.width.coerceAtMost(constraints.maxWidth)
        val height = placeable.height.coerceAtMost(constraints.maxHeight)
        val scrollHeight = placeable.height - height
        val scrollWidth = placeable.width - width
        val side = if (isVertical) scrollHeight else scrollWidth
        return layout(width, height) {
//            scrollerState.origin.maxValue = side
            val scroll = scrollerState.value.coerceIn(0, side)
            val absScroll = if (isReversed) scroll - side else -scroll
            val xOffset = if (isVertical) 0 else absScroll
            val yOffset = if (isVertical) absScroll else 0
            placeable.placeRelativeWithLayer(xOffset, yOffset)
        }
    }
}

fun Constraints.assertNotNestingScrollableContainers(isVertical: Boolean) {
    if (isVertical) {
        check(maxHeight != Constraints.Infinity) {
            "Nesting scrollable in the same direction layouts like ScrollableContainer and " +
                    "LazyColumn is not allowed. If you want to add a header before the list of" +
                    " items please take a look on LazyColumn component which has a DSL api which" +
                    " allows to first add a header via item() function and then the list of " +
                    "items via items()."
        }
    } else {
        check(maxWidth != Constraints.Infinity) {
            "Nesting scrollable in the same direction layouts like ScrollableRow and " +
                    "LazyRow is not allowed. If you want to add a header before the list of " +
                    "items please take a look on LazyRow component which has a DSL api which " +
                    "allows to first add a fixed element via item() function and then the " +
                    "list of items via items()."
        }
    }
}

class ScrollableScrollbarAdapter(
    private val scrollState: ListenableScrollState
) : ScrollbarAdapter {
    override val scrollOffset: Float get() = scrollState.value.toFloat()

    override suspend fun scrollTo(containerSize: Int, scrollOffset: Float) {
        scrollState.scrollTo(scrollOffset.roundToInt())
    }

    override fun maxScrollOffset(containerSize: Int) =
        scrollState.maxValue.toFloat()
}
