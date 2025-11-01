package app.grocery.list.domain.notification

data class NotificationContent(
    val groupKey: Int,
    val productIds: List<Int>,
    val formattedProductTitles: String,
)
