package app.grocery.list.product.list.actions

import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import app.grocery.list.commons.compose.elements.button.AppButtonStateProps
import app.grocery.list.commons.compose.values.StringValue

@Immutable
internal class ProductListActionsProps(
    val useIconsOnBottomBar: Boolean,
    val exitButtonTitle: StringValue,
    val listActionButtonsState: AppButtonStateProps,
    val suggestionToSwitchToIconsVisible: Boolean,
)

internal class ProductListActionsMocks : PreviewParameterProvider<ProductListActionsProps> {

    override val values =
        sequenceOf(0, 11).map {
            ProductListActionsProps(
                useIconsOnBottomBar = false,
                exitButtonTitle = StringValue.ResId(R.string.exit),
                listActionButtonsState = AppButtonStateProps.Normal,
                suggestionToSwitchToIconsVisible = true,
            )
        }
}
