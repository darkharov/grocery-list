package app.grocery.list.product.list.preview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
import app.grocery.list.commons.compose.elements.dialog.list.ConfirmPastedListDialog
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppTypography
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.product.list.preview.ProductListPreviewViewModel.Event
import app.grocery.list.product.list.preview.elements.ProductItem
import kotlinx.serialization.Serializable

@Serializable
data object ProductListPreview

fun NavGraphBuilder.productListPreview(
    delegate: ProductListPreviewDelegate,
    navigation: ProductListPreviewNavigation,
    bottomBar: @Composable () -> Unit,
) {
    composable<ProductListPreview> {
        PreloaderOrContent(
            delegate = delegate,
            bottomBar = bottomBar,
            navigation = navigation,
        )
    }
}

@Composable
private fun PreloaderOrContent(
    navigation: ProductListPreviewNavigation,
    delegate: ProductListPreviewDelegate,
    bottomBar: @Composable () -> Unit,
) {
    val viewModel = hiltViewModel<ProductListPreviewViewModel>()
    val props by viewModel.props.collectAsStateWithLifecycle()
    val dialog by viewModel.dialog().collectAsStateWithLifecycle()
    EventConsumer(
        viewModel = viewModel,
        navigation = navigation,
        delegate = delegate,
    )
    PreloaderOrContent(
        props = props,
        bottomBar = bottomBar,
        callbacks = viewModel,
    )
    OptionalDialog(
        props = dialog,
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
private fun PreloaderOrContent(
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
            when (props) {
                is ProductListPreviewProps.Empty -> {
                    ListEmptyAndTemplates(
                        props = props,
                        callbacks = callbacks,
                    )
                }
                is ProductListPreviewProps.Items -> {
                    ListWithDividers(
                        props = props,
                        callbacks = callbacks,
                    )
                }
            }
        }
        bottomBar()
    }
}

@Composable
private fun ListEmptyAndTemplates(
    props: ProductListPreviewProps.Empty,
    callbacks: ProductListPreviewCallbacks,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(
                horizontal = 16.dp + dimensionResource(R.dimen.margin_16_32_64),
            )
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column {
            val startPadding = 8.dp
            Text(
                text = stringResource(R.string.list_is_empty),
                style = LocalAppTypography.current.label,
                modifier = Modifier
                    .padding(start = startPadding),
            )
            Spacer(
                modifier = Modifier
                    .height(16.dp),
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                for (template in props.templates) {
                    Text(
                        text = "+ ${template.title}",
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                callbacks.onTemplateClick(template)
                            }
                            .padding(vertical = 6.dp)
                            .padding(
                                end = 12.dp,
                                start = startPadding,
                            ),
                    )
                }
            }
        }
    }
}

@Composable
private fun ListWithDividers(
    props: ProductListPreviewProps.Items,
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
    props: ProductListPreviewProps.Items,
    callbacks: ProductListPreviewCallbacks,
) {
    val enableAndDisableAll = props.enableAndDisableAll
    if (enableAndDisableAll != null) {
        enableAndDisableAll(
            enableAndDisableAll = enableAndDisableAll,
            callbacks = callbacks,
        )
    }
    for ((index, item) in props.items.withIndex()) {
        val category = item.category
        if (category != null) {
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
        }
        items(
            items = item.products,
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
    enableAndDisableAll: ProductListPreviewProps.Items.EnableAndDisableAll,
    callbacks: ProductListPreviewCallbacks,
) {
    item(
        key = enableAndDisableAll.key,
    ) {
        val buttonHorizontalPadding = 8.dp
        val buttonPaddingValues = PaddingValues(
            horizontal = buttonHorizontalPadding,
            vertical = 12.dp,
        )
        val desiredHorizontalOffset = dimensionResource(R.dimen.margin_16_32_64)
        val finalHorizontalOffset = desiredHorizontalOffset - buttonHorizontalPadding
        val maxWidth = 160.dp
        Row(
            modifier = Modifier
                .fillParentMaxWidth()
                .padding(
                    horizontal = finalHorizontalOffset,
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            AppTextButton(
                props = AppTextButtonProps.TextOnly(
                    text = StringValue.ResId(R.string.disable_all),
                    enabled = enableAndDisableAll.disableAllAvailable,
                    padding = buttonPaddingValues,
                ),
                onClick = {
                    callbacks.onDisableEnableAll()
                },
                modifier = Modifier
                    .widthIn(max = maxWidth),
            )
            AppTextButton(
                props = AppTextButtonProps.TextOnly(
                    text = StringValue.ResId(R.string.enable_all),
                    enabled = enableAndDisableAll.enableAllAvailable,
                    padding = buttonPaddingValues,

                ),
                onClick = {
                    callbacks.onEnableAll()
                },
                modifier = Modifier
                    .widthIn(max = maxWidth),
            )
        }
    }
}

@Composable
private fun OptionalDialog(
    props: ProductListPreviewDialogProps?,
    callbacks: ProductListPreviewCallbacks,
) {
    if (props != null) {
        when (props) {
            is ProductListPreviewDialogProps.ConfirmPastedProductsWrapper -> {
                ConfirmPastedListDialog(
                    props = props.dialog,
                    callbacks = callbacks,
                )
            }
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
            PreloaderOrContent(
                props = props,
                callbacks = ProductListPreviewCallbacksMock,
                modifier = Modifier
                    .padding(padding),
                bottomBar = {},
            )
        }
    }
}
