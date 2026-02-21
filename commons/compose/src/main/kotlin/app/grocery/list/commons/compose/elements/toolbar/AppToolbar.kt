package app.grocery.list.commons.compose.elements.toolbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.SubcomposeMeasureScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import app.grocery.list.commons.compose.LocalToolbarEmojiProvider
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppColors
import app.grocery.list.commons.compose.theme.LocalAppTypography

private val CounterPaddingSp = 4.sp
private val CounterSizeSp = 32.sp

@Composable
fun AppToolbar(
    props: AppToolbarProps,
    callbacks: AppToolbarCallbacks,
    modifier: Modifier = Modifier,
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
                .background(LocalAppColors.current.brand_70_30)
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
            var shouldEmojiBeSkipped by remember { mutableStateOf(false) }
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
                AppToolbarIconOrSpace(
                    props = props.leadingIcon,
                    callbacks = callbacks,
                )
                Spacer(
                    modifier = Modifier
                        .weight(1f),
                )
                Spacer(
                    modifier = Modifier
                        .width(sideItemOffset - emojiWithOffsetWidth),
                )
                if (props.hasEmojiIfEnoughSpace && !(shouldEmojiBeSkipped)) {
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
                    text = stringResource(props.titleId),
                    modifier = Modifier
                        .widthIn(
                            max = titleMaxWidth,
                        ),
                    color = LocalAppColors.current.blackOrWhite,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    onTextLayout = { result ->
                        shouldEmojiBeSkipped = result.lineCount > 1
                    },
                    style = LocalAppTypography.current.toolbarTitle,
                )
                OptionalCounter(
                    value = props.counter,
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
                AppToolbarIconOrSpace(
                    props = props.trailingIcon,
                    callbacks = callbacks,
                )
            }
            if (props.progress) {
                LinearProgressIndicator(
                    color = LocalAppColors.current.brand_00_50,
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
    content: @Composable (emojiWidth: Dp) -> Unit,
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
        color = LocalAppColors.current.blackOrWhite,
        style = LocalAppTypography.current.toolbarTitle,
    )
}


@PreviewLightDark
@Composable
private fun AppToolbarWithCounterPreview(
    @PreviewParameter(
        provider = AppToolbarMocks::class,
    )
    props: AppToolbarProps,
) {
    GroceryListTheme {
        AppToolbar(
            props = props,
            callbacks = AppToolbarCallbacksMock,
            modifier = Modifier,
        )
    }
}
