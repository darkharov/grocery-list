package app.grocery.list.commons.compose.elements.toolbar

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import app.grocery.list.commons.compose.R

@Immutable
data class AppToolbarProps(
    internal val content: Content,
    internal val progress: Boolean,
) {
    internal val titleId get() = content.titleId
    internal val counter get() = content.counter
    internal val hasEmojiIfEnoughSpace get() = content.hasEmojiIfEnoughSpace
    internal val leadingIcon get() = content.leadingIcon
    internal val trailingIcon get() = content.trailingIcon

    @Immutable
    abstract class Content {

        @get:StringRes
        internal abstract val titleId: Int
        internal abstract val counter: Int?
        internal open val hasEmojiIfEnoughSpace: Boolean = false

        protected abstract val upIconAvailable: Boolean
        internal abstract val trailingIconContent: OptionalIconProps.Content?

        internal val leadingIcon: OptionalIconProps by lazy {
            OptionalIconProps(
                type = OptionalIconProps.Type.Leading,
                content = if (upIconAvailable) {
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

        @Immutable
        data class Default(
            override val counter: Int?,
            private val onStart: Boolean,
            override val hasEmojiIfEnoughSpace: Boolean,
        ) : Content() {

            override val upIconAvailable: Boolean = !(onStart)
            override val titleId = R.string.grocery_list
            override val trailingIconContent: OptionalIconProps.Content? by lazy {
                if (onStart) {
                    OptionalIconProps.Content(
                        iconId = R.drawable.ic_settings,
                        descriptionId = R.string.settings,
                    )
                } else {
                    null
                }
            }
        }
    }

    @Immutable
    data class Title(override val titleId: Int) : Content() {
        override val counter = null
        override val trailingIconContent = null
        override val upIconAvailable = true
    }
}

internal class AppToolbarMocks : PreviewParameterProvider<AppToolbarProps.Content> {
    override val values = sequenceOf(
        AppToolbarProps.Content.Default(
            counter = 0,
            onStart = true,
            hasEmojiIfEnoughSpace = true,
        ),
        AppToolbarProps.Content.Default(
            counter = 11,
            onStart = false,
            hasEmojiIfEnoughSpace = true,
        ),
        AppToolbarProps.Title(R.string.settings),
    )
}
