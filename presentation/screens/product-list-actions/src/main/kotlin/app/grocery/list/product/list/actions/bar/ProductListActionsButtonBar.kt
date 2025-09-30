package app.grocery.list.product.list.actions.bar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.elements.button.AppButtonAdd
import app.grocery.list.commons.compose.elements.button.AppButtonNext
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.product.list.actions.ProductListActionsCallbacks
import app.grocery.list.product.list.actions.ProductListActionsCallbacksMock
import app.grocery.list.product.list.actions.R

@Composable
internal fun ProductListActionsButtonBar(
    callbacks: ProductListActionsCallbacks,
    modifier: Modifier = Modifier,
) {
    ProductListActionsBarContainer(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .padding(
                horizontal = dimensionResource(R.dimen.margin_16_32_64),
            ),
    ) {
        AppButtonAdd(
            onClick = {
                callbacks.onAdd()
            },
            modifier = Modifier
                .weight(1f),
        )
        AppButtonNext(
            titleId = R.string.actions,
            onClick = {
                callbacks.onGoToActions()
            },
            modifier = Modifier
                .weight(1f),
        )
    }
}

@Preview
@Composable
private fun ProductListActionsButtonBarPreview() {
    GroceryListTheme {
        Surface {
            ProductListActionsButtonBar(
                callbacks = ProductListActionsCallbacksMock,
                modifier = Modifier,
            )
        }
    }
}
