package app.grocery.list.domain.preview

import app.grocery.list.domain.formatter.ProductTitleFormatter
import app.grocery.list.domain.product.Product
import app.grocery.list.domain.template.Template

sealed class ProductListPreview {

    data class Empty(
        val templates: List<Template>,
    ) : ProductListPreview()

    data class Items(
        val categories: List<CategoryContent>,
        val enableAndDisableAllFeatures: EnableAndDisableAll?,
    ) : ProductListPreview() {

        data class EnableAndDisableAll(
            val enableAllAvailable: Boolean,
            val disableAllAvailable: Boolean,
        )

        data class CategoryContent(
            val category: Product.Category?,
            val formattedProducts: List<FormattedProduct>,
        )

        data class FormattedProduct(
            val productId: Int,
            val title: ProductTitleFormatter.Result,
            val enabled: Boolean,
        )
    }
}
