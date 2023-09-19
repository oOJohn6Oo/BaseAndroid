package io.john6.johnbase.compose.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.ElevationOverlay
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import kotlin.math.abs
import kotlin.math.ln


object JUtil {

    private val VerticalScrollConsumer = object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource) = available.copy(x = 0f)
        override suspend fun onPreFling(available: Velocity) = available.copy(x = 0f)
    }

    private val VerticalParentScrollConsumer = object : NestedScrollConnection {

        override suspend fun onPostFling(consumed: Velocity, available: Velocity) = available

        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource
        ): Offset = available
    }

    private val HorizontalScrollConsumer = object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource) = available.copy(y = 0f)
        override suspend fun onPreFling(available: Velocity) = available.copy(y = 0f)
    }

    /**
     * 禁止自身竖向滚动
     */
    fun Modifier.disabledNestedVerticalScroll(disabled: Boolean = true) =
        if (disabled) this.nestedScroll(VerticalScrollConsumer) else this

    /**
     * 禁止自身横向滚动
     */
    fun Modifier.disabledNestedHorizontalScroll(disabled: Boolean = true) =
        if (disabled) this.nestedScroll(HorizontalScrollConsumer) else this

    /**
     * 禁止父节点竖向滚动
     */
    fun Modifier.disableParentNestedVerticalScroll(disabled: Boolean = true) =
        if (disabled) this.nestedScroll(VerticalParentScrollConsumer) else this

    @ReadOnlyComposable
    @Composable
    fun calculateForegroundColor(backgroundColor: Color, elevation: Dp): Color {
        val alpha = ((4.5f * ln(elevation.value + 1)) + 2f) / 100f
        val baseForegroundColor = contentColorFor(backgroundColor)
        return baseForegroundColor.copy(alpha = alpha)
    }

    /**
     * 禁止竖向滑动
     *
     * FIXME 所有点击事件变得极难触发
     */
    fun Modifier.disableAllVerticalScroll(disable:Boolean = true) =
        pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    awaitPointerEvent(pass = PointerEventPass.Initial).changes.forEach {
                        val offset = it.positionChange()
                        if (disable && abs(offset.y) > 0) {
                            it.consume()
                        }
                    }
                }
            }
        }

}

@Composable
fun jSurfaceColorAtElevation(
    color: Color,
    elevationOverlay: ElevationOverlay?,
    absoluteElevation: Dp
): Color {
    return if (color == MaterialTheme.colors.surface && elevationOverlay != null) {
        elevationOverlay.apply(color, absoluteElevation)
    } else {
        color
    }
}


fun Modifier.topSafeDrawing(): Modifier = composed {
    this.windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top))
}

fun Modifier.centerSafeDrawing(): Modifier = composed {
    this.windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
}

fun Modifier.bottomSafeDrawing(): Modifier = composed {
    this.windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom))
}

object JElevationOverlayInBothLightAndDarkMode : ElevationOverlay {
    @ReadOnlyComposable
    @Composable
    override fun apply(color: Color, elevation: Dp): Color {
        val foregroundColor = JUtil.calculateForegroundColor(color, elevation)
        return foregroundColor.compositeOver(color)
    }
}