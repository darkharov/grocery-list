package app.grocery.list.data

import android.content.Context
import androidx.annotation.ArrayRes
import app.grocery.list.domain.Product
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
    private val keywordsLinkedValues = keywordsLinkedValues()

    private fun categoriesAndKeywordFamiliesIds() =
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

    private fun keywordsLinkedValues(): Map<String, EmojiAndCategory> =
        categoriesKeywordFamilyArrayIds
            .toList()
            .flatMap { (category, keywordsFamilyArrayId) ->
                val keywordsAndEmojis = keywordsAndEmojis(
                    familyArrayId = keywordsFamilyArrayId,
                )
                keywordsAndEmojis.map { keywordAndEmoji ->
                    keywordAndEmoji.keyword to EmojiAndCategory(
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
        findLinkedValues(search)
            ?.value
            ?.category

    private fun findLinkedValues(search: String) =
        keywordsLinkedValues
            .filterKeys { keyword -> search.contains(keyword, ignoreCase = true) }
            .maxByOrNull { it.key }

    fun emoji(search: String): String? =
        findLinkedValues(search)
            ?.value
            ?.emoji

    private data class KeywordAndEmoji(
        val keyword: String,
        val emoji: String?,
    )


    private data class EmojiAndCategory(
        val emoji: String?,
        val category: Product.Category?,
    )
}
