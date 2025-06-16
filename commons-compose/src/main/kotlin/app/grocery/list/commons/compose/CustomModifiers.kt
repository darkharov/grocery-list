package app.grocery.list.commons.compose

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

fun Modifier.drawUpwardGradient(color: Color): Modifier =
    this
        .drawWithCache {
            val strokeWith = 220.dp.toPx()
            val gradient = Brush.verticalGradient(
                listOf(Color.Transparent, color),
                startY = size.height - strokeWith,
                endY = size.height,
            )
            val y = size.height - strokeWith / 2 + 1
            val start = Offset(0f, y)
            val end = Offset(size.width, y)
            onDrawWithContent {
                drawContent()
                drawLine(
                    brush = gradient,
                    start = start,
                    end = end,
                    strokeWidth = strokeWith,
                )
            }
        }
