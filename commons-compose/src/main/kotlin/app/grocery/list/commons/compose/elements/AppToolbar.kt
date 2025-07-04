package app.grocery.list.commons.compose.elements

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.EmojiProvider
import app.grocery.list.commons.compose.LocalEmojiProvider
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.theme.GroceryListTheme

@Composable
fun AppToolbar(
    title: String,
    modifier: Modifier = Modifier,
    progress: Boolean = false,
    onUpClick: (() -> Unit)? = null,
    onCounterClick: (() -> Unit)? = null,
    counterValue: Int? = null,
) {
    AppToolbarInternal(
        title = title,
        progress = progress,
        onUpClick = onUpClick,
        titleTrailingContent = {
            if (counterValue != null) {
                AppCounter(
                    value = counterValue,
                    modifier = Modifier
                        .then(
                            if (onCounterClick != null) {
                                Modifier
                                    .clickable(
                                        onClick = onCounterClick,
                                        interactionSource = null,
                                        indication = null,
                                    )
                            } else {
                                Modifier
                            }
                        )
                        .padding(8.dp),
                )
            }
        },
        modifier = modifier,
    )
}

@Composable
internal fun AppToolbarInternal(
    title: String,
    progress: Boolean,
    onUpClick: (() -> Unit)?,
    titleTrailingContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    val emojiProvider = LocalEmojiProvider.current
    val elementSize = 48.dp
    val screenHorizontalPadding = dimensionResource(R.dimen.margin_16_32_64)
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.inverseSurface)
            .windowInsetsPadding(
                WindowInsets
                    .systemBars
                    .union(WindowInsets.displayCutout)
                    .only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal),
            )
            .height(84.dp)
            .fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.Center),
        ) {
            OptionalUpIcon(
                size = elementSize,
                screenHorizontalPadding = screenHorizontalPadding,
                onClick = onUpClick,
                modifier = Modifier,
            )
            Text(
                text = decoratedTitle(title, emojiProvider),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .weight(1f),
            )
            Box(
                modifier = Modifier
                    .padding(end = screenHorizontalPadding)
                    .width(elementSize),
                contentAlignment = Alignment.CenterEnd,
            ) {
                titleTrailingContent()
            }
        }
        if (progress) {
            LinearProgressIndicator(
                color = MaterialTheme.colorScheme.inverseOnSurface,
                trackColor = Color.Transparent,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
private fun OptionalUpIcon(
    size: Dp,
    screenHorizontalPadding: Dp,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    val innerPadding = 12.dp
    val shift = innerPadding + 3.dp
    Box(
        modifier = modifier
            .padding(
                start = screenHorizontalPadding - shift,
                end = shift,
            )
            .size(size),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedVisibility(
            visible = onClick != null,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onClick?.invoke() }
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }
}

@Composable
private fun decoratedTitle(title: String, emojiProvider: EmojiProvider) =
    if (title.isNotBlank()) {
        val emoji = rememberSaveable { emojiProvider.obtain() }
        DisposableEffect(Unit) {
            onDispose {
                emojiProvider.release(emoji)
            }
        }
        "$emoji $title"
    } else {
        val list = remember {
            listOf(
                emojiProvider.obtain(),
                emojiProvider.obtain(),
                emojiProvider.obtain(),
            )
        }
        DisposableEffect(Unit) {
            onDispose {
                for (emoji in list) {
                    emojiProvider.release(emoji)
                }
            }
        }
        list.joinToString(separator = " ") { it }
    }

@PreviewLightDark
@Composable
private fun AppToolbarPreview() {
    GroceryListTheme {
        AppToolbar(
            title = "Title",
        )
    }
}

@PreviewLightDark
@Composable
private fun AppToolbarWithCounterPreview() {
    var counterValue by remember { mutableIntStateOf(9) }
    GroceryListTheme {
        AppToolbar(
            title = "Title",
            onUpClick = {},
            counterValue = counterValue,
            modifier = Modifier
                .clickable {
                    counterValue++
                }
        )
    }
}

@PreviewLightDark
@Composable
private fun AppToolbarWithLongTitleAndCounterPreview() {
    var counterValue by remember { mutableIntStateOf(2) }
    GroceryListTheme {
        AppToolbar(
            title = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut vel lacus malesuada, tincidunt ligula ac, bibendum metus. ",
            onUpClick = {},
            progress = true,
            counterValue = counterValue,
            modifier = Modifier
                .clickable {
                    counterValue++
                }
        )
    }
}
