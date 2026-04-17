package app.grocery.list.product.list.preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.grocery.list.commons.compose.EventConsumer
import app.grocery.list.commons.compose.elements.AppPreloaderOrContent
import app.grocery.list.commons.compose.elements.ScrollableContentWithShadows
import app.grocery.list.commons.compose.elements.dialog.AppHowToEditListItemsDialog
import app.grocery.list.commons.compose.elements.dialog.list.ConfirmPastedListDialog
import app.grocery.list.commons.compose.elements.question.appQuestion
import app.grocery.list.commons.compose.elements.start.and.end.button.panel.AppStartAndEndButtonPanel
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppColors
import app.grocery.list.commons.compose.theme.LocalAppTypography
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.product.list.preview.ProductListPreviewViewModel.Event
import app.grocery.list.product.list.preview.elements.empty.list.placeholder.EmptyListPlaceholder
import app.grocery.list.product.list.preview.elements.item.ProductItem
import app.grocery.list.product.list.preview.elements.neighbours.ProductListNeighbours
import app.grocery.list.product.list.preview.elements.neighbours.ProductListNeighboursProps
import app.grocery.list.product.list.preview.elements.neighbours.productListNeighbours
import kotlinx.coroutines.channels.ReceiveChannel

@Composable
fun ProductListPreviewScreen(
    contract: ProductListPreviewContract,
    bottomBar: @Composable () -> Unit,
) {
    val viewModel = hiltViewModel<ProductListPreviewViewModel>()
    val props by viewModel.props.collectAsStateWithLifecycle()
    val dialog by viewModel.dialog().collectAsStateWithLifecycle()
    EventConsumer(
        events = viewModel.events(),
        contract = contract,
    )
    OptionalDialog(
        props = dialog,
        callbacks = viewModel,
    )
    ProductListPreviewScreen(
        props = props,
        callbacks = viewModel,
        bottomBar = bottomBar
    )
}

@Composable
private fun EventConsumer(
    events: ReceiveChannel<Event>,
    contract: ProductListPreviewContract,
) {
    EventConsumer(events) { event ->
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
}

@Composable
private fun OptionalDialog(
    props: ProductListPreviewDialogProps?,
    callbacks: ProductListPreviewCallbacks,
) {
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
        null -> {
            // nothing to show
        }
    }
}

@Composable
private fun ProductListPreviewScreen(
    props: ProductListPreviewProps?,
    callbacks: ProductListPreviewCallbacks,
    bottomBar: @Composable (() -> Unit),
) {
    AppPreloaderOrContent(props) { props ->
        Content(
            props = props,
            callbacks = callbacks,
            bottomBar = bottomBar,
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
        val listContent = props.currentListContent
        val neighbours = props.neighbours
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            when (listContent) {
                is ProductListPreviewProps.Empty -> {
                    EmptyListPlaceholder(
                        props = listContent.backing,
                        callbacks = callbacks,
                        modifier = Modifier
                            .align(Alignment.Center),
                    )
                }
                is ProductListPreviewProps.Items -> {
                    ListWithDividers(
                        currentListContent = listContent,
                        neighbours = neighbours,
                        listState = rememberLazyListState(),
                        callbacks = callbacks,
                    )
                }
            }
        }
        if (
            listContent is ProductListPreviewProps.Empty &&
            neighbours != null
        ) {
            ProductListNeighbours(
                props = neighbours,
                callbacks = callbacks,
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }
        bottomBar()
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
    appQuestion(
        props = currentListContent.question,
        callbacks = callbacks,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
            .padding(top = 16.dp),
    )
    productListNeighbours(
        props = neighbours,
        callbacks = callbacks,
    )
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
            bottomBar = {},
        )
    }
}
