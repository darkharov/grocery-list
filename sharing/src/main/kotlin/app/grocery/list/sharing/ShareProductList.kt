package app.grocery.list.sharing

import android.content.Context
import app.grocery.list.domain.Product
import app.grocery.list.sharing.internal.ProductListFormatter
import commons.android.share
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class ShareProductList @Inject internal constructor(
    @ActivityContext
    private val context: Context,
    private val productListFormatter: ProductListFormatter,
) {
    fun execute(products: List<Product>) {
        val suffix = suffix()
        val text = productListFormatter.print(products, suffix)
        context.share(text = text)
    }

    private fun suffix(): String =
        context.getString(
            R.string.sharing_message_suffix,
            context.getString(R.string.actions),
            "https://play.google.com/store/apps/details?id=app.grocery.list",
        )
}
