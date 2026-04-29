package app.grocery.list.product.list.preview.elements.product.item

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.elements.AppDeleteAction
import app.grocery.list.commons.compose.elements.AppSwipeToDismissBox
import app.grocery.list.commons.compose.elements.switch_.AppSwitch
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppColors
import app.grocery.list.product.list.preview.R
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun ProductItem(
    product: ProductItemProps,
    callbacks: ProductItemCallbacks,
    modifier: Modifier = Modifier,
) {
    AppSwipeToDismissBox(
        enableDismissFromStartToEnd = false,
        backgroundContent = { swipeToDismissBoxState ->
            Actions(
                state = swipeToDismissBoxState,
            )
        },
        onDismiss = {
            callbacks.onDelete(
                product = product,
            )
        },
        modifier = modifier
            .combinedClickable(
                onClick = {
                    callbacks.onProductEnabledChange(
                        product = product,
                        newValue = !(product.enabled),
                    )
                },
                onLongClick = {
                    callbacks.onEditProduct(
                        product = product,
                    )
                },
            )
            .fillMaxWidth(),
    ) {
        Content(
            props = product,
            callbacks = callbacks,
        )
    }
}

@Composable
private fun Actions(
    state: SwipeToDismissBoxState,
    modifier: Modifier = Modifier,
) {
    when (state.dismissDirection) {
        SwipeToDismissBoxValue.StartToEnd -> {
            // nothing to show
        }
        SwipeToDismissBoxValue.EndToStart -> {
            AppDeleteAction(modifier)
        }
        SwipeToDismissBoxValue.Settled -> {
            // nothing to show
        }
    }
}

@Composable
private fun Content(
    props: ProductItemProps,
    callbacks: ProductItemCallbacks,
) {
    val checked = props.enabled
    Row(
        modifier = Modifier
            .background(LocalAppColors.current.background)
            .padding(
                horizontal = dimensionResource(R.dimen.margin_16_32_64),
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = props.title,
            modifier = Modifier
                .alpha(0.81f)
                .weight(1f),
            color = LocalAppColors.current.blackOrWhite,
        )
        Spacer(
            modifier = Modifier
                .padding(8.dp),
        )
        AppSwitch(
            checked = checked,
            onCheckedChange = { newValue ->
                callbacks.onProductEnabledChange(
                    product = props,
                    newValue = newValue,
                )
            },
        )
    }
}

internal fun LazyListScope.products(
    items: ImmutableList<ProductItemProps>,
    callbacks: ProductItemCallbacks,
) {
    items(
        items = items,
        key = { it.key },
        contentType = { "Product" },
    ) { product ->
        ProductItem(
            product = product,
            callbacks = callbacks,
            modifier = Modifier
                .animateItem(),
        )
    }
}

@Composable
@PreviewLightDark
private fun ProductItemPreview(
    @PreviewParameter(
        provider = ProductItemMocks::class,
    )
    props: ProductItemProps,
) {
    GroceryListTheme {
        ProductItem(
            product = props,
            callbacks = ProductItemCallbacksMock,
            modifier = Modifier,
        )
    }
}
