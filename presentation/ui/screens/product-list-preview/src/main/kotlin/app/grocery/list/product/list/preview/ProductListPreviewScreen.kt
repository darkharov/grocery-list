package app.grocery.list.product.list.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.grocery.list.commons.compose.EventConsumer
import app.grocery.list.commons.compose.elements.AppPreloader
import app.grocery.list.commons.compose.elements.ScrollableContentWithShadows
import app.grocery.list.commons.compose.elements.dialog.AppHowToEditListItemsDialog
import app.grocery.list.commons.compose.elements.dialog.list.ConfirmPastedListDialog
import app.grocery.list.commons.compose.elements.question.optionalAppQuestion
import app.grocery.list.commons.compose.elements.start.and.end.button.panel.AppStartAndEndButtonPanel
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppColors
import app.grocery.list.commons.compose.theme.LocalAppTypography
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.commons.compose.values.value
import app.grocery.list.product.list.preview.ProductListPreviewViewModel.Event
import app.grocery.list.product.list.preview.elements.item.ProductItem
import app.grocery.list.product.list.preview.elements.neighbours.ProductListNeighbours
import app.grocery.list.product.list.preview.elements.neighbours.ProductListNeighboursProps

@Composable
fun ProductListPreviewScreen(
    contract: ProductListPreviewContract,
    bottomBar: @Composable () -> Unit,
) {
    val viewModel = hiltViewModel<ProductListPreviewViewModel>()
    val props by viewModel.props.collectAsStateWithLifecycle()
    val dialog by viewModel.dialog().collectAsStateWithLifecycle()
    PreloaderOrContent(
        props = props,
        callbacks = viewModel,
        bottomBar = bottomBar,
    )
    EventConsumer(viewModel.events()) { event ->
        when (event) {
            is Event.OnProductDeleted -> {
                contract.showUndoProductDeletionSnackbar(event.product)
            }
            is Event.OnEditProduct -> {
                contract.goToProductEditingForm(
                    productId = event.productId,
                )
            }
            is Event.OnNeedMoreListsClick -> {
                contract.goToCustomProductListsSettings()
            }
        }
    }
    OptionalDialog(
        props = dialog,
        callbacks = viewModel,
    )
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
            when (val currentList = props.currentListContent) {
                is ProductListPreviewProps.Empty -> {
                    ListEmptyAndTemplates(
                        content = currentList,
                        neighbours = props.neighbours,
                        callbacks = callbacks,
                    )
                }
                is ProductListPreviewProps.Items -> {
                    ListWithDividers(
                        currentListContent = currentList,
                        neighbours = props.neighbours,
                        listState = rememberLazyListState(),
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
    content: ProductListPreviewProps.Empty,
    neighbours: ProductListNeighboursProps?,
    callbacks: ProductListPreviewCallbacks,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp + dimensionResource(R.dimen.margin_16_32_64)),
        ) {
            val startPadding = 8.dp
            Text(
                text = content.text.value(),
                color = LocalAppColors.current.blackOrWhite,
                style = LocalAppTypography.current.label,
                modifier = Modifier
                    .padding(start = startPadding),
            )
            Spacer(
                modifier = Modifier
                    .height(16.dp),
            )
            val templates = content.templates
            if (templates != null) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    for (template in templates) {
                        Text(
                            text = "+ ${template.title}",
                            color = LocalAppColors.current.brand_40_40,
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
        if (neighbours != null) {
            ProductListNeighbours(
                props = neighbours,
                callbacks = callbacks,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
private fun ListWithDividers(
    currentListContent: ProductListPreviewProps.Items,
    neighbours: ProductListNeighboursProps?,
    listState: LazyListState,
    callbacks: ProductListPreviewCallbacks,
    modifier: Modifier = Modifier,
) {
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
                currentListContent = currentListContent,
                neighbours = neighbours,
                callbacks = callbacks,
            )
        }
    }
}

private fun LazyListScope.items(
    currentListContent: ProductListPreviewProps.Items,
    neighbours: ProductListNeighboursProps?,
    callbacks: ProductListPreviewCallbacks,
) {
    val enableAndDisableAll = currentListContent.enableAndDisableAll
    if (enableAndDisableAll != null) {
        enableAndDisableAll(
            enableAndDisableAll = enableAndDisableAll,
            callbacks = callbacks,
        )
    }
    for ((index, item) in currentListContent.items.withIndex()) {
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
                    color = LocalAppColors.current.blackOrWhite,
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
    optionalAppQuestion(
        props = currentListContent.question,
        callbacks = callbacks,
        modifier = Modifier
            .padding(top = 16.dp),
    )
    if (neighbours != null) {
        item(
            key = neighbours.key,
            contentType = "Neighbours",
        ) {
            ProductListNeighbours(
                props = neighbours,
                callbacks = callbacks,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillParentMaxWidth()
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
        contentType = "Disable All and Enable All",
        key = enableAndDisableAll.key,
    ) {
        AppStartAndEndButtonPanel(
            startButtonText = StringValue.ResId(R.string.disable_all),
            startButtonEnabled = enableAndDisableAll.disableAllAvailable,
            onStartClick = {
                callbacks.onDisableEnableAll()
            },
            endButtonText = StringValue.ResId(R.string.enable_all),
            endButtonEnabled = enableAndDisableAll.enableAllAvailable,
            onEndClick = {
                callbacks.onEnableAll()
            },
            modifier = Modifier
                .fillParentMaxWidth(),
        )
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
            is ProductListPreviewDialogProps.HowToEditProducts -> {
                AppHowToEditListItemsDialog(
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
        PreloaderOrContent(
            props = props,
            modifier = Modifier
                .background(LocalAppColors.current.background),
            callbacks = ProductListPreviewCallbacksMock,
            bottomBar = {},
        )
    }
}
