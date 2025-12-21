package app.grocery.list.main.activity.ui.content

import androidx.compose.runtime.Immutable

@Immutable
sealed class AppLevelDialog {

    @Immutable
    data object AppPushNotificationsDenied
        : AppLevelDialog()
}
