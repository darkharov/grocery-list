package app.grocery.list.product.list.actions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalClipboard
import app.grocery.list.commons.compose.EventConsumer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
internal fun ProductListActionsEventsConsumer(
    events: ReceiveChannel<ProductListActionsViewModel.Event>,
    callbacks: ProductListActionsCallbacks,
    delegate: ProductListActionsDelegate,
    navigation: ProductListActionsNavigation,
) {
    val clipboard = LocalClipboard.current
    val clipboardScope = rememberCoroutineScope()
    EventConsumer(events) { event ->
        when (event) {
            is ProductListActionsViewModel.Event.OnExitFromApp -> {
                delegate.exitFromApp()
            }
            is ProductListActionsViewModel.Event.OnShare -> {
                delegate.shareProducts(sharingString = event.sharingString)
            }
            is ProductListActionsViewModel.Event.OnStartShopping -> {
                delegate.startShopping()
            }
            is ProductListActionsViewModel.Event.OnPasteCopiedList -> {
                clipboardScope.launch(Dispatchers.IO) {
                    val clipEntry = clipboard.getClipEntry()
                    withContext(Dispatchers.Main) {
                        val text = clipEntry
                            ?.clipData
                            ?.getItemAt(0)
                            ?.text
                            ?.toString()
                            .orEmpty()
                        callbacks.onPasted(text = text)
                    }
                }
            }
            is ProductListActionsViewModel.Event.OnGoToProductInputForm -> {
                navigation.goToNewProductInputForm()
            }
            is ProductListActionsViewModel.Event.OnGoToActions -> {
                navigation.goToProductListActions()
            }
            is ProductListActionsViewModel.Event.OnGoBack -> {
                navigation.goBack()
            }
        }
    }
}
