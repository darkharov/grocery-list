package app.grocery.list.data

import android.content.Context
import app.grocery.list.domain.Product
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CategoryDao @Inject constructor(
    @ApplicationContext
    private val context: Context,
) {
    private val categoriesKeywordsIds = mapOf(
        R.string.category_fruits_vegetables_berries to R.array.fruits_and_vegetables,
        R.string.category_meat_fish_shrimp to R.array.meat_fish_shrimp,
        R.string.category_flour_products_and_sweets to R.array.flour_products_and_sweets,
        R.string.category_dairy_products to R.array.dairy_products,
        R.string.category_other to R.array.other,
    )

    private val categories: List<Product.Category> =
        categoriesKeywordsIds
            .toList()
            .mapIndexed { index, (titleId, keywordsArrayId) ->
                Product.Category(
                    id = index,
                    title = context.getString(titleId),
                    keywords = context.resources.getStringArray(keywordsArrayId)
                        .flatMap { it.split("|") }
                        .toList(),
                )
            }

    fun all(): List<Product.Category> =
        categories

    fun find(search: String): Product.Category? {
        val (category, keyword) = categories.associateWith {
            it.keywords.find { keyword ->
                search.contains(keyword, ignoreCase = true)
            }
        }.maxBy { it.value.orEmpty() }
        return if (keyword != null) {
            category
        } else {
            return null
        }
    }
}
