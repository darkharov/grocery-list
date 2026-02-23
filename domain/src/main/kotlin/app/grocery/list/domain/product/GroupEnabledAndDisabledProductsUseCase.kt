package app.grocery.list.domain.product

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

@Singleton
class GroupEnabledAndDisabledProductsUseCase @Inject internal constructor(
    private val getProducts: GetProductsUseCase,
) {
    suspend fun execute(): EnabledAndDisabledProducts =
        getProducts
            .execute(enabledOnly = false)
            .map { products ->
                EnabledAndDisabledProducts(
                    all = products,
                    enabled = products.filter { it.enabled },
                    disabled = products.filter { !(it.enabled) },
                )
            }
            .first()
}
