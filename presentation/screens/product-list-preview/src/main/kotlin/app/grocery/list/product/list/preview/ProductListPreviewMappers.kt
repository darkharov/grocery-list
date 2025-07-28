package app.grocery.list.product.list.preview

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import app.grocery.list.commons.format.ProductTitleFormatter
import app.grocery.list.domain.CategoryAndProducts
import app.grocery.list.domain.Product
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

internal class ProductListMapper @AssistedInject constructor(
    @Assisted
    private val formatter: ProductTitleFormatter
) {
    fun transform(productList: List<CategoryAndProducts>): ProductListPreviewProps =
        ProductListPreviewProps(
            categories = productList.map(::transform).toImmutableList(),
            payload = productList
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
            title = formatter.print(product).collectTitle(),
        )

    private fun ProductTitleFormatter.FormattingResult.collectTitle(): AnnotatedString =
        buildAnnotatedString {
            if (!(emoji.isNullOrBlank())) {
                append(emoji)
                append(' ')
            }
            val afterEmoji = length
            append(title)
            val additionalDetailsLocation = additionalDetailsLocation
            if (additionalDetailsLocation != null) {
                addStyle(
                    style = SpanStyle(Color.Gray.copy(alpha = 0.5f)),
                    start = afterEmoji + additionalDetailsLocation.startIndex,
                    end = afterEmoji + additionalDetailsLocation.endIndex,
                )
            }
        }

    @AssistedFactory
    fun interface Factory {
        fun create(formatter: ProductTitleFormatter): ProductListMapper
    }
}
