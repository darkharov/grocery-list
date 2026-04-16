package app.grocery.list.domain.preview

import app.grocery.list.domain.category.Category
import app.grocery.list.domain.formatter.ProductTitleFormatter
import app.grocery.list.domain.product.list.ProductList
import app.grocery.list.domain.question.Question
import app.grocery.list.domain.template.Template

data class ProductListPreview(
    val currentList: CurrentListContent,
    val neighbours: ProductList.Neighbours,
) {
    sealed class CurrentListContent

    sealed class Empty : CurrentListContent() {
        data class Default(val templates: List<Template>) : Empty()
        data class CustomList(val title: String) : Empty()
    }

    data class Items(
        val categories: List<CategoryContent>,
        val enableAndDisableAllFeatures: EnableAndDisableAll?,
        val question: Question?,
    ) : CurrentListContent() {

        data class EnableAndDisableAll(
            val enableAllAvailable: Boolean,
            val disableAllAvailable: Boolean,
        )

        data class CategoryContent(
            val category: Category?,
            val formattedProducts: List<FormattedProduct>,
        )

        data class FormattedProduct(
            val productId: Int,
            val title: ProductTitleFormatter.Result,
            val enabled: Boolean,
        )
    }
}
