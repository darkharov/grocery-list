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
    private val formatter: ProductTitleFormatter,
) {
    fun transform(productList: List<CategoryAndProducts>): ProductListPreviewProps {
        val mapped = productList.map(::transform).toImmutableList()
        val products = mapped.flatMap { it.products }
        return ProductListPreviewProps(
            enableAndDisableAll = enableAndDisableAllState(products),
            categories = mapped,
        )
    }

    private fun transform(categoryAndProducts: CategoryAndProducts): ProductListPreviewProps.Category =
        ProductListPreviewProps.Category(
            id = categoryAndProducts.category.id,
            title = categoryAndProducts.category.title,
            products = transformList(categoryAndProducts.products),
        )

    private fun transformList(products: List<Product>): ImmutableList<ProductListPreviewProps.Product> =
        products
            .map { product ->
                ProductListPreviewProps.Product(
                    id = product.id,
                    title = formatter.print(product).collectTitle(),
                    enabled = product.enabled,
                )
            }
            .toImmutableList()

    private fun ProductTitleFormatter.FormattingResult.collectTitle(): AnnotatedString =
        buildAnnotatedString {
            if (!(emoji.isNullOrBlank())) {
                append(emoji)
                append(' ')
            }
            val afterEmoji = length
            append(title)
            val additionalDetailsLocation = additionalDetails
            if (additionalDetailsLocation != null) {
                addStyle(
                    style = SpanStyle(Color.Gray.copy(alpha = 0.5f)),
                    start = afterEmoji + additionalDetailsLocation.startIndex,
                    end = afterEmoji + additionalDetailsLocation.endIndex,
                )
            }
        }

    private fun enableAndDisableAllState(products: List<ProductListPreviewProps.Product>) =
        when {
            products.none() -> {
                null
            }
            else -> {
                ProductListPreviewProps.EnableAndDisableAllState(
                    enableAllAvailable = products.any { !(it.enabled) },
                    disableAllAvailable = products.any { it.enabled },
                )
            }
        }

    @AssistedFactory
    fun interface Factory {
        fun create(formatter: ProductTitleFormatter): ProductListMapper
    }
}
