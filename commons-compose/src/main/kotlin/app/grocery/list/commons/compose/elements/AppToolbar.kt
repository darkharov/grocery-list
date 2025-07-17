package app.grocery.list.commons.compose.elements

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.SubcomposeMeasureScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import app.grocery.list.commons.compose.LocalToolbarEmojiProvider
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppTypography

private val IconInnerPadding = 12.dp
private val IconSize = 48.dp

private val CounterPaddingSp = 4.sp
private val CounterSizeSp = 32.sp

@Composable
fun AppToolbar(
    modifier: Modifier = Modifier,
    numberOfAddedProducts: Int = 0,
    onUpClick: (() -> Unit)? = null,
    onSettingsClick: (() -> Unit)? = null,
    progress: Boolean = false,
) {
    AppToolbarInternal(
        title = stringResource(R.string.grocery_list),
        modifier = modifier,
        onUpClick = onUpClick,
        counter = numberOfAddedProducts,
        trailingIcon = painterResource(R.drawable.ic_settings),
        onTrailingIconClick = onSettingsClick,
        progress = progress,
    )
}

@Composable
internal fun AppToolbarInternal(
    title: String,
    modifier: Modifier = Modifier,
    onUpClick: (() -> Unit)? = null,
    counter: Int = 0,
    trailingIcon: Painter? = null,
    trailingIconContentDescription: String? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    progress: Boolean = false,
) {
    val screenHorizontalPadding = dimensionResource(R.dimen.margin_16_32_64)
    val counterSize = with(LocalDensity.current) { CounterSizeSp.toDp() }
    val counterPadding = with(LocalDensity.current) { CounterPaddingSp.toDp() }
    val emojiProvider = LocalToolbarEmojiProvider.current
    val emoji = rememberSaveable { emojiProvider.get(1) }

    MeasureContentParts(
        emoji = emoji,
    ) { emojiWidth ->
        BoxWithConstraints(
            modifier = modifier
                .background(MaterialTheme.colorScheme.inverseSurface)
                .windowInsetsPadding(
                    WindowInsets
                        .systemBars
                        .union(WindowInsets.displayCutout)
                        .only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal),
                )
                .padding(vertical = 4.dp)
                .defaultMinSize(minHeight = 84.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            var withEmoji by remember { mutableStateOf(true) }
            val iconWidth = IconSize + screenHorizontalPadding
            val counterWithOffsetsWidth = counterSize + 2 * counterPadding
            val emojiTitleOffset = 4.dp
            val emojiWithOffsetWidth = emojiWidth + emojiTitleOffset
            val sideItemOffset = max(counterWithOffsetsWidth, emojiWithOffsetWidth)
            val titleMaxWidth = minWidth - 2 * iconWidth - 2 * sideItemOffset
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                OptionalIcon(
                    side = IconSide.Start,
                    screenHorizontalPadding = screenHorizontalPadding,
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = null,
                    onClick = onUpClick,
                )
                Spacer(
                    modifier = Modifier
                        .weight(1f),
                )
                Spacer(
                    modifier = Modifier
                        .width(sideItemOffset - emojiWithOffsetWidth),
                )
                if (withEmoji) {
                    Emoji(
                        emoji = emoji,
                    )
                } else {
                    Spacer(
                        modifier = Modifier
                            .width(emojiWidth),
                    )
                }
                Spacer(
                    modifier = Modifier
                        .width(emojiTitleOffset),
                )
                Text(
                    text = title,
                    modifier = Modifier
                        .widthIn(
                            max = titleMaxWidth,
                        ),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    onTextLayout = { result ->
                        if (result.lineCount > 1) {
                            withEmoji = false
                        }
                    },
                    style = LocalAppTypography.current.toolbarTitle,
                )
                AppCounter(
                    value = counter,
                    modifier = Modifier
                        .padding(counterPadding)
                        .size(counterSize)
                )
                Spacer(
                    modifier = Modifier
                        .width(sideItemOffset - counterWithOffsetsWidth),
                )
                Spacer(
                    modifier = Modifier
                        .weight(1f),
                )
                OptionalIcon(
                    side = IconSide.End,
                    screenHorizontalPadding = screenHorizontalPadding,
                    painter = trailingIcon,
                    onClick = onTrailingIconClick,
                    contentDescription = trailingIconContentDescription,
                )
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
}

@Composable
private fun MeasureContentParts(
    emoji: String,
    content: @Composable (
        emojiWidth: Dp,
    ) -> Unit,
) {
    val emojiSlot = 1
    val contentSlot = 2
    SubcomposeLayout { constraints ->

        val emojiWidth = measureWidth(
            slotId = emojiSlot,
            content = { Emoji(emoji = emoji) },
            constraints = constraints,
        )

        val contentPlaceable = subcompose(
            slotId = contentSlot,
            content = { content(emojiWidth) },
        ).first()
            .measure(constraints)
        layout(contentPlaceable.width, contentPlaceable.height) {
            contentPlaceable.place(x = 0, y = 0)
        }
    }
}

private fun SubcomposeMeasureScope.measureWidth(
    slotId: Any?,
    content: @Composable () -> Unit,
    constraints: Constraints,
) =
    subcompose(slotId = slotId, content = content)
        .first()
        .measure(constraints)
        .width
        .toDp()

@Composable
private fun Emoji(
    emoji: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = emoji,
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurface,
        style = LocalAppTypography.current.toolbarTitle,
    )
}

enum class IconSide {
    Start,
    End,
}

@Composable
private fun OptionalIcon(
    side: IconSide,
    screenHorizontalPadding: Dp,
    onClick: (() -> Unit)?,
    painter: Painter?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    val innerPadding = IconInnerPadding
    val shifted = innerPadding + 3.dp
    Box(
        modifier = modifier
            .padding(
                start = if (side == IconSide.Start) {
                    screenHorizontalPadding - shifted
                } else {
                    shifted
                },
                end = if (side == IconSide.End) {
                    screenHorizontalPadding - shifted
                } else {
                    shifted
                },
            )
            .size(IconSize),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedVisibility(
            visible = onClick != null && painter != null,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Image(
                painter = painter ?: ColorPainter(Color.Transparent),
                contentDescription = contentDescription,
                colorFilter = ColorFilter.tint(
                    MaterialTheme.colorScheme.onSurface,
                ),
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onClick?.invoke() }
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }
}


@PreviewLightDark
@Composable
private fun AppToolbarWithCounterPreview() {
    var counterValue by remember { mutableIntStateOf(9) }
    GroceryListTheme {
        AppToolbar(
            modifier = Modifier
                .clickable {
                    counterValue++
                },
            onUpClick = {},
            numberOfAddedProducts = counterValue,
            onSettingsClick = {},
        )
    }
}
