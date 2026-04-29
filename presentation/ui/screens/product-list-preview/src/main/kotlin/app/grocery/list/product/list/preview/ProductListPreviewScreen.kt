package app.grocery.list.product.list.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import app.grocery.list.commons.compose.elements.question.appQuestion
import app.grocery.list.commons.compose.elements.start.and.end.button.panel.AppStartAndEndButtonPanel
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppColors
import app.grocery.list.commons.compose.theme.LocalAppTypography
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.product.list.preview.ProductListPreviewViewModel.Event
import app.grocery.list.product.list.preview.elements.empty.list.placeholder.emptyListPlaceholder
import app.grocery.list.product.list.preview.elements.neighbours.productListNeighbours
import app.grocery.list.product.list.preview.elements.product.item.products
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
    ProductListPreviewDialog(
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
private fun ProductListPreviewScreen(
    props: ProductListPreviewProps?,
    callbacks: ProductListPreviewCallbacks,
    bottomBar: @Composable (() -> Unit),
    modifier: Modifier = Modifier,
) {
    Column {
        AppPreloaderOrContent(props) { props ->
            Content(
                props = props,
                callbacks = callbacks,
                modifier = modifier
                    .weight(1f)
                    .fillMaxWidth(),
            )
        }
        bottomBar()
    }
}

@Composable
private fun Content(
    props: ProductListPreviewProps,
    callbacks: ProductListPreviewCallbacks,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val content = props.content
    val neighbours = props.neighbours
    LaunchedEffect(props.listKey) {
        listState.animateScrollToItem(
            index = 0,
        )
    }
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
            verticalArrangement = props.arrangement,
        ) {
            placeholderOrItems(
                props = content,
                callbacks = callbacks,
            )
            productListNeighbours(
                props = neighbours,
                callbacks = callbacks,
            )
        }
    }
}

private fun LazyListScope.placeholderOrItems(
    props: ProductListPreviewProps.ListContent,
    callbacks: ProductListPreviewCallbacks,
) {
    when (props) {
        is ProductListPreviewProps.Empty -> {
            emptyListPlaceholder(
                props = props.placeholder,
                callbacks = callbacks,
            )
        }
        is ProductListPreviewProps.Items -> {
            items(
                content = props,
                callbacks = callbacks,
            )
        }
    }
}

private fun LazyListScope.items(
    content: ProductListPreviewProps.Items,
    callbacks: ProductListPreviewCallbacks,
) {
    val enableAndDisableAll = content.enableAndDisableAll
    if (enableAndDisableAll != null) {
        enableAndDisableAll(
            enableAndDisableAll = enableAndDisableAll,
            callbacks = callbacks,
        )
    }
    for (item in content.items) {
        val category = item.category
        if (category != null) {
            category(category)
        }
        products(
            items = item.products,
            callbacks = callbacks,
        )
    }
    appQuestion(
        props = content.question,
        callbacks = callbacks,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
            .padding(top = 16.dp),
    )
}

private fun LazyListScope.enableAndDisableAll(
    enableAndDisableAll: ProductListPreviewProps.Items.EnableAndDisableAll,
    callbacks: ProductListPreviewCallbacks,
) {
    item(
        key = "EnableAndDisableAll",
        contentType = "EnableAndDisableAll",
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

private fun LazyListScope.category(
    category: ProductListPreviewProps.Items.Category,
) {
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
                    top = category.topOffset,
                    bottom = 8.dp,
                )
                .animateItem(),
            color = LocalAppColors.current.blackOrWhite,
            style = LocalAppTypography.current.header3,
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
            modifier = Modifier
                .background(LocalAppColors.current.background),
        )
    }
}
