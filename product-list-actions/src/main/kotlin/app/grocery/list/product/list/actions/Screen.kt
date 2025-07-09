package app.grocery.list.product.list.actions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import app.grocery.list.commons.compose.EventConsumer
import app.grocery.list.commons.compose.elements.AppDialogScreen
import app.grocery.list.commons.compose.elements.app.button.AppButtonProps
import app.grocery.list.commons.compose.elements.app.button.WideAppButton
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.product.list.actions.ProductListActionsViewModel.Event
import kotlinx.serialization.Serializable

@Serializable
data object ProductListActions

fun NavGraphBuilder.productListActionsScreen(
    navigation: ProductListActionsNavigation,
    delegate: ProductListActionsDelegate,
) {
    composable<ProductListActions> {
        ProductListActionsScreen(
            delegate = delegate,
            navigation = navigation,
        )
    }
}

@Composable
internal fun ProductListActionsScreen(
    viewModel: ProductListActionsViewModel = hiltViewModel(),
    navigation: ProductListActionsNavigation,
    delegate: ProductListActionsDelegate,
) {
    EventConsumer(
        key = viewModel,
        lifecycleState = Lifecycle.State.RESUMED,
        events = viewModel.events(),
    ) { event ->
        when (event) {
            Event.OnListCleared -> {
                navigation.returnToStartScreen()
            }
            Event.OnExitFromApp -> {
                delegate.onExitFromApp()
            }
            Event.OnStartShopping -> {
                delegate.onStartShopping()
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
    modifier: Modifier = Modifier,
) {
    Content(callbacks, modifier)
    if (dialog != null) {
        Dialog(dialog, callbacks)
    }
}

@Composable
private fun Content(callbacks: ProductListActionsCallbacks, modifier: Modifier) {
    Column(
        modifier = modifier
            .padding(12.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Spacer(
            modifier = Modifier
                .weight(1f),
        )
        WideAppButton(
            props = AppButtonProps.Custom(
                text = stringResource(R.string.clear_list),
                background = AppButtonProps.Background.Negative,
                drawableEndId = R.drawable.ic_bin_outline,
            ),
            onClick = {
                callbacks.onGoToClearListConfirmation()
            },
        )
        WideAppButton(
            props = AppButtonProps.Custom(
                text = stringResource(R.string.i_am_at_shop),
                background = AppButtonProps.Background.Positive,
                drawableEndId = R.drawable.ic_cart,
            ),
            onClick = {
                callbacks.onStartShopping()
            },
        )
        WideAppButton(
            props = AppButtonProps.Custom(
                text = stringResource(R.string.save_and_exit),
                drawableEndId = R.drawable.ic_exit,
            ),
            onClick = {
                callbacks.onExitFromApp()
            },
        )
        Spacer(
            modifier = Modifier
                .weight(1f),
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
                icon = painterResource(R.drawable.ic_bin_outline),
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

@Composable
@PreviewLightDark
private fun ProductListActionsScreenWithDialogPreview(
    @PreviewParameter(
        provider = ProductListActionsDialogMocks::class,
    )
    dialog: ProductListActionsDialog?,
) {
    GroceryListTheme {
        Scaffold { padding ->
            ProductListActionsScreen(
                callbacks = ProductListActionsCallbacksMock,
                dialog = dialog,
                modifier = Modifier
                    .padding(padding),
            )
        }
    }
}
