package app.grocery.list.commons.compose.elements

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.unit.sp
import app.grocery.list.commons.compose.theme.GroceryListTheme

private const val COUNTER_ANIMATION_DURATION = 250

@Composable
internal fun AppCounter(
    value: Int?,
    modifier: Modifier = Modifier,
) {
    if (value != null) {
        AnimatedContent(
            targetState = value,
            transitionSpec = {
                if (targetState > 0) {
                    scaleIn(
                        initialScale = 0f,
                        animationSpec = tween(
                            durationMillis = COUNTER_ANIMATION_DURATION,
                            delayMillis = if (initialState == 0) {
                                0
                            } else {
                                COUNTER_ANIMATION_DURATION
                            },
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
            if (targetValue != 0) {
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
                        .wrapContentHeight()
                        .aspectRatio(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (targetValue > 0) {
                            targetValue.toString()
                        } else {
                            ""
                        },
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier,
                    )
                }
            } else {
                Spacer(
                    modifier = modifier,
                )
            }
        }
    } else {
        Spacer(
            modifier = modifier,
        )
    }
}

@Preview
@Composable
private fun AppCounterPreview() {
    var value: Int by remember { mutableIntStateOf(13) }
    GroceryListTheme {
        Box(
            modifier = Modifier
                .clickable {
                    value++
                },
        ) {
            AppCounter(
                value = value,
                modifier = Modifier
                    .padding(4.dp)
                    .size(40.dp),
            )
        }
    }
}

@Preview
@Composable
private fun AppCounterWithoutAnimationOnStartPreview() {
    GroceryListTheme {
        AppCounter(
            value = 2,
            modifier = Modifier
                .padding(4.dp)
                .size(32.dp),
        )
    }
}
