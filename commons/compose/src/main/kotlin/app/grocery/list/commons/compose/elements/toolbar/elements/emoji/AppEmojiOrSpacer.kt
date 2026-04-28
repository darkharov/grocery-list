package app.grocery.list.commons.compose.elements.toolbar.elements.emoji

import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppColors
import app.grocery.list.commons.compose.theme.LocalAppTypography

@Composable
internal fun AppEmojiOrSpacer(
    emoji: String?,
    modifier: Modifier = Modifier,
) {
    if (emoji != null) {
        Text(
            text = emoji,
            textAlign = TextAlign.Center,
            color = LocalAppColors.current.blackOrWhite,
            style = LocalAppTypography.current.toolbarTitle,
            modifier = modifier,
        )
    } else {
        Spacer(
            modifier = modifier,
        )
    }
}

@Preview
@Composable
private fun AppEmojiOrSpacerPreview() {
    GroceryListTheme {
        AppEmojiOrSpacer(
            emoji = "🥭",
        )
    }
}
