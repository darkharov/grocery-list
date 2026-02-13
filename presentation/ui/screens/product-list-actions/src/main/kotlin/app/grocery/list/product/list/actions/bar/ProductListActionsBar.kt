package app.grocery.list.product.list.actions.bar

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.product.list.actions.ProductListActionsCallbacks
import app.grocery.list.product.list.actions.ProductListActionsCallbacksMock
import app.grocery.list.product.list.actions.ProductListActionsContract
import app.grocery.list.product.list.actions.ProductListActionsEventsConsumer
import app.grocery.list.product.list.actions.ProductListActionsMocks
import app.grocery.list.product.list.actions.ProductListActionsProps
import app.grocery.list.product.list.actions.ProductListActionsViewModel
import app.grocery.list.product.list.actions.dialog.ProductListActionsOptionalDialog

@Composable
fun ProductListActionsBar(
    contract: ProductListActionsContract,
) {
    val viewModel = hiltViewModel<ProductListActionsViewModel>()
    val events = viewModel.events()
    val props by viewModel.props.collectAsStateWithLifecycle()
    val dialog by viewModel.dialog().collectAsStateWithLifecycle()
    ProductListActionsEventsConsumer(
        events = events,
        callbacks = viewModel,
        contract = contract,
    )
    ProductListActionsOptionalBar(
        props = props,
        callbacks = viewModel,
    )
    ProductListActionsOptionalDialog(
        dialog = dialog,
        callbacks = viewModel,
    )
}

@Composable
private fun ProductListActionsOptionalBar(
    props: ProductListActionsProps?,
    callbacks: ProductListActionsCallbacks,
) {
    if (props != null) {
        ProductListActionsBar(
            props = props,
            callbacks = callbacks,
        )
    }
}

@Composable
internal fun ProductListActionsBar(
    props: ProductListActionsProps,
    callbacks: ProductListActionsCallbacks,
    modifier: Modifier = Modifier,
) {
    if (props.useIconsOnBottomBar) {
        ProductListActionsIconBar(
            props = props,
            callbacks = callbacks,
            modifier = modifier,
        )
    } else {
        ProductListActionsButtonBar(
            callbacks = callbacks,
            modifier = modifier,
        )
    }
}

@Composable
@PreviewLightDark
private fun ProductListActionsBarPreview(
    @PreviewParameter(
        provider = ProductListActionsMocks::class,
    )
    props: ProductListActionsProps,
) {
    GroceryListTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            ProductListActionsBar(
                props = props,
                callbacks = ProductListActionsCallbacksMock,
                modifier = Modifier,
            )
        }
    }
}
