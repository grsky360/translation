package ilio.translation.support.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import java.awt.Point

operator fun Point.minus(point: Point): IntOffset = IntOffset(x - point.x, y - point.y)

operator fun Point.plus(offset: IntOffset): Point = Point(x + offset.x, y + offset.y)

operator fun Point.plus(offset: Offset): Point = Point(x + offset.x.toInt(), y + offset.y.toInt())

operator fun IntOffset.plus(point: Point) = point + this
