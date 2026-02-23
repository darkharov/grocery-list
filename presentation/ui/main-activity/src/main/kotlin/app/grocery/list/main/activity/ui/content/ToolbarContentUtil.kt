package app.grocery.list.main.activity.ui.content

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.navigation3.runtime.NavKey
import app.grocery.list.commons.compose.elements.toolbar.AppToolbarProps
import app.grocery.list.main.activity.R

@Immutable
object ToolbarContentUtil {

    private val customTitles = mapOf(
        Settings to AppToolbarProps.InnerScreenWithCustomTitle(R.string.settings),
        ListFormatSettings to AppToolbarProps.InnerScreenWithCustomTitle(R.string.list_format),
        BottomBarSettings to AppToolbarProps.InnerScreenWithCustomTitle(R.string.bottom_bar),
        Faq to AppToolbarProps.InnerScreenWithCustomTitle(R.string.faq),
        CustomListsSettings to AppToolbarProps.InnerScreenWithCustomTitle(R.string.additional_lists),
        ProductListPicker to AppToolbarProps.InnerScreenWithCustomTitle(R.string.my_lists),
    )

    @Stable
    fun customContentOrNull(navKey: NavKey): AppToolbarProps.Content? {

        // if this function becomes too large,
        // refactor it to ensure that open–closed principle is followed

        val staticTitle = customTitles[navKey]
        if (staticTitle != null) {
            return staticTitle
        }

        if (
            navKey is ProductInputForm &&
            navKey.productId != null
        ) {
            return AppToolbarProps.InnerScreenWithCustomTitle(R.string.editing)
        }

        return null
    }
}
