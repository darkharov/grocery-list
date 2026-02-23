package app.grocery.list.settings.elements.menu.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.elements.AppHorizontalDivider
import app.grocery.list.commons.compose.theme.AppIcons
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppColors
import app.grocery.list.commons.compose.theme.LocalAppTypography
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.commons.compose.values.value

@Composable
internal fun SettingsMenuItem(
    leadingIcon: Painter,
    text: StringValue,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clickable {
                onClick()
            },
    ) {
        Row(
            modifier = Modifier
                .padding(
                    vertical = 12.dp,
                    horizontal = 24.dp,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = leadingIcon,
                contentDescription = null,
                tint = LocalAppColors.current.brand_40_40,
            )
            Spacer(
                modifier = Modifier
                    .padding(4.dp),
            )
            Text(
                text = text.value(),
                color = LocalAppColors
                    .current
                    .blackOrWhite
                    .copy(alpha = 0.88f),
                textAlign = TextAlign.Start,
                style = LocalAppTypography.current.textButton,
                modifier = Modifier
                    .weight(1f),
            )
            Icon(
                painter = rememberVectorPainter(AppIcons.arrowForward),
                contentDescription = null,
                tint = LocalAppColors.current.blackOrWhite,
                modifier = Modifier
                    .size(16.dp),
            )
        }
        AppHorizontalDivider(
            modifier = Modifier
                .padding(horizontal = 16.dp),
        )
    }
}

@PreviewLightDark
@Composable
private fun SettingsMenuItemPreview() {
    GroceryListTheme {
        Scaffold {
            SettingsMenuItem(
                leadingIcon = rememberVectorPainter(Icons.Default.Android),
                text = StringValue.StringWrapper("Text"),
                onClick = {},
            )
        }
    }
}
