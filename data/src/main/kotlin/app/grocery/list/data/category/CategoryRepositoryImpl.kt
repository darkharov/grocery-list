package app.grocery.list.data.category

import app.grocery.list.domain.category.Category
import app.grocery.list.domain.category.CategoryRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class CategoryRepositoryImpl @Inject internal constructor(
    private val dao: CategoryDao,
) : CategoryRepository {

    override fun all(): Flow<List<Category>> =
        dao.categories()

    override suspend fun get(id: Int): Category =
        dao.get(id = id)

    override suspend fun find(search: String): Category? =
        dao.category(search = search)
}
