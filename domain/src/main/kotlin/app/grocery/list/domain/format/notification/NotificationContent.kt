package app.grocery.list.domain.format.notification

data class NotificationContent(
    val groupKey: Int,
    val productIds: List<Int>,
    val formattedProductTitles: String,
)
