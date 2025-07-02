package app.grocery.list.product.list.preview

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import app.grocery.list.commons.compose.EventConsumer
import app.grocery.list.commons.compose.elements.AppHorizontalDivider
import app.grocery.list.commons.compose.elements.AppHorizontalDividerMode
import app.grocery.list.commons.compose.elements.app.button.AppButton
import app.grocery.list.commons.compose.elements.app.button.AppButtonProps
import app.grocery.list.commons.compose.theme.GroceryListTheme
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
internal fun ProductListPreviewScreen(
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
internal fun ProductListPreviewScreen(
    props: ProductListPreviewProps?,
    callbacks: ProductListPreviewCallbacks,
    modifier: Modifier = Modifier,
) {
    if (props == null) {
        Preloader(
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
            props = props,
            callbacks = callbacks,
        )
    }
}

@Composable
private fun Preloader(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
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
    val listState = rememberLazyListState()
    Box(
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
        if (listState.canScrollBackward) {
            AppHorizontalDivider(
                mode = AppHorizontalDividerMode.Shadow.Downward,
                modifier = Modifier
                    .align(Alignment.TopCenter),
            )
        }
        if (listState.canScrollForward) {
            AppHorizontalDivider(
                mode = AppHorizontalDividerMode.Shadow.Upward,
                modifier = Modifier
                    .align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
private fun Buttons(
    props: ProductListPreviewProps,
    callbacks: ProductListPreviewCallbacks,
) {
    Row(
        modifier = Modifier
            .padding(
                vertical = 16.dp,
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
                state = AppButtonProps.State.enabled(
                    props.categories.isNotEmpty(),
                ),
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
            Category(
                category = category,
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(R.dimen.margin_16_32_64),
                    )
                    .padding(
                        top = 12.dp,
                        bottom = 4.dp,
                    )
                    .animateItem(),
            )
        }
        items(
            items = category.products,
            key = { it.key },
            contentType = { "Product" },
        ) { product ->
            Product(
                product = product,
                callbacks = callbacks,
                modifier = Modifier
                    .animateItem(),
            )
        }
    }
}

@Composable
private fun Category(
    category: ProductListPreviewProps.Category,
    modifier: Modifier = Modifier,
) {
    Text(
        text = category.title,
        modifier = modifier,
        style = MaterialTheme.typography.titleLarge
            .copy(fontWeight = FontWeight.Medium),
    )
}

@Composable
private fun Product(
    product: ProductListPreviewProps.Product,
    callbacks: ProductListPreviewCallbacks,
    modifier: Modifier = Modifier,
) {
    val horizontalPadding = dimensionResource(R.dimen.margin_16_32_64)
    Row(
        modifier = modifier
            .padding(
                vertical = 2.dp,
            ),
        horizontalArrangement = Arrangement
            .spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = product.title,
            modifier = Modifier
                .padding(
                    start = horizontalPadding,
                )
                .weight(1f),
            style = MaterialTheme.typography.titleMedium
                .copy(fontWeight = FontWeight.Normal),
        )
        Icon(
            painter = painterResource(R.drawable.ic_delete),
            contentDescription = stringResource(R.string.delete),
            paddingEnd = horizontalPadding,
            onClick = {
                callbacks.onDelete(productId = product.id)
            },
        )
    }
}

@Composable
private fun Icon(
    painter: Painter,
    contentDescription: String?,
    paddingEnd: Dp,
    onClick: () -> Unit,
) {
    val innerPadding = 9.dp
    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = Modifier
            .padding(end = paddingEnd - innerPadding)
            .clickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = ripple(bounded = false),
                onClick = {
                    onClick()
                }
            )
            .size(36.dp)
            .padding(innerPadding),
        colorFilter = ColorFilter.tint(
            color = MaterialTheme.colorScheme.onSurface,
        ),
    )
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
