package app.grocery.list.domain.category

import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun all(): Flow<List<Category>>
    suspend fun get(id: Int): Category
    suspend fun find(search: CharSequence): Category?
}
