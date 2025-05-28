package app.grocery.list.commons.compose.theme.elements

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.theme.GroceryListTheme

private const val COUNTER_ANIMATION_DURATION = 250

@Composable
fun AppCounter(
    value: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    AnimatedContent(
        targetState = value,
        transitionSpec = {
            if (value > 0) {
                scaleIn(
                    initialScale = 0f,
                    animationSpec = tween(
                        durationMillis = COUNTER_ANIMATION_DURATION,
                        delayMillis = COUNTER_ANIMATION_DURATION,
                    ),
                )
            } else {
                EnterTransition.None
            }.togetherWith(
                scaleOut(
                    animationSpec = tween(
                        durationMillis = COUNTER_ANIMATION_DURATION,
                    ),
                )
            )
        },
        modifier = modifier,
    ) { targetValue ->
        Box(
            modifier = Modifier
                .drawBehind {
                    if (targetValue > 0) {
                        val delta = (if (targetValue < 10) 4 else 0).dp.toPx()
                        val radius = (size.maxDimension - delta) / 2
                        drawCircle(
                            color = Color.Red,
                            radius = radius,
                        )
                    }
                }
                .size(28.dp)
                .clickable(
                    interactionSource = null,
                    indication = null,
                ) {
                    onClick()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (targetValue > 0) {
                    targetValue.toString()
                } else {
                    ""
                },
                style = MaterialTheme.typography.titleSmall
                    .copy(fontWeight = FontWeight.ExtraBold),
                textAlign = TextAlign.Center,
                color = Color.White,
                modifier = Modifier
                    .then(
                        Modifier.offset(
                            x = (if (targetValue < 10 || targetValue % 10 == 1) 0.0 else -0.5).dp,
                        )
                    ),
            )
        }
    }
}

@Preview
@Composable
private fun AppCounterPreview() {
    var value: Int by remember { mutableIntStateOf(51) }
    GroceryListTheme {
        AppCounter(
            value = value,
            modifier = Modifier
                .padding(4.dp),
            onClick = {
                value++
            },
        )
    }
}

@Preview
@Composable
private fun AppCounterWithoutAnimationOnStartPreview() {
    GroceryListTheme {
        AppCounter(
            value = 2,
            modifier = Modifier
                .padding(4.dp),
        )
    }
}
