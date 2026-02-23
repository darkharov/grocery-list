package app.grocery.list.custom.product.lists.picker.item.mappers

import app.grocery.list.commons.compose.elements.color.scheme.ColorSchemeMapper
import app.grocery.list.custom.product.lists.picker.item.ProductListPickerItemProps
import app.grocery.list.domain.product.list.ProductList
import javax.inject.Inject

internal class ProductListPickerItemMapper @Inject constructor(
    private val demoColorsMapper: ColorSchemeMapper,
    private val productListIdMapper: ProductListIdMapper,
) {
    fun toPresentation(summary: ProductList.Summary): ProductListPickerItemProps {
        val productList = summary.productList
        val (totalSize, numberOfEnabled) = summary.counters
        return ProductListPickerItemProps(
            id = productListIdMapper.toPresentation(
                id = productList.id,
            ),
            counter = when (totalSize) {
                0 -> {
                    ProductListPickerItemProps.Counter.NoItems
                }
                numberOfEnabled -> {
                    ProductListPickerItemProps.Counter.JustTotalSize(totalSize)
                }
                else -> {
                    ProductListPickerItemProps.Counter.Ratio(
                        numberOfEnabled = numberOfEnabled,
                        totalSize = totalSize,
                    )
                }
            },
            title = productList.title,
            stub = summary.formattedStub.ifBlank { null },
            demoColors = demoColorsMapper.toPresentation(
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
