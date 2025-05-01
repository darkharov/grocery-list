package app.grocery.list.assembly.impls

import android.app.Activity
import android.content.Context
import android.content.Intent
import app.grocery.list.product.input.form.ProductInputFormActivity
import app.grocery.list.product.input.form.ProductInputFormNavigation
import app.grocery.list.product.list.actions.ProductListActionsActivity
import app.grocery.list.product.list.actions.ProductListActionsNavigation
import app.grocery.list.product.list.preview.ProductListPreviewActivity
import app.grocery.list.product.list.preview.ProductListPreviewNavigation
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class NavigationFacade @Inject constructor(
    @ActivityContext
    private val context: Context,
) : ProductInputFormNavigation,
    ProductListPreviewNavigation,
    ProductListActionsNavigation {

    override fun goToProductListPreview() {
        val intent = Intent(context, ProductListPreviewActivity::class.java)
        context.startActivity(intent)
    }

    override fun goToProductListActions() {
        val intent = Intent(context, ProductListActionsActivity::class.java)
        context.startActivity(intent)
    }

    override fun backToProductInputForm() {
        val intent = Intent(context, ProductInputFormActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }

    override fun goToHowItWorks() {
        (context as Activity).finishAffinity()
    }
}
