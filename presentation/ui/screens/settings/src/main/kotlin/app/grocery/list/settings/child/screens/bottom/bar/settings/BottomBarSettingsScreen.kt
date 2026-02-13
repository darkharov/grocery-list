package app.grocery.list.settings.child.screens.bottom.bar.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.AbleToGoBack
import app.grocery.list.commons.compose.AbleToGoBackMock
import app.grocery.list.commons.compose.R
import app.grocery.list.settings.child.screens.use.icons.on.bottom.bar.switch_.UseIconsOnBottomBarSwitch
import app.grocery.list.settings.child.screens.use.icons.on.bottom.bar.switch_.UseIconsOnBottomBarSwitchStrategy

@Composable
fun BottomBarSettingsScreen(
    contract: AbleToGoBack,
) {
    UseIconsOnBottomBarSwitch(
        strategy = UseIconsOnBottomBarSwitchStrategy.Screen,
        contract = contract,
        modifier = Modifier
            .padding(
                top = 16.dp,
            )
            .padding(
                horizontal = dimensionResource(R.dimen.margin_16_32_64),
            )
            .fillMaxSize(),
    )
}

@PreviewLightDark
@Composable
private fun BottomBarSettingsScreenPreview() {
    BottomBarSettingsScreen(
        contract = AbleToGoBackMock,
    )
}
