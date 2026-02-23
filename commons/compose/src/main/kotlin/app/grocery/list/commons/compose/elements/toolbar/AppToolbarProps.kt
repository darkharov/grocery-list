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

        abstract val icons: Icons

        @Immutable
        data class Icons(
            val leading: AppToolbarIconProps.Leading?,
            val trailing: AppToolbarIconProps.Trailing?,
        )

        @Immutable
        data class Default(
            override val counter: Int?,
            override val hasEmojiIfEnoughSpace: Boolean,
            override val icons: Icons,
        ) : Content() {
            override val titleId = R.string.grocery_list

            constructor(
                counter: Int?,
                hasEmojiIfEnoughSpace: Boolean,
                isOnStart: Boolean,
                isCustomListsFeatureEnabled: Boolean,
            ) : this(
                counter = counter,
                hasEmojiIfEnoughSpace = hasEmojiIfEnoughSpace,
                icons = if (isOnStart) {
                    Icons(
                        leading = if (isCustomListsFeatureEnabled) {
                            AppToolbarIconProps.AllLists
                        } else {
                            null
                        },
                        trailing = AppToolbarIconProps.Settings,
                    )
                } else {
                    Icons(
                        leading = AppToolbarIconProps.Up,
                        trailing = null,
                    )
                }
            )
        }
    }

    @Immutable
    data class InnerScreenWithCustomTitle(
        override val titleId: Int,
    ) : Content() {
        override val counter = null

        override val icons = Icons(
            leading = AppToolbarIconProps.Up,
            trailing = null,
        )
    }
}

internal class AppToolbarMocks : PreviewParameterProvider<AppToolbarProps.Content> {
    override val values = sequenceOf(
        AppToolbarProps.Content.Default(
            counter = 0,
            isOnStart = true,
            hasEmojiIfEnoughSpace = true,
            isCustomListsFeatureEnabled = true,
        ),
        AppToolbarProps.Content.Default(
            counter = 11,
            isOnStart = false,
            hasEmojiIfEnoughSpace = true,
            isCustomListsFeatureEnabled = false,
        ),
        AppToolbarProps.InnerScreenWithCustomTitle(R.string.settings),
    )
}
