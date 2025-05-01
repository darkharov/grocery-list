package app.grocery.list.commons.compose.theme

import android.content.Context
import androidx.compose.runtime.staticCompositionLocalOf
import app.grocery.list.commons.compose.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

val LocalEmojiProvider = staticCompositionLocalOf<EmojiProvider> {
    EmojiProviderMock
}

interface EmojiProvider {
    fun obtain(): String
    fun release(emoji: String)
}

private object EmojiProviderMock : EmojiProvider {
    override fun obtain() = "🍋"
    override fun release(emoji: String) {}
}

@Singleton
class EmojiProviderImpl @Inject internal constructor(
    @ApplicationContext
    private val context: Context,
) : EmojiProvider {

    private val allEmojis = context.resources.getStringArray(R.array.toolbar_emojis)
    private val list = mutableSetOf(*(allEmojis))

    private var initialObtain = true

    init {
        list.remove(LIME)   // lime is not displayed well immediately after launch
    }

    override fun obtain(): String {
        val item = list.randomOrNull() ?: ""
        list -= item
        if (initialObtain) {
            initialObtain = false
            list.add(LIME)
        }
        return item
    }

    override fun release(emoji: String) {
        if (emoji in allEmojis) {
            list.add(emoji)
        }
    }

    companion object {
        const val LIME = "🍋‍🟩"
    }
}
