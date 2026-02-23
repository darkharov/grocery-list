package app.grocery.list.domain.preview

import app.grocery.list.domain.category.Category
import app.grocery.list.domain.formatter.ProductTitleFormatter
import app.grocery.list.domain.template.Template

sealed class ProductListPreview {

    data class Empty(
        val templates: List<Template>,
    ) : ProductListPreview()

    data class Items(
        val categories: List<CategoryContent>,
        val enableAndDisableAllFeatures: EnableAndDisableAll?,
        val needMoreListsQuestion: Boolean,
    ) : ProductListPreview() {

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
