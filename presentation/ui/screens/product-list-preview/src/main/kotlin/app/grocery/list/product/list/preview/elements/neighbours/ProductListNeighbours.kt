package app.grocery.list.product.list.preview.elements.neighbours

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import app.grocery.list.commons.compose.elements.start.and.end.button.panel.AppStartAndEndButtonPanel
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.values.StringValue

@Composable
internal fun ProductListNeighbours(
    props: ProductListNeighboursProps,
    callbacks: ProductListNeighboursCallbacks,
    modifier: Modifier = Modifier,
) {
    val (_, trailing, leading) = props
    AppStartAndEndButtonPanel(
        startButtonText = StringValue
            .nullableToString(trailing) {
                "<< $title ${counter.formattedValue}"
            },
        onStartClick = {
            if (trailing != null) {
                callbacks.onNeighbourProductListClick(trailing)
            }
        },
        endButtonText = StringValue
            .nullableToString(leading) {
                "$title ${counter.formattedValue} >>"
            },
        onEndClick = {
            if (leading != null) {
                callbacks.onNeighbourProductListClick(leading)
            }
        },
        modifier = modifier,
    )
}

@Composable
@PreviewLightDark
private fun ProductListNeighboursPreview(
    @PreviewParameter(
        provider = ProductListNeighboursMocks::class,
    )
    props: ProductListNeighboursProps,
) {
    GroceryListTheme {
        ProductListNeighbours(
            props = props,
            callbacks = ProductListNeighboursCallbacksMock,
            modifier = Modifier,
        )
    }
}
