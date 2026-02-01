package app.grocery.list.main.activity.ui.content

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object ClearNotificationsReminder : NavKey

@Serializable
data object FinalSteps : NavKey

@Serializable
data class ProductInputForm(
    val productId: Int? = null,
) : NavKey

@Serializable
data object ProductListActions : NavKey

@Serializable
data object ProductListPreview : NavKey

@Serializable
data object Settings : NavKey

@Serializable
data object ListFormatSettings : NavKey

@Serializable
data object BottomBarSettings : NavKey
