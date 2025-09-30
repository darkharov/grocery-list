package app.grocery.list.product.list.actions.bar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentPaste
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.elements.button.AppButtonStateProps
import app.grocery.list.commons.compose.elements.button.icon.AppIconButton
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.product.list.actions.ProductListActionsCallbacks
import app.grocery.list.product.list.actions.ProductListActionsCallbacksMock
import app.grocery.list.product.list.actions.ProductListActionsMocks
import app.grocery.list.product.list.actions.ProductListActionsProps
import app.grocery.list.product.list.actions.R

@Composable
internal fun ProductListActionsIconBar(
    props: ProductListActionsProps,
    callbacks: ProductListActionsCallbacks,
    modifier: Modifier = Modifier,
) {
    ProductListActionsBarContainer(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .padding(
                horizontal = dimensionResource(R.dimen.margin_16_32_64) - 16.dp,
            ),
    ) {
        AppIconButton(
            painter = painterResource(R.drawable.ic_add_2),
            contentDescription = StringValue.ResId(R.string.add_product_to_buy),
            onClick = {
                callbacks.onAdd()
            },
        )
        AppIconButton(
            painter = rememberVectorPainter(Icons.Outlined.Delete),
            contentDescription = StringValue.ResId(R.string.clear_list),
            onClick = {
                callbacks.onAttemptToClearList()
            },
            state = AppButtonStateProps.enabled(props.clearListAvailable),
        )
        AppIconButton(
            painter = rememberVectorPainter(Icons.Outlined.ShoppingCart),
            contentDescription = StringValue.ResId(R.string.i_am_at_shop),
            onClick = {
                callbacks.onAttemptToStartShopping(
                    atLeastOneProductEnabled = props.atLeastOneProductEnabled,
                    numberOfProducts = props.numberOfProducts,
                )
            },
            state = AppButtonStateProps.enabled(props.shoppingAvailable),
        )
        AppIconButton(
            painter = rememberVectorPainter(Icons.Outlined.Share),
            contentDescription = StringValue.ResId(R.string.share_current_list),
            onClick = {
                callbacks.onAttemptToShareCurrentList()
            },
            state = props.shareButtonState,
        )
        AppIconButton(
            painter = rememberVectorPainter(Icons.Outlined.ContentPaste),
            contentDescription = StringValue.ResId(R.string.paste_copied_list),
            onClick = {
                callbacks.onAttemptToPaste()
            },
        )
    }
}

@PreviewLightDark
@Composable
private fun ProductListActionsIconBarPreview(
    @PreviewParameter(
        provider = ProductListActionsMocks::class,
    )
    props: ProductListActionsProps,
) {
    GroceryListTheme {
        ProductListActionsIconBar(
            props = props,
            callbacks = ProductListActionsCallbacksMock,
            modifier = Modifier,
        )
    }
}
