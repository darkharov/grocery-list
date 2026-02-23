package app.grocery.list.product.list.preview

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.parcelize.Parcelize

@Immutable
sealed class ProductListPreviewProps {

    @Immutable
    data class Empty(
        val templates: List<Template>,
    ) : ProductListPreviewProps() {

        @Immutable
        data class Template(
            val id: Int,
            val title: String,
        )
    }

    @Immutable
    data class Items(
        val items: ImmutableList<CategoryAndFormattedProducts>,
        val enableAndDisableAll: EnableAndDisableAll?,
        val needMoreListsButtonVisible: Boolean,
    ) : ProductListPreviewProps() {

        @Immutable
        data class EnableAndDisableAll(
            val enableAllAvailable: Boolean,
            val disableAllAvailable: Boolean,
        ) {
            val key = EnabledAllAndDisableAllKey
        }

        @Immutable
        data class CategoryAndFormattedProducts(
            val category: Category?,
            val products: List<Product>,
        )

        @Immutable
        data class Category(
            val id: Int,
            val title: String,
        ) {
            val key = CategoryKey(id = id)
            val topOffsetKey = CategoryTopOffsetKey(id = id)
        }

        @Immutable
        data class Product(
            val id: Int,
            val enabled: Boolean = true,
            val title: AnnotatedString,
        ) {
            val key = ProductKey(id = id)
        }
    }


    @Immutable
    @Parcelize
    data object EnabledAllAndDisableAllKey : Parcelable

    @Immutable
    @Parcelize
    data class ProductKey(val id: Int) : Parcelable

    @Immutable
    @Parcelize
    data class CategoryKey(val id: Int) : Parcelable

    @Immutable
    @Parcelize
    data class CategoryTopOffsetKey(val id: Int) : Parcelable
}

internal class ProductListPreviewMocks : PreviewParameterProvider<ProductListPreviewProps?> {

    private val productIds = generateSequence(1) { it + 1 }.iterator()
    private val categoryIds = generateSequence(1) { it + 1 }.iterator()

    private val prototype = ProductListPreviewProps.Items(
        enableAndDisableAll = ProductListPreviewProps.Items.EnableAndDisableAll(
            enableAllAvailable = true,
            disableAllAvailable = false,
        ),
        items = persistentListOf(
            ProductListPreviewProps.Items.CategoryAndFormattedProducts(
                category = ProductListPreviewProps.Items.Category(
                    id = categoryIds.next(),
                    title = "Veggies",
                ),
                products = persistentListOf(
                    ProductListPreviewProps.Items.Product(
                        id = productIds.next(),
                        title = AnnotatedString("🍅 Tomato"),
                    ),
                    ProductListPreviewProps.Items.Product(
                        id = productIds.next(),
                        title = AnnotatedString("🥔 Potato"),
                    ),
                ),
            ),
            ProductListPreviewProps.Items.CategoryAndFormattedProducts(
                category = ProductListPreviewProps.Items.Category(
                    id = categoryIds.next(),
                    title = "Dairy Products",
                ),
                products = persistentListOf(
                    ProductListPreviewProps.Items.Product(
                        id = productIds.next(),
                        title = AnnotatedString("🥛Milk 2L"),
                    ),
                    ProductListPreviewProps.Items.Product(
                        id = productIds.next(),
                        title = AnnotatedString("Yogurt x8"),
                    ),
                    ProductListPreviewProps.Items.Product(
                        id = productIds.next(),
                        title = AnnotatedString("Feta"),
                    ),
                    ProductListPreviewProps.Items.Product(
                        id = productIds.next(),
                        title = AnnotatedString("Blue Cheese"),
                    ),
                ),
            ),
            ProductListPreviewProps.Items.CategoryAndFormattedProducts(
                category = ProductListPreviewProps.Items.Category(
                    id = categoryIds.next(),
                    title = "Sweets",
                ),
                products = persistentListOf(
                    ProductListPreviewProps.Items.Product(
                        id = productIds.next(),
                        title = AnnotatedString("🍬 Candies"),
                    ),
                    ProductListPreviewProps.Items.Product(
                        id = productIds.next(),
                        title = AnnotatedString("🍦 Ice Cream"),
                    ),
                    ProductListPreviewProps.Items.Product(
                        id = productIds.next(),
                        title = AnnotatedString("Buns"),
                    ),
                ),
            ),
        ),
        needMoreListsButtonVisible = true,
    )

    override val values: Sequence<ProductListPreviewProps?> =
        sequenceOf(
            //null,
            prototype,
        )
}
