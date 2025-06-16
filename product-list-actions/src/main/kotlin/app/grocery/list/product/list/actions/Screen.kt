package app.grocery.list.product.list.actions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import app.grocery.list.commons.compose.theme.values.StringValue
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
                navigation.onReturnToInitialScreen()
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
                text = stringResource(R.string.i_am_at_shop),
                background = AppButtonProps.Background.Positive,
                drawableEndId = R.drawable.ic_cart,
            ),
            onClick = {
                callbacks.onStartShopping()
            },
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
