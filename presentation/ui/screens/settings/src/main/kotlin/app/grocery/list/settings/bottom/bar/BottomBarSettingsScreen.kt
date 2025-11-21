package app.grocery.list.settings.bottom.bar

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import app.grocery.list.commons.compose.AbleToGoBack
import app.grocery.list.commons.compose.R
import app.grocery.list.settings.use.icons.on.bottom.bar.switch_.UseIconsOnBottomBarSwitch
import app.grocery.list.settings.use.icons.on.bottom.bar.switch_.UseIconsOnBottomBarSwitchStrategy
import kotlinx.serialization.Serializable

@Serializable
data object BottomBarSettings

internal fun NavGraphBuilder.bottomBarSettings(
    ableToGoBack: AbleToGoBack,
) {
    composable<BottomBarSettings> {
        UseIconsOnBottomBarSwitch(
            strategy = UseIconsOnBottomBarSwitchStrategy.Screen,
            navigation = ableToGoBack,
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
}
