package app.grocery.list.domain.theming

import app.grocery.list.domain.product.list.ProductList
import app.grocery.list.domain.settings.ProductTitleFormat

data class AppTopLevelParams(
    val currentProductList: ProductList,
    val productTitleFormat: ProductTitleFormat,
    val numberOfEnabledProducts: Int,
    val customListsFeatureEnabled: Boolean,
)
