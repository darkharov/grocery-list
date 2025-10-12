package app.grocery.list.commons.compose

import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.CacheDrawScope
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object AppGradientDefaults {
    val height = 220.dp
}

@Immutable
enum class AppGradientDirection {
    Upward {

        override fun colors(color: Color): List<Color> =
            listOf(Color.Transparent, color)

        override fun y(cacheDrawScope: CacheDrawScope, strokeWidth: Float): Float =
            cacheDrawScope.size.height - strokeWidth / 2 + 1

        override fun brush(
            cacheDrawScope: CacheDrawScope,
            colors: List<Color>,
            strokeWidthPx: Float,
        ): Brush =
            Brush.verticalGradient(
                colors = colors,
                startY = cacheDrawScope.size.height - strokeWidthPx,
                endY = cacheDrawScope.size.height,
            )
    },
    Downward {

        override fun colors(color: Color): List<Color> =
            listOf(color, Color.Transparent)

        override fun y(cacheDrawScope: CacheDrawScope, strokeWidth: Float): Float =
            strokeWidth / 2 - 1

        override fun brush(
            cacheDrawScope: CacheDrawScope,
            colors: List<Color>,
            strokeWidthPx: Float,
        ): Brush =
            Brush.verticalGradient(
                colors = colors,
                startY = 0f,
                endY = strokeWidthPx,
            )
    },
    ;
    abstract fun colors(color: Color): List<Color>
    abstract fun y(cacheDrawScope: CacheDrawScope, strokeWidth: Float): Float
    abstract fun brush(cacheDrawScope: CacheDrawScope, colors: List<Color>, strokeWidthPx: Float): Brush
}

fun Modifier.drawGradient(
    direction: AppGradientDirection,
    color: Color,
    height: Dp = AppGradientDefaults.height,
    visible: Boolean = true,
) = this
    .drawWithCache {
        if (visible) {
            val colors = direction.colors(color)
            val strokeWidth = height.toPx()
            val gradient = direction.brush(this, colors, strokeWidth)
            val y = direction.y(cacheDrawScope = this, strokeWidth = strokeWidth)
            val start = Offset(0f, y)
            val end = Offset(size.width, y)
            onDrawWithContent {
                drawContent()
                drawLine(
                    brush = gradient,
                    start = start,
                    end = end,
                    strokeWidth = strokeWidth,
                )
            }
        } else {
            onDrawWithContent {
                drawContent()
            }
        }
    }
