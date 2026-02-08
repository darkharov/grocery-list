package app.grocery.list.domain.faq

import kotlinx.coroutines.flow.Flow

interface FaqItemRepository {
    fun all(): Flow<List<FaqItem>>
}
