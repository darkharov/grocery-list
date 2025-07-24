package app.grocery.list.commons.compose.elements.toolbar

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import app.grocery.list.commons.compose.R

@Immutable
sealed class AppToolbarProps {

    @get:StringRes
    internal abstract val titleId: Int
    internal abstract val counter: Int?
    internal abstract val progress: Boolean
    internal open val hasEmoji: Boolean = false

    internal abstract val isUpIconAvailable: Boolean
    internal abstract val trailingIconContent: OptionalIconProps.Content?

    internal val leadingIcon: OptionalIconProps by lazy {
        OptionalIconProps(
            type = OptionalIconProps.Type.Leading,
            content = if (isUpIconAvailable) {
                OptionalIconProps.Content(
                    iconId = R.drawable.ic_back,
                    descriptionId = null,
                )
            } else {
                null
            },
        )
    }

    internal val trailingIcon: OptionalIconProps by lazy {
        OptionalIconProps(
            type = OptionalIconProps.Type.Trailing,
            content = trailingIconContent,
        )
    }

    data class Default(
        override val counter: Int?,
        override val progress: Boolean,
        val isOnStart: Boolean,
    ) : AppToolbarProps() {

        override val hasEmoji = true
        override val titleId = R.string.grocery_list
        override val isUpIconAvailable = isOnStart
        override val trailingIconContent: OptionalIconProps.Content by lazy {
            OptionalIconProps.Content(
                iconId = R.drawable.ic_settings,
                descriptionId = R.string.settings,
            )
        }
    }

    data class Settings(
        override val progress: Boolean,
    ) : AppToolbarProps() {
        override val titleId = R.string.settings
        override val counter = null
        override val trailingIconContent = null
        override val isUpIconAvailable = true
    }
}

internal class AppToolbarMocks : PreviewParameterProvider<AppToolbarProps> {
    override val values = sequenceOf(
        AppToolbarProps.Default(
            counter = 0,
            isOnStart = true,
            progress = false,
        ),
        AppToolbarProps.Default(
            counter = 11,
            isOnStart = true,
            progress = true,
        ),
        AppToolbarProps.Settings(
            progress = false,
        )
    )
}
