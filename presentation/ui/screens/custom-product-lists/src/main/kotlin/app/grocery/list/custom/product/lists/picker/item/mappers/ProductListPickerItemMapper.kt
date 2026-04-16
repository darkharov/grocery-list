package app.grocery.list.custom.product.lists.picker.item.mappers

import app.grocery.list.commons.compose.elements.color.scheme.ColorSchemeMapper
import app.grocery.list.commons.compose.props.ProductListCountersMapper
import app.grocery.list.custom.product.lists.picker.item.ProductListPickerItemProps
import app.grocery.list.domain.product.list.ProductList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ProductListPickerItemMapper @Inject constructor(
    private val demoColorsMapper: ColorSchemeMapper,
    private val productListIdMapper: ProductListIdMapper,
    private val productListCountersMapper: ProductListCountersMapper,
) {
    fun toPresentation(summary: ProductList.Summary): ProductListPickerItemProps {
        val productList = summary.productList
        return ProductListPickerItemProps(
            id = productListIdMapper.toPresentation(
                id = productList.id,
            ),
            counter = productListCountersMapper.toPresentation(summary.counters),
            title = productList.title,
            stub = summary.formattedStub.ifBlank { null },
            colorScheme = demoColorsMapper.toPresentation(
                colorScheme = summary.productList.colorScheme,
            ),
            selected = summary.isSelected,
            deletable = summary.productList.id is ProductList.Id.Custom,
            payload = summary,
        )
    }

    fun toDomain(props: ProductListPickerItemProps): ProductList.Summary =
        props.payload as ProductList.Summary
}
