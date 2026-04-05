package app.grocery.list.product.list.preview

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import app.grocery.list.commons.compose.elements.question.AppQuestionMapper
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.domain.formatter.ProductTitleFormatter
import app.grocery.list.domain.preview.ProductListPreview
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.collections.immutable.toImmutableList

@Singleton
internal class ProductListPreviewMapper @Inject constructor(
    private val questionMapper: AppQuestionMapper,
) {
    fun transform(preview: ProductListPreview): ProductListPreviewProps =
        when (preview) {
            is ProductListPreview.Empty.Default -> {
                ProductListPreviewProps.Empty(
                    text = StringValue.ResId(R.string.list_is_empty),
                    templates = preview.templates.map {
                        ProductListPreviewProps.Empty.Template(
                            id = it.id,
                            title = it.title,
                        )
                    },
                )
            }
            is ProductListPreview.Empty.CustomList -> {
                ProductListPreviewProps.Empty(
                    text = StringValue.ResId(
                        resId = R.string.template_no_products_in_custom_list_yet,
                        arguments = listOf(preview.title),
                    ),
                    templates = null,
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
                    question = questionMapper.toPresentationNullable(preview.question),
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
