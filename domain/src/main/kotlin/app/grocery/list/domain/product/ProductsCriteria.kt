package app.grocery.list.domain.product

data class ProductsCriteria(
    val enabledOnly: Boolean,
    val idOfSelectedCustomList: Int?,
)
