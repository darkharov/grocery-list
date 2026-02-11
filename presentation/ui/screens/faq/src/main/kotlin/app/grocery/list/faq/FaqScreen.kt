package app.grocery.list.faq

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.grocery.list.commons.compose.elements.AppPreloader
import app.grocery.list.commons.compose.elements.ScrollableContentWithShadows
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.faq.item.FaqItem

private const val TopOffset = "TopOffset"
private const val BottomOffset = "BottomOffset"

@Composable
fun FaqScreen() {
    val viewModel = hiltViewModel<FaqViewModel>()
    val props by viewModel.props.collectAsStateWithLifecycle()
    FaqScreen(
        props = props,
        callbacks = viewModel,
    )
}

@Composable
private fun FaqScreen(
    props: FaqProps?,
    callbacks: FaqCallbacks,
) {
    if (props == null) {
        AppPreloader()
    } else {
        Content(
            props = props,
            callbacks = callbacks,
        )
    }
}

@Composable
private fun Content(
    props: FaqProps,
    callbacks: FaqCallbacks,
) {
    val listState = rememberLazyListState()
    ScrollableContentWithShadows(
        scrollableState = listState,
    ) {
        LazyColumn(
            state = listState,
        ) {
            topOffset()
            items(props, callbacks)
            bottomOffset()
        }
    }
}

private fun LazyListScope.topOffset() {
    item(
        key = TopOffset,
        contentType = TopOffset,
    ) {
        Spacer(
            modifier = Modifier
                .height(16.dp),
        )
    }
}

private fun LazyListScope.items(
    props: FaqProps,
    callbacks: FaqCallbacks,
) {
    items(
        items = props.items,
        key = { it.id },
        contentType = { "faq item" },
    ) {
        FaqItem(
            props = it,
            callbacks = callbacks,
        )
    }
}

private fun LazyListScope.bottomOffset() {
    item(
        key = BottomOffset,
        contentType = BottomOffset,
    ) {
        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.systemBars),
        )
    }
}


@Preview
@Composable
private fun FaqScreenPreview(
    @PreviewParameter(
        provider = FaqMocks::class,
    )
    props: FaqProps,
) {
    GroceryListTheme {
        FaqScreen(
            props = props,
            callbacks = FaqCallbacksMock,
        )
    }
}
