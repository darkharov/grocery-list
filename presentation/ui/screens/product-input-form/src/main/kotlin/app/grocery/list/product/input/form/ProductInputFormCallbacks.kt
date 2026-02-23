package app.grocery.list.product.input.form

import androidx.compose.runtime.Stable
import app.grocery.list.commons.compose.elements.dropdown.menu.AppDropdownMenuProps

@Stable
internal interface ProductInputFormCallbacks {
    fun onAttemptToCompleteProductInput(productTitle: String, props: ProductInputFormProps)
    fun onDone(productTitle: String, props: ProductInputFormProps)
    fun onCategoriesExpandedChange(expanded: Boolean)
    fun onCategorySelected(category: AppDropdownMenuProps.Item)
    fun onProductListsExpandedChange(expanded: Boolean)
    fun onProductListSelected(productList: AppDropdownMenuProps.Item)
}


internal object ProductInputFormCallbacksMock : ProductInputFormCallbacks {
    override fun onAttemptToCompleteProductInput(productTitle: String, props: ProductInputFormProps) {}
    override fun onDone(productTitle: String, props: ProductInputFormProps) {}
    override fun onCategoriesExpandedChange(expanded: Boolean) {}
    override fun onCategorySelected(category: AppDropdownMenuProps.Item) {}
    override fun onProductListsExpandedChange(expanded: Boolean) {}
    override fun onProductListSelected(productList: AppDropdownMenuProps.Item) {}
}
