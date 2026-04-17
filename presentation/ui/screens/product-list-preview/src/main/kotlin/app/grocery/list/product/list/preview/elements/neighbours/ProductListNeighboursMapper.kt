package app.grocery.list.product.list.preview.elements.neighbours

import app.grocery.list.commons.compose.props.ProductListCountersMapper
import app.grocery.list.domain.product.list.ProductList
import app.grocery.list.kotlin.ellipsize
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.uuid.ExperimentalUuidApi

@Singleton
internal class ProductListNeighboursMapper @Inject constructor(
    private val counterMapper: ProductListCountersMapper,
) {
    @OptIn(ExperimentalUuidApi::class)
    fun toPresentation(neighbours: ProductList.Neighbours): ProductListNeighboursProps? {
        val (trailing, leading) = neighbours
        return if (
            trailing != null ||
            leading != null
        ) {
            val trailingId = trailing?.productList?.id
            val leadingId = leading?.productList?.id
            ProductListNeighboursProps(
                key = "$KEY_PREFIX trailing=$trailingId, leading=$leadingId",
                trailing = trailing?.let(::toPresentation),
                leading = leading?.let(::toPresentation),
            )
        } else {
            null
        }
    }

    private fun toPresentation(item: ProductList.WithCounters): ProductListNeighboursProps.Item =
        ProductListNeighboursProps.Item(
            title = item.productList.title.ellipsize(maxLength = 12),
            counter = counterMapper.toPresentation(item.counters),
            payload = item,
        )

    fun toDomain(item: ProductListNeighboursProps.Item): ProductList.WithCounters =
        item.payload as ProductList.WithCounters

    companion object {
        private const val KEY_PREFIX = "app.grocery.list.product.list.preview.elements.neighbours"
    }
}
