package app.grocery.list.main.activity.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import app.grocery.list.domain.formatter.ProductTitleWithoutEmojiFormatter
import app.grocery.list.domain.product.Product
import app.grocery.list.domain.product.ProductRepository
import app.grocery.list.domain.settings.ProductTitleFormat
import app.grocery.list.domain.settings.SettingsRepository
import app.grocery.list.kotlin.ellipsize
import app.grocery.list.main.activity.ui.content.AppContentContract
import app.grocery.list.main.activity.ui.content.AppLevelDialog
import app.grocery.list.main.activity.ui.content.AppSnackbar
import app.grocery.list.main.activity.ui.content.BottomBarSettings
import app.grocery.list.main.activity.ui.content.ClearNotificationsReminder
import app.grocery.list.main.activity.ui.content.Faq
import app.grocery.list.main.activity.ui.content.FinalSteps
import app.grocery.list.main.activity.ui.content.ListFormatSettings
import app.grocery.list.main.activity.ui.content.ProductInputForm
import app.grocery.list.main.activity.ui.content.ProductListActions
import app.grocery.list.main.activity.ui.content.ProductListPreview
import app.grocery.list.main.activity.ui.content.Settings
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
) : ViewModel(),
    AppContentContract {

    private val snackbars = Channel<AppSnackbar>(capacity = Channel.UNLIMITED)
    private val dialog = MutableStateFlow<AppLevelDialog?>(null)
    private val events = Channel<Event>(capacity = Channel.UNLIMITED)

    val backStack = NavBackStack<NavKey>(ProductListPreview)

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

    override fun snackbars(): ReceiveChannel<AppSnackbar> =
        snackbars

    override fun showUndoProductDeletionSnackbar(product: Product) {
        val event = AppSnackbar.UndoDeletionProduct(
            product = product,
            formattedTitle = ProductTitleWithoutEmojiFormatter
                .print(product)
                .collectStringTitle()
                .ellipsize(maxLength = 10),
        )
        snackbars.trySend(event)
    }

    override fun undoProductDeletion(product: Product) {
        viewModelScope.launch {
            productRepository.put(product)
        }
    }

    override fun openNotificationSettings() {
        events.trySend(Event.OnNotificationSettingsRequired)
    }

    override fun exitFromApp() {
        events.trySend(Event.OnExitFromAppSelected)
    }

    override fun startShopping() {
        events.trySend(Event.OnStartShopping)
    }

    override fun dismissDialog() {
        dialog.value = null
    }

    override fun handleUpClick() {
        backStack.removeLastOrNull()
    }

    override fun handleTrailingIconClick() {
        backStack.add(Settings)
    }

    override fun goToNewProductInputForm() {
        backStack.add(ProductInputForm())
    }

    override fun goToProductListActions() {
        backStack.add(ProductListActions)
    }

    override fun goBack() {
        backStack.removeLastOrNull()
    }

    override fun goToListFormatSettings() {
        backStack.add(ListFormatSettings)
    }

    override fun goToBottomBarSettings() {
        backStack.add(BottomBarSettings)
    }

    override fun goToFaq() {
        backStack.add(Faq)
    }

    override fun goToProductEditingForm(productId: Int) {
        backStack.add(ProductInputForm(productId = productId))
    }

    override fun goToFinalSteps() {
        backStack.add(FinalSteps)
    }

    override fun backToListPreview() {
        backStack.retainAll { it is ProductListPreview }
    }

    fun notifyPushNotificationsGranted() {
        viewModelScope.launch {
            progress.value = true
            val enabled = settingsRepository.clearNotificationsReminderEnabled.get()
            val key = if (enabled) {
                ClearNotificationsReminder
            } else {
                FinalSteps
            }
            backStack.add(key)
            progress.value = false
        }
    }

    fun notifyPostNotificationsDenied() {
        dialog.value = AppLevelDialog.AppPushNotificationsDenied
    }

    fun dialog(): StateFlow<AppLevelDialog?> =
        dialog

    fun events(): ReceiveChannel<Event> =
        events

    sealed class Event {
        data object OnNotificationSettingsRequired : Event()
        data object OnExitFromAppSelected : Event()
        data object OnStartShopping : Event()
    }
}
