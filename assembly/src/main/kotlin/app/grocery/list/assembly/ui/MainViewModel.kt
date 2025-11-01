package app.grocery.list.assembly.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.assembly.ui.content.AppEvent
import app.grocery.list.assembly.ui.content.AppSnackbar
import app.grocery.list.domain.AppRepository
import app.grocery.list.domain.ellipsize
import app.grocery.list.domain.format.ProductTitleFormatter
import app.grocery.list.domain.product.Product
import app.grocery.list.domain.product.ProductRepository
import app.grocery.list.storage.value.kotlin.get
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val appRepository: AppRepository,
    private val productRepository: ProductRepository,
) : ViewModel() {

    private val appEvents = Channel<AppEvent>(capacity = Channel.UNLIMITED)
    private val snackbars = Channel<AppSnackbar>(capacity = Channel.UNLIMITED)

    val numberOfEnabledProducts =
        productRepository
            .numberOfEnabledProducts()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val hasEmojiIfEnoughSpace =
        appRepository
            .productTitleFormatter
            .observe()
            .map {
                when (it) {
                    ProductTitleFormatter.WithoutEmoji -> {
                        false
                    }
                    ProductTitleFormatter.EmojiAndFullText,
                    ProductTitleFormatter.EmojiAndAdditionalDetail -> {
                        true
                    }
                }
            }
            .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val progress = MutableStateFlow(false)

    fun appEvents(): ReceiveChannel<AppEvent> =
        appEvents

    fun snackbars(): ReceiveChannel<AppSnackbar> =
        snackbars

    fun notifyPushNotificationsGranted() {
        viewModelScope.launch(Dispatchers.IO) {
            progress.value = true
            val enabled = appRepository.clearNotificationsReminderEnabled.get()
            val event = AppEvent.PushNotificationsGranted(
                clearNotificationsReminderEnabled = enabled
            )
            appEvents.trySend(event)
            progress.value = false
        }
    }

    fun showUndoProductDeletionSnackbar(product: Product) {
        val event = AppSnackbar.UndoDeletionProduct(
            product = product,
            formattedTitle = ProductTitleFormatter.WithoutEmoji
                .print(product)
                .collectStringTitle()
                .ellipsize(maxLength = 10),
        )
        snackbars.trySend(event)
    }

    fun undoProductDeletion(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.putProduct(product)
        }
    }
}
