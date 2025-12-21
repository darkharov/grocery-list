package app.grocery.list.main.activity.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.domain.formatter.ProductTitleWithoutEmojiFormatter
import app.grocery.list.domain.product.Product
import app.grocery.list.domain.product.ProductRepository
import app.grocery.list.domain.settings.ProductTitleFormat
import app.grocery.list.domain.settings.SettingsRepository
import app.grocery.list.kotlin.ellipsize
import app.grocery.list.main.activity.ui.content.AppEvent
import app.grocery.list.main.activity.ui.content.AppLevelDialog
import app.grocery.list.main.activity.ui.content.AppSnackbar
import app.grocery.list.storage.value.kotlin.get
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val productRepository: ProductRepository,
) : ViewModel() {

    private val appEvents = Channel<AppEvent>(capacity = Channel.UNLIMITED)
    private val snackbars = Channel<AppSnackbar>(capacity = Channel.UNLIMITED)
    private val dialog = MutableStateFlow<AppLevelDialog?>(null)

    val numberOfEnabledProducts =
        productRepository
            .numberOfEnabled()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val hasEmojiIfEnoughSpace =
        settingsRepository
            .productTitleFormat
            .observe()
            .map {
                when (it) {
                    ProductTitleFormat.WithoutEmoji -> {
                        false
                    }
                    ProductTitleFormat.EmojiAndFullText,
                    ProductTitleFormat.EmojiAndAdditionalDetails -> {
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
        viewModelScope.launch {
            progress.value = true
            val enabled = settingsRepository.clearNotificationsReminderEnabled.get()
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
            formattedTitle = ProductTitleWithoutEmojiFormatter
                .print(product)
                .collectStringTitle()
                .ellipsize(maxLength = 10),
        )
        snackbars.trySend(event)
    }

    fun undoProductDeletion(product: Product) {
        viewModelScope.launch {
            productRepository.put(product)
        }
    }

    fun notifyPostNotificationsDenied() {
        dialog.value = AppLevelDialog.AppPushNotificationsDenied
    }

    fun notifyDialogDismiss() {
        dialog.value = null
    }

    fun dialog(): StateFlow<AppLevelDialog?> =
        dialog
}
