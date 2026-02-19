package app.grocery.list.commons.compose.elements.toolbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
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
internal fun AppToolbarIconOrSpace(
    props: AppToolbarIconProps?,
    callbacks: AppToolbarCallbacks,
    modifier: Modifier = Modifier,
) {
    val screenHorizontalPadding = dimensionResource(R.dimen.margin_16_32_64)
    val shift = 3.dp
    val shiftedPadding = IconInnerPadding + shift
    val offsetFromEdge = screenHorizontalPadding - shiftedPadding

    if (props != null) {
        Box(
            modifier = modifier
                .padding(
                    start = if (props is AppToolbarIconProps.Leading) {
                        offsetFromEdge
                    } else {
                        shiftedPadding
                    },
                    end = if (props is AppToolbarIconProps.Trailing) {
                        offsetFromEdge
                    } else {
                        shiftedPadding
                    },
                )
                .size(IconSize),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(props.iconId),
                contentDescription = stringResource(props.descriptionId),
                colorFilter = ColorFilter.tint(
                    MaterialTheme.colorScheme.onSurface,
                ),
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        when (props) {
                            is AppToolbarIconProps.Up -> {
                                callbacks.onUpIconClick()
                            }
                            is AppToolbarIconProps.AllLists -> {
                                callbacks.onAllListsClick()
                            }
                            is AppToolbarIconProps.Settings -> {
                                callbacks.onSettingsClick()
                            }
                        }
                    }
                    .fillMaxSize()
                    .padding(IconInnerPadding),
            )
        }
    } else {
        Spacer(
            modifier = modifier
                .size(
                    width = offsetFromEdge + IconSize + shiftedPadding,
                    height = IconSize,
                ),
        )
    }
}

@PreviewLightDark
@Composable
private fun AppToolbarIconOrSpacePreview() {
    GroceryListTheme {
        Surface {
            AppToolbarIconOrSpace(
                props = AppToolbarIconProps.Up,
                callbacks = AppToolbarCallbacksMock,
                modifier = Modifier,
            )
        }
    }
}
