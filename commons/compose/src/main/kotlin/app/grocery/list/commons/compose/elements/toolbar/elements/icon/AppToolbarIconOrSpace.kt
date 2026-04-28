package app.grocery.list.commons.compose.elements.toolbar.elements.icon

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.elements.toolbar.AppToolbarCallbacks
import app.grocery.list.commons.compose.elements.toolbar.AppToolbarCallbacksMock
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppColors

@Composable
internal fun AppToolbarIconOrSpace(
    props: AppToolbarIconProps?,
    callbacks: AppToolbarCallbacks,
    modifier: Modifier = Modifier,
) {
    val size = 56.dp
    val innerPadding = 16.dp
    val offsetFromEdge = dimensionResource(R.dimen.margin_16_32_64) - innerPadding

    if (props != null) {
        Box(
            modifier = modifier
                .padding(
                    start = if (props is AppToolbarIconProps.Leading) {
                        offsetFromEdge
                    } else {
                        innerPadding
                    },
                    end = if (props is AppToolbarIconProps.Trailing) {
                        offsetFromEdge
                    } else {
                        innerPadding
                    },
                )
                .size(size),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(props.iconId),
                contentDescription = stringResource(props.descriptionId),
                colorFilter = ColorFilter.tint(
                    LocalAppColors.current.blackOrWhite,
                ),
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
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
                        },
                    )
                    .fillMaxSize()
                    .padding(innerPadding),
            )
        }
    } else {
        Spacer(
            modifier = modifier
                .size(
                    width = offsetFromEdge + size + innerPadding,
                    height = size,
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
