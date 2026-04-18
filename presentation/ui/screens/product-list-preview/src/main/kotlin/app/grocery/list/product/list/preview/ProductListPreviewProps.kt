package app.grocery.list.product.list.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import app.grocery.list.commons.compose.elements.question.AppQuestionProps
import app.grocery.list.commons.compose.layout.AppArrangement
import app.grocery.list.product.list.preview.elements.empty.list.placeholder.EmptyListPlaceholderMocks
import app.grocery.list.product.list.preview.elements.empty.list.placeholder.EmptyListPlaceholderProps
import app.grocery.list.product.list.preview.elements.neighbours.ProductListNeighboursMocks
import app.grocery.list.product.list.preview.elements.neighbours.ProductListNeighboursProps
import app.grocery.list.product.list.preview.elements.product.item.ProductItemProps
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class ProductListPreviewProps(
    val content: ListContent,
    val neighbours: ProductListNeighboursProps?,
) {
    val arrangement
        get() =
            if (neighbours != null) {
                content.arrangementWithNeighbours
            } else {
                Arrangement.Top
            }

    @Immutable
    sealed class ListContent {

        @Stable
        abstract val arrangementWithNeighbours: Arrangement.Vertical
    }

    @Immutable
    data class Empty(
        val placeholder: EmptyListPlaceholderProps,
    ) : ListContent() {

        @Stable
        override val arrangementWithNeighbours get() = AppArrangement.LastAtBottomRestInCenter
    }

    @Immutable
    data class Items(
        val items: ImmutableList<CategoryAndFormattedProducts>,
        val enableAndDisableAll: EnableAndDisableAll?,
        val question: AppQuestionProps?,
    ) : ListContent() {

        @Stable
        override val arrangementWithNeighbours get() = AppArrangement.LastAtBottomRestAtTop

        @Immutable
        data class EnableAndDisableAll(
            val enableAllAvailable: Boolean,
            val disableAllAvailable: Boolean,
        )

        @Immutable
        data class CategoryAndFormattedProducts(
            val category: Category?,
            val products: ImmutableList<ProductItemProps>,
        )

        @Immutable
        data class Category(
            val id: Int,
            val title: String,
        ) {
            val key = "Category $id"
            val topOffsetKey = "CategoryTopOffset $id"
        }
    }
}

internal class ProductListPreviewMocks : PreviewParameterProvider<ProductListPreviewProps?> {

    private val categoryIds = generateSequence(1) { it + 1 }.iterator()

    private val productKeys =
        generateSequence(1) { it + 1 }
            .map { "Product $it" }
            .iterator()

    private val veggies = persistentListOf(
        ProductItemProps(
            key = productKeys.next(),
            title = AnnotatedString("🍅 Tomato"),
        ),
        ProductItemProps(
            key = productKeys.next(),
            title = AnnotatedString("🥔 Potato"),
        ),
    )

    private val dairy = persistentListOf(
        ProductItemProps(
            key = productKeys.next(),
            title = AnnotatedString("🥛Milk 2L"),
        ),
        ProductItemProps(
            key = productKeys.next(),
            title = AnnotatedString("Yogurt x8"),
        ),
        ProductItemProps(
            key = productKeys.next(),
            title = AnnotatedString("Feta"),
        ),
        ProductItemProps(
            key = productKeys.next(),
            title = AnnotatedString("Blue Cheese"),
        ),
    )

    private val sweets = persistentListOf(
        ProductItemProps(
            key = productKeys.next(),
            title = AnnotatedString("🍬 Candies"),
        ),
        ProductItemProps(
            key = productKeys.next(),
            title = AnnotatedString("🍦 Ice Cream"),
        ),
        ProductItemProps(
            key = productKeys.next(),
            title = AnnotatedString("Buns"),
        ),
    )

    private val currentListContentPrototype = ProductListPreviewProps.Items(
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
                products = veggies,
            ),
            ProductListPreviewProps.Items.CategoryAndFormattedProducts(
                category = ProductListPreviewProps.Items.Category(
                    id = categoryIds.next(),
                    title = "Dairy Products",
                ),
                products = dairy,
            ),
            ProductListPreviewProps.Items.CategoryAndFormattedProducts(
                category = ProductListPreviewProps.Items.Category(
                    id = categoryIds.next(),
                    title = "Sweets",
                ),
                products = sweets,
            ),
        ),
        question = null,
    )

    override val values: Sequence<ProductListPreviewProps?> =
        sequenceOf(
            null,
            ProductListPreviewProps(
                content = currentListContentPrototype,
                neighbours = ProductListNeighboursMocks.prototype,
            ),
            ProductListPreviewProps(
                content = ProductListPreviewProps.Empty(
                    placeholder = EmptyListPlaceholderMocks.prototype,
                ),
                neighbours = ProductListNeighboursMocks.prototype,
            ),
        )
}
