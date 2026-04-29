package app.grocery.list.commons.compose.elements.toolbar

import android.content.res.Resources
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.elements.toolbar.elements.counter.AppCounterOrSpacer
import app.grocery.list.commons.compose.elements.toolbar.elements.emoji.AppEmojiOrSpacer
import app.grocery.list.commons.compose.elements.toolbar.elements.icon.AppToolbarIconOrSpace
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppColors
import app.grocery.list.commons.compose.theme.LocalAppTypography
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.commons.compose.values.value
import kotlin.random.Random

private val CounterPaddingSp = 4.sp
private val ItemSizeSp = 28.sp

@Composable
fun AppToolbar(
    props: AppToolbarProps,
    callbacks: AppToolbarCallbacks,
    modifier: Modifier = Modifier,
) {
    val decorationItemSize = with(LocalDensity.current) { ItemSizeSp.toDp() }
    val counterPadding = with(LocalDensity.current) { CounterPaddingSp.toDp() }
    val resources = LocalResources.current
    val emoji = rememberSaveable { resources.randomToolbarEmoji() }
    Box(
        modifier = modifier
            .background(LocalAppColors.current.brand_70_30)
            .windowInsetsPadding(
                WindowInsets
                    .systemBars
                    .union(WindowInsets.displayCutout)
                    .only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal),
            )
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            AppToolbarIconOrSpace(
                props = props.icons.leading,
                callbacks = callbacks,
            )
            Spacer(
                modifier = Modifier
                    .weight(1f),
            )
            if (props.noDecorations) {
                Title(
                    title = props.title,
                )
            } else {
                DecoratedTitle(
                    props = props,
                    emoji = emoji,
                    counterPadding = counterPadding,
                    decorationItemSize = decorationItemSize,
                )
            }
            Spacer(
                modifier = Modifier
                    .weight(1f),
            )
            AppToolbarIconOrSpace(
                props = props.icons.trailing,
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

@Composable
private fun DecoratedTitle(
    props: AppToolbarProps,
    emoji: String,
    counterPadding: Dp,
    decorationItemSize: Dp,
) {
    AppEmojiOrSpacer(
        emoji = if (props.mightHaveEmoji) {
            emoji
        } else {
            null
        },
        modifier = Modifier
            .padding(start = counterPadding * 2) // to take the same space as counter
            .width(decorationItemSize),
    )
    Title(
        title = props.title,
        modifier = Modifier
            .animateContentSize(),
    )
    AppCounterOrSpacer(
        value = props.counter,
        modifier = Modifier
            .padding(counterPadding)
            .size(decorationItemSize),
    )
}

private fun Resources.randomToolbarEmoji(): String {
    val emojis = getStringArray(R.array.toolbar_emojis)
    val index = Random.nextInt(emojis.size)
    return emojis[index]
}

@Composable
private fun Title(
    title: StringValue,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title.value(),
        modifier = modifier,
        color = LocalAppColors.current.blackOrWhite,
        textAlign = TextAlign.Center,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
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
