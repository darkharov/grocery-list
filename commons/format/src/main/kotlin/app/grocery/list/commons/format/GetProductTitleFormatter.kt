package app.grocery.list.commons.format

import app.grocery.list.domain.AppRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class GetProductTitleFormatter @Inject internal constructor(
    private val repository: AppRepository,
    private val factory: ProductTitleFormatter.Factory,
) {
    fun execute(): Flow<ProductTitleFormatter> =
        repository
            .productTitleFormat
            .observe()
            .map { format ->
                factory.create(format)
            }
}
