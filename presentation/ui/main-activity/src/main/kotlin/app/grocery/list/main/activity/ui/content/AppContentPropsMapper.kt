package app.grocery.list.main.activity.ui.content

import app.grocery.list.commons.compose.elements.color.scheme.ColorSchemeMapper
import app.grocery.list.domain.theming.AppTopLevelParams
import app.grocery.list.domain.theming.ColorScheme
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AppContentPropsMapper @Inject constructor(
    private val colorSchemeMapper: ColorSchemeMapper,
    private val toolbarPropsMapper: ToolbarPropsMapper,
) {
    fun toPresentation(params: Params) =
        AppContentProps(
            toolbar = toolbarPropsMapper.transform(params.toolbarParams),
            colorScheme = colorSchemeMapper.toPresentation(params.colorScheme),
        )

    data class Params(
        val colorScheme: ColorScheme,
        val toolbarParams: ToolbarPropsMapper.Params,
    ) {
        constructor(
            progress: Boolean,
            currentScreen: AppNavKey,
            topLevelParams: AppTopLevelParams,
        ) : this(
            colorScheme = topLevelParams.currentProductList.colorScheme,
            toolbarParams = ToolbarPropsMapper.Params(
                progress,
                currentScreen,
                topLevelParams,
            ),
        )
    }
}
