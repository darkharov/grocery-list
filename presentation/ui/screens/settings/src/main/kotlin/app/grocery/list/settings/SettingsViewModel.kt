package app.grocery.list.settings

import androidx.lifecycle.ViewModel
import app.grocery.list.domain.AppInfo
import app.grocery.list.settings.dialog.SettingsDialogProps
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
internal class SettingsViewModel @Inject constructor(
    appInfo: AppInfo,
) : ViewModel(),
    SettingsCallbacks {

    val props = MutableStateFlow(
        SettingsProps(
            appVersionName = appInfo.versionName,
            appVersionCode = appInfo.versionCode,
        )
    )
    val dialog = MutableStateFlow<SettingsDialogProps?>(null)

    private val events = Channel<Event>(Channel.UNLIMITED)

    override fun onListFormatClick() {
        events.trySend(Event.OnGoToListFormatSettings)
    }

    override fun onContactSupportClick() {
        events.trySend(Event.OnContactSupport)
    }

    override fun onFaqClick() {
        events.trySend(Event.OnFaqClick)
    }

    override fun onPrivacyPolicyClick() {
        events.trySend(Event.OnPrivacyPolicyClick)
    }

    override fun onBrowserAppNotFound() {
        dialog.value = SettingsDialogProps.BrowserNotFound
    }

    override fun onBottomBarItemClick() {
        events.trySend(Event.OnBottomBarItemClick)
    }

    override fun onDismiss() {
        dialog.value = null
    }

    fun events(): ReceiveChannel<Event> =
        events

    sealed class Event {
        data object OnGoToListFormatSettings: Event()
        data object OnContactSupport: Event()
        data object OnFaqClick: Event()
        data object OnPrivacyPolicyClick : Event()
        data object OnBottomBarItemClick : Event()
    }
}
