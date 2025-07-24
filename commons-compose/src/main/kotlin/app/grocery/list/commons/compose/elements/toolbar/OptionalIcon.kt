package app.grocery.list.commons.compose.elements.toolbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.theme.GroceryListTheme

internal val IconSize = 48.dp
internal val IconInnerPadding = 12.dp

@Composable
internal fun OptionalIcon(
    props: OptionalIconProps,
    onClick: (() -> Unit),
    modifier: Modifier = Modifier,
) {
    val screenHorizontalPadding = dimensionResource(R.dimen.margin_16_32_64)
    val innerPadding = IconInnerPadding
    val shifted = innerPadding + 3.dp
    Box(
        modifier = modifier
            .padding(
                start = if (props.type == OptionalIconProps.Type.Leading) {
                    screenHorizontalPadding - shifted
                } else {
                    shifted
                },
                end = if (props.type == OptionalIconProps.Type.Trailing) {
                    screenHorizontalPadding - shifted
                } else {
                    shifted
                },
            )
            .size(IconSize),
        contentAlignment = Alignment.Center,
    ) {
        val content = props.content
        if (content != null) {
            Image(
                painter = painterResource(content.iconId),
                contentDescription = stringResource(
                    content.descriptionId ?: R.string._empty
                ),
                colorFilter = ColorFilter.tint(
                    MaterialTheme.colorScheme.onSurface,
                ),
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        onClick()
                    }
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun OptionalIconPreview() {
    GroceryListTheme {
        Surface {
            OptionalIcon(
                props = OptionalIconProps(
                    type = OptionalIconProps.Type.Trailing,
                    content = OptionalIconProps.Content(
                        iconId = R.drawable.ic_back,
                        descriptionId = null,
                    ),
                ),
                onClick = {},
                modifier = Modifier,
            )
        }
    }
}
