package app.grocery.list.product.list.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import app.grocery.list.commons.compose.EventConsumer
import app.grocery.list.commons.compose.elements.AppPreloader
import app.grocery.list.commons.compose.elements.ScrollableContentWithShadows
import app.grocery.list.commons.compose.elements.button.AppButton
import app.grocery.list.commons.compose.elements.button.AppButtonProps
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppTypography
import app.grocery.list.product.list.preview.elements.ProductItem
import kotlinx.serialization.Serializable

@Serializable
data object ProductListPreview

fun NavGraphBuilder.productListPreviewScreen(
    navigation: ProductListPreviewNavigation,
) {
    composable<ProductListPreview> {
        ProductListPreviewScreen(
            navigation = navigation,
        )
    }
}

@Composable
private fun ProductListPreviewScreen(
    navigation: ProductListPreviewNavigation,
) {
    val viewModel = hiltViewModel<ProductListPreviewViewModel>()
    val props by viewModel.props.collectAsState()
    EventConsumer(
        key = viewModel,
        lifecycleState = Lifecycle.State.RESUMED,
        events = viewModel.events(),
    ) { event ->
        when (event) {
            ProductListPreviewViewModel.Event.OnGoToActions -> {
                navigation.goToActions()
            }
            ProductListPreviewViewModel.Event.OnAddProduct -> {
                navigation.goToProductInputForm()
            }
        }
    }
    ProductListPreviewScreen(
        props = props,
        callbacks = viewModel,
    )
}

@Composable
private fun ProductListPreviewScreen(
    props: ProductListPreviewProps?,
    callbacks: ProductListPreviewCallbacks,
    modifier: Modifier = Modifier,
) {
    if (props == null) {
        AppPreloader(
            modifier = modifier,
        )
    } else {
        Content(
            props = props,
            callbacks = callbacks,
            modifier = modifier,
        )
    }
}

@Composable
private fun Content(
    props: ProductListPreviewProps,
    callbacks: ProductListPreviewCallbacks,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .windowInsetsPadding(WindowInsets.navigationBars),
    ) {
        if (props.categories.isEmpty()) {
            NoItemsMessage(
                modifier = Modifier
                    .weight(1f),
            )
        } else {
            ListWithDividers(
                props = props,
                callbacks = callbacks,
                modifier = Modifier
                    .weight(1f),
            )
        }
        Buttons(
            callbacks = callbacks,
        )
    }
}

@Composable
private fun NoItemsMessage(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.list_is_empty),
        )
    }
}

@Composable
private fun ListWithDividers(
    props: ProductListPreviewProps,
    callbacks: ProductListPreviewCallbacks,
    modifier: Modifier = Modifier,
) {
    val scrollableState = rememberLazyListState()
    ScrollableContentWithShadows(
        scrollableState = scrollableState,
        modifier = modifier,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = scrollableState,
            contentPadding = PaddingValues(
                vertical = 16.dp,
            ),
        ) {
            items(
                props = props,
                callbacks = callbacks,
            )
        }
    }
}

@Composable
private fun Buttons(
    callbacks: ProductListPreviewCallbacks,
) {
    Row(
        modifier = Modifier
            .padding(
                top = 16.dp,
                bottom = 4.dp,
            )
            .padding(
                horizontal = dimensionResource(R.dimen.margin_16_32_64),
            ),
        horizontalArrangement = Arrangement
            .spacedBy(16.dp)
    ) {
        AppButton(
            props = AppButtonProps.Custom(
                text = "+ ${stringResource(R.string.add)}",
            ),
            onClick = {
                callbacks.onAddProduct()
            },
            modifier = Modifier
                .weight(1f),
        )
        AppButton(
            props = AppButtonProps.Next(
                titleId = R.string.actions,
            ),
            onClick = {
                callbacks.onNext()
            },
            modifier = Modifier
                .weight(1f),
        )
    }
}

private fun LazyListScope.items(
    props: ProductListPreviewProps,
    callbacks: ProductListPreviewCallbacks,
) {
    for (category in props.categories) {
        item(
            key = category.key,
            contentType = "Category",
        ) {
            Text(
                text = category.title,
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(R.dimen.margin_16_32_64),
                    )
                    .padding(
                        top = 28.dp,
                        bottom = 6.dp,
                    )
                    .animateItem(),
                style = LocalAppTypography.current.header,
            )
        }
        items(
            items = category.products,
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
}

@Composable
@PreviewLightDark
private fun ProductListPreviewPreview(
    @PreviewParameter(
        provider = ProductListPreviewMocks::class,
    )
    props: ProductListPreviewProps,
) {
    GroceryListTheme {
        Scaffold { padding ->
            ProductListPreviewScreen(
                props = props,
                callbacks = ProductListPreviewCallbacksMock,
                modifier = Modifier
                    .padding(padding),
            )
        }
    }
}
