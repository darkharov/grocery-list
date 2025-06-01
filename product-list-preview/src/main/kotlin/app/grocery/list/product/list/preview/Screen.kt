package app.grocery.list.product.list.preview

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.elements.AppHorizontalDivider
import app.grocery.list.commons.compose.theme.elements.AppHorizontalDividerMode
import app.grocery.list.commons.compose.theme.elements.WideAppButton

@Composable
internal fun ProductListPreviewScreen(
    viewModel: ProductListPreviewViewModel = hiltViewModel(),
    navigation: ProductListPreviewNavigation,
) {
    val props by viewModel.props.collectAsState()
    LaunchedEffect(viewModel) {
        for (event in viewModel.events()) {
            when (event) {
                ProductListPreviewViewModel.Event.OnGoToActions -> {
                    navigation.onGoToActions()
                }
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
    when {
        (props == null) -> {
            Preloader(modifier)
        }
        props.categories.isEmpty() -> {
            NoItemsMessage(modifier)
        }
        else -> {
            Items(modifier, props, callbacks)
        }
    }
}

@Composable
private fun NoItemsMessage(modifier: Modifier) {
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
private fun Items(
    modifier: Modifier,
    props: ProductListPreviewProps,
    callbacks: ProductListPreviewCallbacks,
) {
    Column(
        modifier = modifier,
    ) {
        ListWithDividers(
            props = props,
            callbacks = callbacks,
            modifier = Modifier
                .weight(1f),
        )
        WideAppButton(
            text = "${stringResource(R.string.next)} >>",
            onClick = {
                callbacks.onNext()
            },
            enabled = props.categories.isNotEmpty(),
            modifier = Modifier
                .padding(vertical = 16.dp),
        )
    }
}

@Composable
private fun Preloader(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ListWithDividers(
    props: ProductListPreviewProps,
    callbacks: ProductListPreviewCallbacks,
    modifier: Modifier,
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
                    .padding(
                        horizontal = dimensionResource(R.dimen.margin_16_32_64),
                        vertical = 2.dp,
                    )
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
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement
            .spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = product.title,
            modifier = Modifier
                .weight(1f),
            style = MaterialTheme.typography.titleMedium
                .copy(fontWeight = FontWeight.Normal),
        )
        Icon(
            painter = painterResource(R.drawable.ic_delete),
            onClick = {
                callbacks.onDeletedClick(productId = product.id)
            },
        )
    }
}

@Composable
private fun Icon(
    painter: Painter,
    onClick: () -> Unit,
    contentDescription: String? = null,
) {
    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = Modifier
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
            .padding(9.dp),
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
        ProductListPreviewScreen(
            props = props,
            callbacks = ProductListPreviewCallbacksMock,
        )
    }
}
