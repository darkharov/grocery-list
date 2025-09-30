package app.grocery.list.product.list.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import app.grocery.list.commons.compose.EventConsumer
import app.grocery.list.commons.compose.elements.AppPreloader
import app.grocery.list.commons.compose.elements.ScrollableContentWithShadows
import app.grocery.list.commons.compose.elements.button.text.AppTextButton
import app.grocery.list.commons.compose.elements.button.text.AppTextButtonProps
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppTypography
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.product.list.preview.ProductListPreviewViewModel.Event
import app.grocery.list.product.list.preview.elements.ProductItem
import kotlinx.serialization.Serializable

@Serializable
data object ProductListPreview

fun NavGraphBuilder.productListPreviewScreen(
    delegate: ProductListPreviewDelegate,
    navigation: ProductListPreviewNavigation,
    bottomBar: @Composable () -> Unit,
) {
    composable<ProductListPreview> {
        ProductListPreviewScreen(
            delegate = delegate,
            bottomBar = bottomBar,
            navigation = navigation,
        )
    }
}

@Composable
private fun ProductListPreviewScreen(
    navigation: ProductListPreviewNavigation,
    delegate: ProductListPreviewDelegate,
    bottomBar: @Composable () -> Unit,
) {
    val viewModel = hiltViewModel<ProductListPreviewViewModel>()
    val props by viewModel.props.collectAsStateWithLifecycle()
    EventConsumer(
        viewModel = viewModel,
        navigation = navigation,
        delegate = delegate,
    )
    ProductListPreviewScreen(
        props = props,
        bottomBar = bottomBar,
        callbacks = viewModel,
    )
}

@Composable
private fun EventConsumer(
    viewModel: ProductListPreviewViewModel,
    navigation: ProductListPreviewNavigation,
    delegate: ProductListPreviewDelegate,
) {
    EventConsumer(viewModel.events()) { event ->
        when (event) {
            is Event.OnProductDeleted -> {
                delegate.showUndoProductDeletionSnackbar(event.product)
            }
            is Event.OnEditProduct -> {
                navigation.goToProductEditingForm(
                    productId = event.productId,
                )
            }
        }
    }
}

@Composable
private fun ProductListPreviewScreen(
    props: ProductListPreviewProps?,
    callbacks: ProductListPreviewCallbacks,
    bottomBar: @Composable () -> Unit,
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
            bottomBar = bottomBar,
            modifier = modifier,
        )
    }
}

@Composable
private fun Content(
    props: ProductListPreviewProps,
    callbacks: ProductListPreviewCallbacks,
    bottomBar: @Composable () -> Unit,
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
        bottomBar()
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
                top = 8.dp,
                bottom = 16.dp,
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
    val enableAndDisableAll = props.enableAndDisableAll
    if (enableAndDisableAll != null) {
        enableAndDisableAll(
            enableAndDisableAll = enableAndDisableAll,
            callbacks = callbacks,
        )
    }
    for ((index, category) in props.categories.withIndex()) {
        if (index > 0) {
            // I am afraid of dynamic paddings in lazy columns
            item(
                key = category.topOffsetKey,
                contentType = { "Category top offset" },
            ) {
                Spacer(
                    modifier = Modifier
                        .height(26.dp),
                )
            }
        }
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
                        top = 6.dp,
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

private fun LazyListScope.enableAndDisableAll(
    enableAndDisableAll: ProductListPreviewProps.EnableAndDisableAllState,
    callbacks: ProductListPreviewCallbacks,
) {
    item {
        val buttonHorizontalPadding = 8.dp
        val desiredHorizontalOffset = dimensionResource(R.dimen.margin_16_32_64)
        val finalHorizontalOffset = desiredHorizontalOffset - buttonHorizontalPadding
        Row(
            modifier = Modifier
                .fillParentMaxWidth()
                .padding(
                    horizontal = finalHorizontalOffset,
                ),
            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
        ) {
            AppTextButton(
                props = AppTextButtonProps.TextOnly(
                    text = StringValue.ResId(R.string.disable_all),
                    enabled = enableAndDisableAll.disableAllAvailable,
                    padding = PaddingValues(
                        horizontal = buttonHorizontalPadding,
                        vertical = 12.dp,
                    ),
                ),
                onClick = {
                    callbacks.onDisableEnableAll()
                },
                modifier = Modifier
                    .animateItem(),
            )
            AppTextButton(
                props = AppTextButtonProps.TextOnly(
                    text = StringValue.ResId(R.string.enable_all),
                    enabled = enableAndDisableAll.enableAllAvailable
                ),
                onClick = {
                    callbacks.onEnableAll()
                },
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
                bottomBar = {},
            )
        }
    }
}
