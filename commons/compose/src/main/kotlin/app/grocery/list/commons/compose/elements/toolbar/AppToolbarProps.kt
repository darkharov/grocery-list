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
    internal val leadingIcon get() = content.icons.leading
    internal val trailingIcon get() = content.icons.trailing

    @Immutable
    abstract class Content {

        @get:StringRes
        internal abstract val titleId: Int
        internal abstract val counter: Int?
        internal open val hasEmojiIfEnoughSpace: Boolean = false

        internal abstract val isOnStart: Boolean

        val icons: Icons by lazy {
            if (isOnStart) {
                Icons(
                    leading = null,
                    trailing = AppToolbarIconProps.Settings,
                )
            } else {
                Icons(
                    leading = AppToolbarIconProps.Up,
                    trailing = null,
                )
            }
        }

        @Immutable
        data class Icons(
            val leading: AppToolbarIconProps.Leading?,
            val trailing: AppToolbarIconProps.Trailing?,
        )

        @Immutable
        data class Default(
            override val counter: Int?,
            override val hasEmojiIfEnoughSpace: Boolean,
            override val isOnStart: Boolean,
        ) : Content() {
            override val titleId = R.string.grocery_list
        }
    }

    @Immutable
    data class Title(
        override val titleId: Int,
    ) : Content() {
        override val counter = null
        override val isOnStart = false
    }
}

internal class AppToolbarMocks : PreviewParameterProvider<AppToolbarProps.Content> {
    override val values = sequenceOf(
        AppToolbarProps.Content.Default(
            counter = 0,
            isOnStart = true,
            hasEmojiIfEnoughSpace = true,
        ),
        AppToolbarProps.Content.Default(
            counter = 11,
            isOnStart = false,
            hasEmojiIfEnoughSpace = true,
        ),
        AppToolbarProps.Title(R.string.settings),
    )
}
