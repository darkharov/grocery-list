package app.grocery.list.product.list.actions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import app.grocery.list.commons.compose.elements.AppDialogScreen
import app.grocery.list.commons.compose.elements.app.button.AppButtonProps
import app.grocery.list.commons.compose.elements.app.button.WideAppButton
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.values.StringValue
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
    val dialog = viewModel.dialog().collectAsState().value
    ProductListActionsScreen(
        dialog = dialog,
        callbacks = viewModel,
    )
}

@Composable
private fun ProductListActionsScreen(
    dialog: ProductListActionsDialog?,
    callbacks: ProductListActionsCallbacks,
) {
    Content(callbacks)
    if (dialog != null) {
        Dialog(dialog, callbacks)
    }
}

@Composable
private fun Content(callbacks: ProductListActionsCallbacks) {
    Column(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        val gap = 8.dp
        WideAppButton(
            props = AppButtonProps.Custom(
                text = stringResource(R.string.clear_list),
                background = AppButtonProps.Background.Negative,
                drawableEndId = R.drawable.ic_delete,
            ),
            onClick = {
                callbacks.onGoToClearListConfirmation()
            },
        )
        Spacer(
            modifier = Modifier
                .height(gap),
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
        Spacer(
            modifier = Modifier
                .height(gap),
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
            text = stringResource(R.string.start_shopping_explanation),
            modifier = Modifier
                .padding(
                    horizontal = dimensionResource(R.dimen.margin_16_32_64),
                ),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Composable
private fun Dialog(
    dialog: ProductListActionsDialog,
    callbacks: ProductListActionsCallbacks,
) {
    when (dialog) {
        ProductListActionsDialog.ConfirmClearList -> {
            AppDialogScreen(
                icon = painterResource(R.drawable.ic_delete),
                text = StringValue.ResId(
                    R.string.clear_product_list_confirmation,
                ),
                confirmButtonText = StringValue.ResId(
                    R.string.delete,
                ),
                onDismiss = {
                    callbacks.onClearListDenied()
                },
                onConfirm = {
                    callbacks.onClearListConfirmed()
                }
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun ProductListActionsScreenWithDialogPreview(
    @PreviewParameter(
        provider = ProductListActionsDialogMocks::class,
    )
    dialog: ProductListActionsDialog?,
) {
    GroceryListTheme {
        ProductListActionsScreen(
            callbacks = ProductListActionsCallbacksMock,
            dialog = dialog,
        )
    }
}
