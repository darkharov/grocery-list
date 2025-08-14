package app.grocery.list.product.list.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
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
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import app.grocery.list.commons.compose.EventConsumer
import app.grocery.list.commons.compose.elements.AppPreloader
import app.grocery.list.commons.compose.elements.ScrollableContentWithShadows
import app.grocery.list.commons.compose.elements.button.AppButton
import app.grocery.list.commons.compose.elements.button.AppButtonProps
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppTypography
import app.grocery.list.product.list.preview.ProductListPreviewViewModel.Event
import app.grocery.list.product.list.preview.elements.ProductItem
import kotlinx.serialization.Serializable

@Serializable
data object ProductListPreview

fun NavGraphBuilder.productListPreviewScreen(
    delegate: ProductListPreviewDelegate,
    navigation: ProductListPreviewNavigation,
) {
    composable<ProductListPreview> {
        ProductListPreviewScreen(
            delegate = delegate,
            navigation = navigation,
        )
    }
}

@Composable
private fun ProductListPreviewScreen(
    navigation: ProductListPreviewNavigation,
    delegate: ProductListPreviewDelegate,
) {
    val viewModel = hiltViewModel<ProductListPreviewViewModel>()
    val props by viewModel.props.collectAsState()
    EventConsumer(
        viewModel = viewModel,
        navigation = navigation,
        delegate = delegate,
    )
    ProductListPreviewScreen(
        props = props,
        callbacks = viewModel,
    )
}

@Composable
private fun EventConsumer(
    viewModel: ProductListPreviewViewModel,
    navigation: ProductListPreviewNavigation,
    delegate: ProductListPreviewDelegate,
) {
    EventConsumer(
        events = viewModel.events(),
    ) { event ->
        when (event) {
            is Event.OnAdd -> {
                navigation.goToProductInputForm()
            }
            is Event.OnNext -> {
                navigation.goToActions()
            }
            is Event.OnProductDeleted -> {
                delegate.showUndoProductDeletionSnackbar(event.product)
            }
        }
    }
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
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .weight(1f),
        ) {
            if (props.categories.isEmpty()) {
                NoItemsMessage()
            } else {
                ListWithDividers(
                    props = props,
                    callbacks = callbacks,
                )
            }
        }
        Buttons(
            callbacks = callbacks,
        )
    }
}

@Composable
private fun Buttons(
    callbacks: ProductListPreviewCallbacks,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(
                top = 16.dp,
                bottom = 4.dp,
            )
            .padding(
                horizontal = dimensionResource(R.dimen.margin_16_32_64),
            )
            .windowInsetsPadding(
                WindowInsets
                    .systemBars
                    .union(WindowInsets.displayCutout)
                    .only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal)
            ),
        horizontalArrangement = Arrangement
            .spacedBy(16.dp)
    ) {
        AppButton(
            props = AppButtonProps.Custom(
                text = "+ ${stringResource(R.string.add)}",
            ),
            onClick = {
                callbacks.onAddClick()
            },
            modifier = Modifier
                .weight(1f),
        )
        AppButton(
            props = AppButtonProps.Next(
                titleId = R.string.actions,
            ),
            onClick = {
                callbacks.onNextClick()
            },
            modifier = Modifier
                .weight(1f),
        )
    }
}

@Composable
private fun NoItemsMessage(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
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
    val listState = rememberLazyListState()
    ScrollableContentWithShadows(
        scrollableState = listState,
        modifier = modifier,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = listState,
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
