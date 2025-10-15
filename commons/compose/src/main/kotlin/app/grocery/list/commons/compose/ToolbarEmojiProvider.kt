package app.grocery.list.commons.compose

import android.content.Context
import androidx.compose.runtime.staticCompositionLocalOf
import app.grocery.list.domain.Holiday
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

val LocalToolbarEmojiProvider = staticCompositionLocalOf<ToolbarEmojiProvider> {
    ToolbarEmojiProviderMock
}

interface ToolbarEmojiProvider {
    fun get(number: Int): String
}

private object ToolbarEmojiProviderMock : ToolbarEmojiProvider {
    override fun get(number: Int) = "🍋".repeat(number)
}

@Singleton
class ToolbarEmojiProviderImpl @Inject internal constructor(
    @ApplicationContext
    private val context: Context,
) : ToolbarEmojiProvider {

    private val emojis by lazy { context.resources.getStringArray(R.array.toolbar_emojis) }

    init {
        emojis.shuffle()
        if (emojis[0] == LIME) {    // lime is not displayed well
            emojis[0] = emojis[1]   // immediately after app launch
            emojis[1] = LIME
        }
    }

    override fun get(number: Int): String {
        val holiday = Holiday.entries.find { it.timeToChangeEmoji() }
        if (holiday != null) {
            return holiday.emoji()
        }
        val result = emojis.take(number).joinToString(separator = " ")
        emojis.shuffle()
        return result
    }

    companion object {
        const val LIME = "🍋‍🟩"
    }
}
