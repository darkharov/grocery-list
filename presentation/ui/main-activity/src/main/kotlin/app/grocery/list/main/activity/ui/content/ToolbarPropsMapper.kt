package app.grocery.list.main.activity.ui.content

import app.grocery.list.commons.compose.elements.toolbar.AppToolbarIconProps
import app.grocery.list.commons.compose.elements.toolbar.AppToolbarProps
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.domain.product.list.ProductList
import app.grocery.list.domain.settings.ProductTitleFormat
import app.grocery.list.domain.theming.AppTopLevelParams
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ToolbarPropsMapper @Inject constructor() {

    fun transform(params: Params): AppToolbarProps {
        val (progress, currentScreen, topLevelParams) = params
        return AppToolbarProps(
            title = when (val title = currentScreen.title) {
                is AppNavKey.Title.Custom -> {
                    title.value
                }
                is AppNavKey.Title.CurrentList -> {
                    StringValue.StringWrapper(
                        topLevelParams.currentProductList.title,
                    )
                }
            },
            counter = topLevelParams.numberOfEnabledProducts.takeIf {
                currentScreen.title is AppNavKey.Title.CurrentList
            },
            mightHaveEmoji =
                currentScreen.title is AppNavKey.Title.CurrentList &&
                topLevelParams.currentProductList.id is ProductList.Id.Default &&
                topLevelParams.productTitleFormat != ProductTitleFormat.WithoutEmoji,
            icons = when (currentScreen.level) {
                AppNavKey.Level.Initial -> {
                    AppToolbarProps.Icons(
                        leading = AppToolbarIconProps.AllLists.takeIf {
                            topLevelParams.customListsFeatureEnabled
                        },
                        trailing = AppToolbarIconProps.Settings,
                    )
                }
                AppNavKey.Level.Subsequential -> {
                    AppToolbarProps.Icons(
                        leading = AppToolbarIconProps.Up,
                        trailing = null,
                    )
                }
            },
            progress = progress,
        )
    }

    data class Params(
        val progress: Boolean,
        val currentScreen: AppNavKey,
        val topLevelParams: AppTopLevelParams,
    )
}
