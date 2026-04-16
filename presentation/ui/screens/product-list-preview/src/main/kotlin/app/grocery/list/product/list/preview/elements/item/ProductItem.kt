package app.grocery.list.product.list.preview.elements.item

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.elements.AppDeleteAction
import app.grocery.list.commons.compose.elements.AppSwipeToDismissBox
import app.grocery.list.commons.compose.elements.switch_.AppSwitch
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppColors
import app.grocery.list.product.list.preview.ProductListPreviewProps
import app.grocery.list.product.list.preview.R

@Composable
internal fun ProductItem(
    product: ProductListPreviewProps.Items.Product,
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
                productId = product.id,
            )
        },
        modifier = modifier
            .combinedClickable(
                onClick = {
                    callbacks.onProductEnabledChange(
                        productId = product.id,
                        newValue = !(product.enabled),
                    )
                },
                onLongClick = {
                    callbacks.onEditProduct(
                        productId = product.id,
                    )
                },
            )
            .fillMaxWidth(),
    ) {
        Content(
            product = product,
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
    product: ProductListPreviewProps.Items.Product,
    callbacks: ProductItemCallbacks,
) {
    val checked = product.enabled
    Row(
        modifier = Modifier
            .background(LocalAppColors.current.background)
            .padding(
                horizontal = dimensionResource(R.dimen.margin_16_32_64),
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = product.title,
            modifier = Modifier
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
                    productId = product.id,
                    newValue = newValue,
                )
            },
        )
    }
}

@Composable
@Preview
private fun ProductItemPreview() {
    GroceryListTheme {
        ProductItem(
            product = ProductListPreviewProps.Items.Product(
                id = 1,
                title = AnnotatedString("🍅 Tomato"),
                enabled = true,
            ),
            callbacks = ProductItemCallbacksMock,
            modifier = Modifier,
        )
    }
}
