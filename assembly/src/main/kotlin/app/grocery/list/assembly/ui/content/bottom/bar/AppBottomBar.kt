package app.grocery.list.assembly.ui.content.bottom.bar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.elements.button.AppButton
import app.grocery.list.commons.compose.elements.button.AppButtonProps
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.product.list.preview.R

@Composable
internal fun AppBottomBar(
    navigation: AppBottomBarNavigation,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(
                top = 16.dp,
                bottom = 4.dp,
            )
            .padding(
                horizontal = dimensionResource(R.dimen.margin_16_32_64),
            )
            .windowInsetsPadding(
                WindowInsets
                    .systemBars
                    .union(WindowInsets.displayCutout)
                    .only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal)
            ),
        horizontalArrangement = Arrangement
            .spacedBy(16.dp)
    ) {
        AppButton(
            props = AppButtonProps.Custom(
                text = "+ ${stringResource(R.string.add)}",
            ),
            onClick = {
                navigation.goToProductInputForm()
            },
            modifier = Modifier
                .weight(1f),
        )
        AppButton(
            props = AppButtonProps.Next(
                titleId = R.string.actions,
            ),
            onClick = {
                navigation.goToActions()
            },
            modifier = Modifier
                .weight(1f),
        )
    }
}

@PreviewLightDark
@Composable
private fun AppBottomBarPreview() {
    GroceryListTheme {
        Scaffold { padding ->
            AppBottomBar(
                navigation = AppBottomBarNavigationMock,
                modifier = Modifier
                    .padding(padding),
            )
        }
    }
}
