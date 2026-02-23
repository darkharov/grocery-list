package app.grocery.list.product.list.preview

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import app.grocery.list.domain.formatter.ProductTitleFormatter
import app.grocery.list.domain.preview.ProductListPreview
import kotlinx.collections.immutable.toImmutableList

internal object ProductListPreviewMapper {

    fun transform(preview: ProductListPreview): ProductListPreviewProps =
        when (preview) {
            is ProductListPreview.Empty -> {
                ProductListPreviewProps.Empty(
                    templates = preview.templates.map {
                        ProductListPreviewProps.Empty.Template(
                            id = it.id,
                            title = it.title,
                        )
                    },
                )
            }
            is ProductListPreview.Items -> {
                val enableAndDisableAllFeatures = preview.enableAndDisableAllFeatures
                ProductListPreviewProps.Items(
                    items = preview.categories.map {
                        val category = it.category
                        ProductListPreviewProps.Items.CategoryAndFormattedProducts(
                            category = if (category == null) {
                                null
                            } else {
                                ProductListPreviewProps.Items.Category(
                                    id = category.id,
                                    title = category.title,
                                )
                            },
                            products = it.formattedProducts.map { product ->
                                ProductListPreviewProps.Items.Product(
                                    id = product.productId,
                                    enabled = product.enabled,
                                    title = product.title.collectTitle(),
                                )
                            }
                        )
                    }.toImmutableList(),
                    enableAndDisableAll = if (enableAndDisableAllFeatures == null) {
                        null
                    } else {
                        ProductListPreviewProps.Items.EnableAndDisableAll(
                            enableAllAvailable = enableAndDisableAllFeatures.enableAllAvailable,
                            disableAllAvailable = enableAndDisableAllFeatures.disableAllAvailable,
                        )
                    },
                    needMoreListsButtonVisible = preview.needMoreListsQuestion,
                )
            }
        }

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
}
