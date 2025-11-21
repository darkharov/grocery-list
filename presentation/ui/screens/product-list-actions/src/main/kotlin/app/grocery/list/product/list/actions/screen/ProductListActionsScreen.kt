package app.grocery.list.product.list.actions.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import app.grocery.list.commons.compose.elements.AppPreloader
import app.grocery.list.commons.compose.elements.ScrollableContentWithShadows
import app.grocery.list.commons.compose.elements.button.AppButton
import app.grocery.list.commons.compose.elements.button.AppButtonBackgroundProps
import app.grocery.list.commons.compose.elements.button.AppButtonStateProps
import app.grocery.list.commons.compose.theme.AppIcons
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.product.list.actions.ProductListActionsCallbacks
import app.grocery.list.product.list.actions.ProductListActionsCallbacksMock
import app.grocery.list.product.list.actions.ProductListActionsDelegate
import app.grocery.list.product.list.actions.ProductListActionsEventsConsumer
import app.grocery.list.product.list.actions.ProductListActionsMocks
import app.grocery.list.product.list.actions.ProductListActionsNavigation
import app.grocery.list.product.list.actions.ProductListActionsProps
import app.grocery.list.product.list.actions.ProductListActionsViewModel
import app.grocery.list.product.list.actions.R
import app.grocery.list.product.list.actions.dialog.ProductListActionsOptionalDialog
import kotlinx.serialization.Serializable

@Serializable
data object ProductListActions

fun NavGraphBuilder.productListActionsScreen(
    delegate: ProductListActionsDelegate,
    navigation: ProductListActionsNavigation,
    bottomElement: @Composable () -> Unit,
) {
    composable<ProductListActions> {
        ProductListActionsScreen(
            delegate = delegate,
            navigation = navigation,
            bottomElement = bottomElement,
        )
    }
}

@Composable
private fun ProductListActionsScreen(
    delegate: ProductListActionsDelegate,
    navigation: ProductListActionsNavigation,
    bottomElement: @Composable () -> Unit,
) {
    val viewModel = hiltViewModel<ProductListActionsViewModel>()
    val items by viewModel.props.collectAsState()
    val dialog by viewModel.dialog().collectAsStateWithLifecycle()
    ProductListActionsEventsConsumer(
        events = viewModel.events(),
        callbacks = viewModel,
        delegate = delegate,
        navigation = navigation,
    )
    ProductListActionsScreen(
        props = items,
        callbacks = viewModel,
        bottomElement = bottomElement,
    )
    ProductListActionsOptionalDialog(
        dialog = dialog,
        callbacks = viewModel,
    )
}

@Composable
private fun ProductListActionsScreen(
    props: ProductListActionsProps?,
    callbacks: ProductListActionsCallbacks,
    bottomElement: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (props == null) {
        AppPreloader()
    } else {
        Content(
            props = props,
            callbacks = callbacks,
            bottomElement = bottomElement,
            modifier = modifier,
        )
    }
}

@Composable
private fun Content(
    props: ProductListActionsProps,
    callbacks: ProductListActionsCallbacks,
    bottomElement: @Composable () -> Unit,
    modifier: Modifier,
) {
    val scrollState = rememberScrollState()
    ScrollableContentWithShadows(scrollState) {
        Column(
            modifier = modifier
                .padding(horizontal = 12.dp + dimensionResource(R.dimen.margin_16_32_64))
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
        ) {
            Buttons(props, callbacks)
            bottomElement()
            Spacer(
                modifier = Modifier
                    .windowInsetsBottomHeight(WindowInsets.navigationBars),
            )
        }
    }
}

@Composable
private fun Buttons(
    props: ProductListActionsProps,
    callbacks: ProductListActionsCallbacks,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        AppButton(
            title = StringValue.ResId(R.string.clear_list),
            endIcon = rememberVectorPainter(AppIcons.delete),
            background = AppButtonBackgroundProps.Negative,
            state = AppButtonStateProps.enabled(props.clearListAvailable),
            onClick = {
                callbacks.onAttemptToClearList()
            },
        )
        AppButton(
            title = StringValue.ResId(R.string.i_am_at_shop),
            endIcon = rememberVectorPainter(AppIcons.cart),
            background = AppButtonBackgroundProps.Positive,
            state = AppButtonStateProps.enabled(props.shoppingAvailable),
            onClick = {
                callbacks.onAttemptToStartShopping(
                    atLeastOneProductEnabled = props.atLeastOneProductEnabled,
                    numberOfProducts = props.numberOfProducts,
                )
            },
        )
        AppButton(
            title = StringValue.ResId(R.string.share_current_list),
            endIcon = rememberVectorPainter(AppIcons.share),
            state = props.shareButtonState,
            onClick = {
                callbacks.onAttemptToShareCurrentList()
            },
        )
        AppButton(
            title = StringValue.ResId(R.string.paste_copied_list),
            endIcon = rememberVectorPainter(AppIcons.paste),
            onClick = {
                callbacks.onAttemptToPaste()
            },
        )
        AppButton(
            title = StringValue.ResId(
                if (props.listEmpty) {
                    R.string.exit
                } else {
                    R.string.save_and_exit
                }
            ),
            endIcon = rememberVectorPainter(AppIcons.exit),
            onClick = {
                callbacks.onExitFromApp()
            },
        )
    }
}

@PreviewLightDark
@Composable
private fun ProductListActionsScreenPreview(
    @PreviewParameter(
        provider = ProductListActionsMocks::class,
    )
    props: ProductListActionsProps,
) {
    GroceryListTheme {
        Scaffold { padding ->
            ProductListActionsScreen(
                props = props,
                callbacks = ProductListActionsCallbacksMock,
                bottomElement = {},
                modifier = Modifier
                    .padding(padding),
            )
        }
    }
}
