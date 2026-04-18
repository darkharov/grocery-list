package app.grocery.list.product.list.preview.elements.product.item

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import app.grocery.list.domain.formatter.ProductTitleFormatter
import app.grocery.list.domain.preview.ProductListPreview
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ProductItemMapper @Inject constructor() {

    fun toPresentation(product: ProductListPreview.Items.FormattedProduct): ProductItemProps =
        ProductItemProps(
            key = "Product ${product.productId}",
            enabled = product.enabled,
            title = product.title.collectTitle(),
            payload = product,
        )

    private fun ProductTitleFormatter.Result.collectTitle(): AnnotatedString =
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

    fun toDomain(product: ProductItemProps): ProductListPreview.Items.FormattedProduct =
        product.payload as ProductListPreview.Items.FormattedProduct
}
