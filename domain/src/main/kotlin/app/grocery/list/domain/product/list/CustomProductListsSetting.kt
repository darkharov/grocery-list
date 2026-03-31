package app.grocery.list.domain.product.list

sealed class CustomProductListsSetting {

    data object NotSet : CustomProductListsSetting()

    sealed class Customizable : CustomProductListsSetting()

    data object Enabled : Customizable()
    data object Disabled : Customizable()
}
