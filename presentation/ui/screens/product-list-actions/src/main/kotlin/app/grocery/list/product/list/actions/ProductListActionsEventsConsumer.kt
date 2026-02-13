package app.grocery.list.product.list.actions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import app.grocery.list.commons.compose.EventConsumer
import commons.android.share
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
internal fun ProductListActionsEventsConsumer(
    events: ReceiveChannel<ProductListActionsViewModel.Event>,
    callbacks: ProductListActionsCallbacks,
    contract: ProductListActionsContract,
) {
    val context = LocalContext.current
    val clipboard = LocalClipboard.current
    val clipboardScope = rememberCoroutineScope()
    EventConsumer(events) { event ->
        when (event) {
            is ProductListActionsViewModel.Event.OnExitFromApp -> {
                contract.exitFromApp()
            }
            is ProductListActionsViewModel.Event.OnShare -> {
                context.share(text = event.sharingString)
            }
            is ProductListActionsViewModel.Event.OnStartShopping -> {
                contract.startShopping()
            }
            is ProductListActionsViewModel.Event.OnPasteCopiedList -> {
                clipboardScope.launch {
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
                contract.goToNewProductInputForm()
            }
            is ProductListActionsViewModel.Event.OnGoToActions -> {
                contract.goToProductListActions()
            }
            is ProductListActionsViewModel.Event.OnGoBack -> {
                contract.goBack()
            }
        }
    }
}
