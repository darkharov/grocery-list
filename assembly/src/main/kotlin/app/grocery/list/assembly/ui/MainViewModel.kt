package app.grocery.list.assembly.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.assembly.ui.content.AppEvent
import app.grocery.list.assembly.ui.content.AppSnackbar
import app.grocery.list.commons.format.ProductTitleFormatter
import app.grocery.list.domain.AppRepository
import app.grocery.list.domain.Product
import app.grocery.list.domain.settings.ProductTitleFormat
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
    private val repository: AppRepository,
    private val productTitleFormatterFactory: ProductTitleFormatter.Factory,
) : ViewModel() {

    private val appEvents = Channel<AppEvent>(capacity = Channel.UNLIMITED)
    private val snackbars = Channel<AppSnackbar>(capacity = Channel.UNLIMITED)

    val numberOfEnabledProducts =
        repository
            .numberOfEnabledProducts()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val hasEmojiIfEnoughSpace =
        repository
            .productTitleFormat()
            .map {
                when (it) {
                    ProductTitleFormat.WithoutEmoji ->
                        false
                    ProductTitleFormat.EmojiAndFullText,
                    ProductTitleFormat.EmojiAndAdditionalDetail ->
                        true
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
            val enabled = repository.clearNotificationsReminderEnabled.get()
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
            formattedTitle = productTitleFormatterFactory
                .create(ProductTitleFormat.EmojiAndFullText)
                .print(product)
                .collectStringTitle(),
        )
        snackbars.trySend(event)
    }

    fun undoProductDeletion(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.putProduct(product)
        }
    }
}
