package app.grocery.list.commons.compose.elements.dialog.list

import app.grocery.list.domain.format.FormattedProducts
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfirmPastedListDialogMapper @Inject constructor() {

    fun transform(products: FormattedProducts) =
        ConfirmPastedListDialogProps(
            numberOfFoundProducts = products.items.size,
            titlesAsString = products.formattedString,
            productList = products.items,
        )
}
