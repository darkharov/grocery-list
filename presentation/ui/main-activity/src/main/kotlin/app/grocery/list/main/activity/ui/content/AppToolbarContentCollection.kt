package app.grocery.list.main.activity.ui.content

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.elements.toolbar.AppToolbarProps
import app.grocery.list.settings.Settings
import app.grocery.list.settings.bottom.bar.BottomBarSettings
import app.grocery.list.settings.list.format.ListFormatSettings
import kotlin.reflect.KClass
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Immutable
internal class AppToolbarContentCollection private constructor(
    private val entries: ImmutableList<Entry>,
) {
    @Stable
    fun getOrDefault(
        destination: NavDestination?,
        default: () -> AppToolbarProps.Content,
    ): AppToolbarProps.Content =
        if (destination == null) {
            default()
        } else {
            entries
                .firstNotNullOfOrNull { it.tryToGetContentFor(destination) }
                ?: default()
        }

    internal class Entry(
        private val route: KClass<*>,
        private val content: AppToolbarProps.Content,
    ) {
        fun tryToGetContentFor(destination: NavDestination) =
            if (destination.hasRoute(route)) {
                content
            } else {
                null
            }
    }

    class Builder {

        private val entries = mutableListOf<Entry>()

        inline fun <reified T> title(@StringRes titleId: Int) {
            entries.add(
                Entry(
                    route = T::class,
                    content = AppToolbarProps.Title(
                        titleId = titleId,
                    ),
                )
            )
        }

        fun build(): AppToolbarContentCollection =
            AppToolbarContentCollection(
                entries = entries.toImmutableList(),
            )

        companion object {

            fun build(block: Builder.() -> Unit): AppToolbarContentCollection {
                val builder = Builder()
                builder.block()
                return builder.build()
            }
        }
    }

    @Immutable
    companion object {

        val Instance = Builder.build {
            title<Settings>(R.string.settings)
            title<ListFormatSettings>(R.string.list_format)
            title<BottomBarSettings>(R.string.bottom_bar)
        }
    }
}
