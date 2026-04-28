package app.grocery.list.product.list.preview

import app.grocery.list.commons.compose.elements.question.AppQuestionMapper
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.domain.preview.ProductListPreview
import app.grocery.list.product.list.preview.elements.empty.list.placeholder.EmptyListPlaceholderProps
import app.grocery.list.product.list.preview.elements.neighbours.ProductListNeighboursMapper
import app.grocery.list.product.list.preview.elements.product.item.ProductItemMapper
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.collections.immutable.toImmutableList

@Singleton
internal class ProductListPreviewMapper @Inject constructor(
    private val questionMapper: AppQuestionMapper,
    private val productListNeighboursMapper: ProductListNeighboursMapper,
    private val productItemMapper: ProductItemMapper,
) {
    fun toPresentation(preview: ProductListPreview): ProductListPreviewProps =
        ProductListPreviewProps(
            content = currentList(preview),
            neighbours = productListNeighboursMapper.toPresentation(preview.neighbours),
        )

    private fun currentList(preview: ProductListPreview): ProductListPreviewProps.ListContent =
        when (val currentList = preview.currentList) {
            is ProductListPreview.Empty.Default -> {
                ProductListPreviewProps.Empty(
                    placeholder = EmptyListPlaceholderProps(
                        key = "Empty Default List",
                        text = StringValue.ResId(R.string.list_is_empty),
                        templates = currentList.templates.map {
                            EmptyListPlaceholderProps.Template(
                                id = it.id,
                                title = it.title,
                            )
                        },
                    )
                )
            }
            is ProductListPreview.Empty.CustomList -> {
                ProductListPreviewProps.Empty(
                    placeholder = EmptyListPlaceholderProps(
                        key = "Empty Custom List ${currentList.title}",
                        text = StringValue.ResId(
                            resId = R.string.template_no_products_in_custom_list_yet,
                            arguments = listOf(currentList.title),
                        ),
                        templates = null,
                    )
                )
            }
            is ProductListPreview.Items -> {
                val enableAndDisableAllFeatures = currentList.enableAndDisableAllFeatures
                ProductListPreviewProps.Items(
                    items = currentList.categories.mapIndexed { index, it ->
                        val category = it.category
                        ProductListPreviewProps.Items.CategoryAndFormattedProducts(
                            category = if (category == null) {
                                null
                            } else {
                                ProductListPreviewProps.Items.Category(
                                    id = category.id,
                                    title = category.title,
                                    topOffsetHolder = if (index == 0) {
                                        ProductListPreviewProps.Items.Category.TopOffsetHolder.First
                                    } else {
                                        ProductListPreviewProps.Items.Category.TopOffsetHolder.Subsequent
                                    },
                                )
                            },
                            products = it
                                .formattedProducts
                                .map(productItemMapper::toPresentation)
                                .toImmutableList(),
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
                    question = questionMapper.toPresentationNullable(currentList.question),
                )
            }
        }
}
