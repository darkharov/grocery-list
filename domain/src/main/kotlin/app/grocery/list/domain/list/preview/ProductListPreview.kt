package app.grocery.list.domain.list.preview

import app.grocery.list.domain.format.ProductTitleFormatter
import app.grocery.list.domain.product.Product
import app.grocery.list.domain.template.Template

sealed class ProductListPreview {

    data class Empty(
        val templates: List<Template>,
    ) : ProductListPreview()

    data class Items(
        val items: List<Item>,
        val enableAndDisableAllFeatures: EnableAndDisableAll?,
    ) : ProductListPreview() {

        data class EnableAndDisableAll(
            val enableAllAvailable: Boolean,
            val disableAllAvailable: Boolean,
        )

        data class Item(
            val category: Product.Category?,
            val products: List<FormattedProduct>,
        )

        data class FormattedProduct(
            val productId: Int,
            val enabled: Boolean,
            val formattingResult: ProductTitleFormatter.Result,
        )
    }
}
