package app.grocery.list.custom.product.lists.picker

import app.grocery.list.custom.product.lists.picker.item.mappers.ProductListPickerItemMapper
import app.grocery.list.domain.product.list.ProductList
import app.grocery.list.domain.question.HowToDeleteOrRenameCustomList
import app.grocery.list.domain.question.Question
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.collections.immutable.toImmutableList

@Singleton
internal class ProductListPickerMapper @Inject constructor(
    private val itemMapper: ProductListPickerItemMapper,
) {
    fun toPresentation(params: Params): ProductListPickerProps =
        ProductListPickerProps(
            items = params
                .items
                .filter { item ->
                    params.idsOfExcludedOnes.none { it == item.productList.id }
                }
                .map { item ->
                    itemMapper.toPresentation(item)
                }
                .toImmutableList(),
            question = when (val question = params.question) {
                is HowToDeleteOrRenameCustomList -> {
                    ProductListPickerProps.Question.HowToRenameOrDeleteCustomList
                }
                null -> null
                else -> {
                    throw UnsupportedOperationException("Unknown type of question: $question")
                }
            },
        )

    data class Params(
        val items: List<ProductList.Summary>,
        val idsOfExcludedOnes: Set<ProductList.Id>,
        val question: Question?,
    )
}
