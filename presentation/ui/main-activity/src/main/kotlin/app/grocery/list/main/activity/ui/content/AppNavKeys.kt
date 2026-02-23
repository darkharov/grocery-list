package app.grocery.list.main.activity.ui.content

import androidx.navigation3.runtime.NavKey
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.domain.product.list.ProductList
import app.grocery.list.main.activity.R
import kotlinx.serialization.Serializable

interface AppNavKey : NavKey {

    val title: Title get() = Title.CurrentList
    val level: Level get() = Level.Subsequential

    sealed class Title {
        data class Custom(val value: StringValue) : Title()
        data object CurrentList : Title()
    }

    sealed class Level {
        data object Initial : Level()
        data object Subsequential : Level()
    }
}

@Serializable
data object ClearNotificationsReminder : AppNavKey

@Serializable
data object FinalSteps : AppNavKey

@Serializable
data class ProductInputForm(
    val productId: Int? = null,
) : AppNavKey {

    override val title
        get() =
            if (productId != null) {
                AppNavKey.Title.Custom(StringValue.ResId(R.string.editing))
            } else {
                AppNavKey.Title.CurrentList
            }
}

@Serializable
data object ProductListActions : AppNavKey

@Serializable
data object ProductListPreview : AppNavKey {
    override val level = AppNavKey.Level.Initial
}

@Serializable
data object Settings : AppNavKey {
    override val title = AppNavKey.Title.Custom(StringValue.ResId(R.string.settings))
}

@Serializable
data object ListFormatSettings : AppNavKey {
    override val title = AppNavKey.Title.Custom(StringValue.ResId(R.string.list_format))
}

@Serializable
data object BottomBarSettings : AppNavKey {
    override val title = AppNavKey.Title.Custom(StringValue.ResId(R.string.bottom_bar))
}

@Serializable
data object Faq : AppNavKey {
    override val title = AppNavKey.Title.Custom(StringValue.ResId(R.string.faq))
}

@Serializable
data object ProductListPicker : AppNavKey {
    override val title = AppNavKey.Title.Custom(StringValue.ResId(R.string.all_lists))
}

@Serializable
data object CustomListsSettings : AppNavKey {
    override val title = AppNavKey.Title.Custom(StringValue.ResId(R.string.additional_lists))
}

@Serializable
data class CustomListInputForm(
    val customListId: ProductList.Id.Custom?,
) : AppNavKey {

    override val title get() =
        AppNavKey.Title.Custom(
            StringValue.ResId(
                if (customListId == null) {
                    R.string.new_product_list
                } else {
                    R.string.editing
                }
            )
        )
}
