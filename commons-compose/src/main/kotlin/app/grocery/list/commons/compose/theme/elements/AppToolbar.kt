package app.grocery.list.commons.compose.theme.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.theme.EmojiProvider
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalEmojiProvider

@Composable
fun AppToolbar(
    title: String,
    modifier: Modifier = Modifier,
    counterValue: Int? = null,
) {
    AppToolbarInternal(
        title = title,
        modifier = modifier,
        titleTrailingContent = {
            if (counterValue != null) {
                AppCounter(
                    value = counterValue,
                    modifier = Modifier.padding(
                        horizontal = 6.dp,
                    )
                )
            }
        }
    )
}

@Composable
internal fun AppToolbarInternal(
    title: String,
    modifier: Modifier = Modifier,
    titleTrailingContent: @Composable () -> Unit = {},
) {
    val emojiProvider = LocalEmojiProvider.current
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .windowInsetsPadding(
                WindowInsets
                    .systemBars
                    .union(WindowInsets.displayCutout)
                    .only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal),
            ),
    ) {
        Row(
            modifier = Modifier
                .height(84.dp)
                .padding(horizontal = dimensionResource(R.dimen.margin_16_32_64))
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(
                modifier = Modifier
                    .weight(1f),
            )
            Text(
                text = decoratedTitle(title, emojiProvider),
                modifier = Modifier,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
            )
            Box(
                modifier = Modifier
                    .weight(1f),
            ) {
                titleTrailingContent()
            }
        }
    }
}

@Composable
private fun decoratedTitle(title: String, emojiProvider: EmojiProvider) =
    if (title.isNotBlank()) {
        val emoji = remember { emojiProvider.obtain() }
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
            counterValue = counterValue,
            modifier = Modifier
                .clickable {
                    counterValue++
                }
        )
    }
}
