package app.grocery.list.custom.lists.picker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.grocery.list.commons.compose.elements.AppBottomBarContainer
import app.grocery.list.commons.compose.elements.AppPreloader
import app.grocery.list.commons.compose.elements.ScrollableContentWithShadows
import app.grocery.list.commons.compose.elements.button.AppButtonAdd
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppColors
import app.grocery.list.custom.lists.R
import app.grocery.list.custom.lists.picker.item.ProductListPickerItem

@Composable
fun ProductListPickerScreen() {
    val viewModel = hiltViewModel<ProductListPickerViewModel>()
    val props by viewModel.props.collectAsStateWithLifecycle()
    ProductListPickerScreen(
        props = props,
        callbacks = viewModel,
    )
}

@Composable
internal fun ProductListPickerScreen(
    props: ProductListPickerProps?,
    callbacks: ProductListPickerCallbacks,
    modifier: Modifier = Modifier,
) {
    if (props != null) {
        Content(
            props = props,
            callbacks = callbacks,
            modifier = modifier,
        )
    } else {
        AppPreloader(
            modifier = modifier,
        )
    }
}

@Composable
private fun Content(
    props: ProductListPickerProps,
    callbacks: ProductListPickerCallbacks,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .weight(1f),
        ) {
            Items(
                props = props,
                callbacks = callbacks,
            )
        }
        Buttons()
    }
}

@Composable
private fun Buttons() {
    AppBottomBarContainer {
        AppButtonAdd(
            onClick = {},
            modifier = Modifier
                .widthIn(max = 420.dp)
                .padding(
                    horizontal = dimensionResource(R.dimen.margin_16_32_64),
                ),
        )
    }
}

@Composable
private fun Items(
    props: ProductListPickerProps,
    callbacks: ProductListPickerCallbacks,
) {
    val state = rememberLazyListState()
    ScrollableContentWithShadows(state) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = state,
            contentPadding = PaddingValues(
                top = dimensionResource(R.dimen.margin_0_16_48),
                bottom = dimensionResource(R.dimen.margin_16_32_64)
            )
        ) {
            items(
                items = props.items,
                key = { it.id },
                contentType = { "Item" },
                itemContent = { item ->
                    ProductListPickerItem(
                        props = item,
                        callbacks = callbacks,
                        modifier = Modifier
                            .animateItem(),
                    )
                },
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun ProductListPickerScreenPreview(
    @PreviewParameter(
        provider = ProductListPickerMocks::class,
    )
    props: ProductListPickerProps,
) {
    GroceryListTheme {
        ProductListPickerScreen(
            props = props,
            callbacks = ProductListPickerCallbacksMock,
            modifier = Modifier
                .background(LocalAppColors.current.background)
        )
    }
}
