package app.grocery.list.product.list.actions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.elements.app.button.AppButtonProps
import app.grocery.list.commons.compose.elements.app.button.WideAppButton
import app.grocery.list.product.list.actions.ProductListActionsViewModel.Event
import kotlinx.serialization.Serializable

@Serializable
data object ProductListActions

fun NavGraphBuilder.productListActionsScreen(
    navigation: ProductListActionsNavigation,
) {
    composable<ProductListActions> {
        ProductListActionsScreen(
            navigation = navigation,
        )
    }
}

@Composable
internal fun ProductListActionsScreen(
    viewModel: ProductListActionsViewModel = hiltViewModel(),
    navigation: ProductListActionsNavigation,
) {
    LaunchedEffect(viewModel) {
        for (event in viewModel.events()) {
            when (event) {
                Event.OnListCleared -> {
                    navigation.onListCleared()
                }
                Event.OnExitFromApp -> {
                    navigation.onExitFromApp()
                }
                Event.OnStartShopping -> {
                    navigation.onStartShopping()
                }
            }
        }
    }
    ProductListActionsScreen(
        callbacks = viewModel,
    )
}

@Composable
private fun ProductListActionsScreen(
    callbacks: ProductListActionsCallbacks,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        WideAppButton(
            props = AppButtonProps.Custom(
                text = stringResource(R.string.clear_list),
                background = AppButtonProps.Background.Negative,
                drawableEndId = R.drawable.ic_delete,
            ),
            onClick = {
                callbacks.onClearList()
            },
        )
        WideAppButton(
            props = AppButtonProps.Custom(
                text = stringResource(R.string.exit_with_saving),
                drawableEndId = R.drawable.ic_exit,
            ),
            onClick = {
                callbacks.onExitFromApp()
            },
        )
        WideAppButton(
            props = AppButtonProps.Custom(
                text = stringResource(R.string.start_shopping),
                background = AppButtonProps.Background.Positive,
                drawableEndId = R.drawable.ic_cart,
            ),
            onClick = {
                callbacks.onStartShopping()
            },
        )
        Text(
            text = stringResource(R.string.explanation),
            modifier = Modifier
                .padding(
                    horizontal = dimensionResource(R.dimen.margin_16_32_64),
                ),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Preview
@Composable
private fun ProductListActionsScreenPreview() {
    GroceryListTheme {
        ProductListActionsScreen(
            callbacks = ProductListActionsCallbacksMock,
        )
    }
}
