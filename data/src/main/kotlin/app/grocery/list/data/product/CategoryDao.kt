package app.grocery.list.data.product

import android.content.Context
import androidx.annotation.ArrayRes
import app.grocery.list.data.R
import app.grocery.list.domain.product.EmojiAndCategoryId
import app.grocery.list.domain.product.EmojiSearchResult
import app.grocery.list.domain.product.Product
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Singleton
internal class CategoryDao @Inject constructor(
    @ApplicationContext
    private val context: Context,
) {
    private val categoriesKeywordFamilyArrayIds = categoriesAndKeywordFamiliesIds()
    private val categories = categoriesKeywordFamilyArrayIds.keys.toList()
    private val keywordAndCategoryWithOptionalEmojiMap = keywordAndCategoryWithOptionalEmojiMap()

    val defaultCategoryId = categories.last().id

    private fun categoriesAndKeywordFamiliesIds() =
        // DO NOT REORDER!
        mapOf(
            R.string.category_fruits_vegetables_berries to R.array.fruits_and_vegetables,
            R.string.category_meat_fish_shrimp to R.array.meat_fish_shrimp,
            R.string.category_flour_products_and_sweets to R.array.flour_products_and_sweets,
            R.string.category_dairy_products to R.array.dairy_products,
            R.string.category_other to R.array.other,
        ).toList()
            .mapIndexed { index, (titleId, keywordFamiliesId) ->
                Product.Category(
                    id = index,
                    title = context.getString(titleId),
                ) to keywordFamiliesId
            }
            .toMap()

    private fun keywordAndCategoryWithOptionalEmojiMap(): Map<String, CategoryWithOptionalEmoji> =
        categoriesKeywordFamilyArrayIds
            .toList()
            .flatMap { (category, keywordsFamilyArrayId) ->
                val keywordsAndEmojis = keywordsAndEmojis(
                    familyArrayId = keywordsFamilyArrayId,
                )
                keywordsAndEmojis.map { keywordAndEmoji ->
                    keywordAndEmoji.keyword to CategoryWithOptionalEmoji(
                        category = category,
                        emoji = keywordAndEmoji.emoji,
                    )
                }
            }
            .toMap()

    private fun keywordsAndEmojis(@ArrayRes familyArrayId: Int): List<KeywordAndEmoji> =
        context
            .resources
            .getStringArray(familyArrayId)
            .flatMap { keywordFamily ->
                toKeywordAndEmojiList(keywordFamily)
            }

    private fun toKeywordAndEmojiList(keywordFamily: String): List<KeywordAndEmoji> {
        val emoji = keywordFamily
            .substringAfter("{", missingDelimiterValue = "")
            .substringBefore("}", missingDelimiterValue = "")
            .takeIf { it.isNotBlank() }
        val withNoEmoji = keywordFamily.substringAfter("}")
        val keywords = withNoEmoji.split("|")
        return keywords.map { keyword ->
            KeywordAndEmoji(
                keyword = keyword,
                emoji = emoji,
            )
        }
    }

    fun categories(): Flow<List<Product.Category>> =
        flowOf(categories)

    fun category(search: String): Product.Category? =
        findKeywordAndCategoryWithOptionalEmoji(rawSearch = search)
            ?.value
            ?.category

    private fun findKeywordAndCategoryWithOptionalEmoji(rawSearch: String): Map.Entry<String, CategoryWithOptionalEmoji>? {
        val normalizedSearch = rawSearch.filter { it.isLetter() || it.isWhitespace() }
        return keywordAndCategoryWithOptionalEmojiMap
            .filterKeys { keyword -> normalizedSearch.contains(keyword, ignoreCase = true) }
            .maxByOrNull { it.key.length }
    }

    fun emoji(search: String): EmojiSearchResult? =
        findKeywordAndCategoryWithOptionalEmoji(search)
            ?.emojiSearchResult()

    fun emojiAndCategoryId(search: String): EmojiAndCategoryId {
        val entry = findKeywordAndCategoryWithOptionalEmoji(search)
        return EmojiAndCategoryId(
            emoji = entry?.emojiSearchResult(),
            categoryId = entry?.value?.category?.id ?: defaultCategoryId,
        )
    }

    private fun Map.Entry<String, CategoryWithOptionalEmoji>.emojiSearchResult(): EmojiSearchResult? {
        val emoji = value.emoji
        return if (!(emoji.isNullOrBlank())) {
            EmojiSearchResult(
                emoji = emoji,
                keyword = key,
            )
        } else {
            null
        }
    }

    fun get(id: Int): Product.Category =
        categories.first { it.id == id }

    private data class KeywordAndEmoji(
        val keyword: String,
        val emoji: String?,
    )


    private data class CategoryWithOptionalEmoji(
        val category: Product.Category,
        val emoji: String?,
    )
}
