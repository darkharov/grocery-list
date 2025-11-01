package app.grocery.list.domain.format

data class NotificationContent(
    val groupKey: Int,
    val productIds: List<Int>,
    val formattedProductTitles: String,
)
