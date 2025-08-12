package app.grocery.list.product.list.preview

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.parcelize.Parcelize

@Immutable
internal data class ProductListPreviewProps(
    val categories: ImmutableList<Category>,
    val payload: Any? = null,
) {
    @Immutable
    data class Product(
        val id: Int,
        val enabled: Boolean = true,
        val title: AnnotatedString,
        val payload: Any? = null,
    ) {
        val key = ProductKey(id = id)
    }

    @Immutable
    data class Category(
        val id: Int,
        val title: String,
        val products: ImmutableList<Product>,
    ) {
        val key = CategoryKey(id = id)
    }

    @Immutable
    @Parcelize
    data class ProductKey(val id: Int) : Parcelable

    @Immutable
    @Parcelize
    data class CategoryKey(val id: Int) : Parcelable
}


internal class ProductListPreviewMocks : PreviewParameterProvider<ProductListPreviewProps?> {

    private val productIds = generateSequence(1) { it + 1 }.iterator()
    private val categoryIds = generateSequence(1) { it + 1 }.iterator()

    private val prototype = ProductListPreviewProps(
            persistentListOf(
                ProductListPreviewProps.Category(
                    id = categoryIds.next(),
                    title = "Veggies",
                    products = persistentListOf(
                        ProductListPreviewProps.Product(
                            id = productIds.next(),
                            title = AnnotatedString("🍅 Tomato"),
                        ),
                        ProductListPreviewProps.Product(
                            id = productIds.next(),
                            title = AnnotatedString("🥔 Potato"),
                        ),
                    ),
                ),
                ProductListPreviewProps.Category(
                    id = categoryIds.next(),
                    title = "Dairy Products",
                    products = persistentListOf(
                        ProductListPreviewProps.Product(
                            id = productIds.next(),
                            title = AnnotatedString("🥛Milk 2L"),
                        ),
                        ProductListPreviewProps.Product(
                            id = productIds.next(),
                            title = AnnotatedString("Yogurt x8"),
                        ),
                        ProductListPreviewProps.Product(
                            id = productIds.next(),
                            title = AnnotatedString("Feta"),
                        ),
                        ProductListPreviewProps.Product(
                            id = productIds.next(),
                            title = AnnotatedString("Blue Cheese"),
                        ),
                    ),
                ),
                ProductListPreviewProps.Category(
                    id = categoryIds.next(),
                    title = "Sweets",
                    products = persistentListOf(
                        ProductListPreviewProps.Product(
                            id = productIds.next(),
                            title = AnnotatedString("🍬 Candies"),
                        ),
                        ProductListPreviewProps.Product(
                            id = productIds.next(),
                            title = AnnotatedString("🍦 Ice Cream"),
                        ),
                        ProductListPreviewProps.Product(
                            id = productIds.next(),
                            title = AnnotatedString("Buns"),
                        ),
                    ),
                ),
            ),
        )

    override val values: Sequence<ProductListPreviewProps?> =
        sequenceOf(
            null,
            ProductListPreviewProps(
                categories = persistentListOf(),
            ),
            prototype,
        )
}
