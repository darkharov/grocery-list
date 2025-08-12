package app.grocery.list.product.list.preview.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.elements.AppSwipeToDismissBox
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.product.list.preview.ProductListPreviewProps
import app.grocery.list.product.list.preview.R

@Composable
internal fun ProductItem(
    product: ProductListPreviewProps.Product,
    callbacks: ProductItemCallbacks,
    modifier: Modifier = Modifier,
) {
    val horizontalPadding = dimensionResource(R.dimen.margin_16_32_64)
    AppSwipeToDismissBox(
        key = product.key,
        enableDismissFromStartToEnd = false,
        backgroundContent = { swipeToDismissBoxState ->
            Actions(
                state = swipeToDismissBoxState,
                horizontalPadding = horizontalPadding,
            )
        },
        onSwipeFromEndToStartFinished = {
            callbacks.onDelete(
                product = product,
            )
        },
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Content(
            product = product,
            horizontalPadding = horizontalPadding,
            callbacks = callbacks,
        )
    }
}

@Composable
private fun Actions(
    state: SwipeToDismissBoxState,
    horizontalPadding: Dp,
) {
    when (state.dismissDirection) {
        SwipeToDismissBoxValue.StartToEnd -> {
            // nothing to show
        }
        SwipeToDismissBoxValue.EndToStart -> {
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_bin_outline),
                    contentDescription = stringResource(R.string.delete),
                    tint = Color.White,
                    modifier = Modifier
                        .padding(end = horizontalPadding),
                )
            }
        }
        SwipeToDismissBoxValue.Settled -> {
            // nothing to show
        }
    }
}

@Composable
private fun Content(
    product: ProductListPreviewProps.Product,
    horizontalPadding: Dp,
    callbacks: ProductItemCallbacks,
) {
    val checked = product.enabled
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .clickable {
                callbacks.onProductEnabledChange(
                    productId = product.id,
                    newValue = !(checked),
                )
            }
            .padding(
                vertical = 6.dp,
                horizontal = horizontalPadding,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = product.title,
            modifier = Modifier
                .weight(1f),
        )
        Spacer(
            modifier = Modifier
                .padding(8.dp),
        )
        Switch(
            checked = checked,
            onCheckedChange = null,
        )
    }
}

@Composable
@PreviewLightDark
private fun ProductItemPreview() {
    GroceryListTheme {
        Scaffold { padding ->
            ProductItem(
                product = ProductListPreviewProps.Product(
                    id = 1,
                    title = AnnotatedString("🍅 Tomato"),
                    enabled = true,
                ),
                callbacks = ProductItemCallbacksMock,
                modifier = Modifier
                    .padding(padding),
            )
        }
    }
}
