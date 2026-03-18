package app.grocery.list.commons.compose.elements.button.icon

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.elements.AppCircularProgressIndicator
import app.grocery.list.commons.compose.elements.button.AppButtonStateMocks
import app.grocery.list.commons.compose.elements.button.AppButtonStateProps
import app.grocery.list.commons.compose.theme.AppIcons
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppColors
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.commons.compose.values.value

private enum class AppIconButtonType(
    val size: Dp,
    val padding: Dp,
) {
    Normal(
        size = 48.dp,
        padding = 12.dp,
    ),
    Compact(
        size = 36.dp,
        padding = 8.dp,
    ),
}

@Composable
fun AppIconButton(
    painter: Painter,
    contentDescription: StringValue,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    state: AppButtonStateProps = AppButtonStateProps.Normal,
) {
    Content(
        modifier = modifier,
        painter = painter,
        state = state,
        contentDescription = contentDescription,
        type = AppIconButtonType.Normal,
        onClick = onClick,
    )
}

@Composable
private fun Content(
    modifier: Modifier,
    painter: Painter,
    state: AppButtonStateProps,
    contentDescription: StringValue,
    type: AppIconButtonType,
    onClick: () -> Unit,
) {
    when (state) {
        AppButtonStateProps.Gone -> {
            // nothing to show
        }
        AppButtonStateProps.Loading -> {
            AppCircularProgressIndicator(
                modifier = modifier
                    .requiredSize(
                        size = type.size,
                    ),
            )
        }
        else -> {
            Image(
                painter = painter,
                contentDescription = contentDescription.value(),
                modifier = modifier
                    .requiredSize(
                        size = type.size,
                    )
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(
                        enabled = state == AppButtonStateProps.Normal,
                        onClick = onClick,
                    )
                    .padding(type.padding),
                colorFilter = ColorFilter
                    .tint(
                        color = LocalAppColors.current.blackOrWhite
                            .copy(
                                alpha = if (state == AppButtonStateProps.Disabled) {
                                    0.33f
                                } else {
                                    1f
                                }
                            ),
                    ),
            )
        }
    }
}

@Composable
fun AppCloseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: StringValue = StringValue.ResId(R.string.close),
    state: AppButtonStateProps = AppButtonStateProps.Normal,
) {
    Content(
        painter = rememberVectorPainter(AppIcons.close),
        state = state,
        contentDescription = contentDescription,
        type = AppIconButtonType.Compact,
        onClick = onClick,
        modifier = modifier,
    )
}

@PreviewLightDark
@Composable
private fun AppIconButtonPreview(
    @PreviewParameter(
        provider = AppButtonStateMocks::class,
    )
    state: AppButtonStateProps,
) {
    GroceryListTheme {
        AppIconButton(
            painter = painterResource(R.drawable.ic_android),
            contentDescription = StringValue.StringWrapper("Content Description"),
            onClick = {},
            state = state,
            modifier = Modifier
                .background(LocalAppColors.current.background)
                .padding(12.dp),
        )
    }
}
