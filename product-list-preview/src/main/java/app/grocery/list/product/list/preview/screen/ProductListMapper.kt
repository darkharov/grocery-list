package app.grocery.list.product.list.preview.screen

import app.grocery.list.domain.CategoryAndProducts
import app.grocery.list.domain.Product
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Singleton
internal class ProductListMapper @Inject constructor() {

    fun transform(productList: List<CategoryAndProducts>): ProductListPreviewProps =
        ProductListPreviewProps(
            categories = productList.map(::transform).toImmutableList(),
            payload = productList,
        )

    private fun transform(categoryAndProducts: CategoryAndProducts): ProductListPreviewProps.Category =
        ProductListPreviewProps.Category(
            id = categoryAndProducts.category.id,
            title = categoryAndProducts.category.title,
            products = transformList(categoryAndProducts.products),
        )

    private fun transformList(products: List<Product>): ImmutableList<ProductListPreviewProps.Product> =
        products.map(::transform).toImmutableList()

    private fun transform(product: Product): ProductListPreviewProps.Product =
        ProductListPreviewProps.Product(
            id = product.id,
            title = product.title,
        )
}
