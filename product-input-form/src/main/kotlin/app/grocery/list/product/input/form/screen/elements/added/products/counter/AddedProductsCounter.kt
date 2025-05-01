package app.grocery.list.product.input.form.screen.elements.added.products.counter

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

private const val COUNTER_ANIMATION_DURATION = 250

@Composable
internal fun AddedProductsCounter(
    numberOfAddedProducts: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        AnimatedContent(
            targetState = numberOfAddedProducts,
            transitionSpec = {
                scaleIn(
                    initialScale = 0f,
                    animationSpec = tween(
                        COUNTER_ANIMATION_DURATION,
                        delayMillis = COUNTER_ANIMATION_DURATION,
                    ),
                ).togetherWith(
                    scaleOut(
                        animationSpec = tween(COUNTER_ANIMATION_DURATION),
                    )
                )
            }
        ) { targetValue ->
            Text(
                text = if (targetValue > 0) {
                    targetValue.toString()
                } else {
                    ""
                },
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center,
                color = Color.White,
                modifier = Modifier
                    .padding(12.dp)
                    .drawBehind {
                        if (targetValue > 0) {
                            drawCircle(
                                color = Color.Red,
                                radius = size.width / 2,
                            )
                        }
                    }
                    .padding(
                        if (numberOfAddedProducts < 10) {
                            8.dp
                        } else {
                            4.dp
                        }
                    ),
            )
        }
    }
}
