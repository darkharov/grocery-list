package app.grocery.list.domain.format

import app.grocery.list.domain.AppRepository
import app.grocery.list.domain.format.sharing.ParseProductListUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FormatTemplateProductsUseCase @Inject constructor(
    private val repository: AppRepository,
    private val parseProducts: ParseProductListUseCase,
) {
    suspend fun execute(
        templateId: Int,
        separator: ProductListSeparator,
    ): FormattedProducts {
        val titles = repository
            .productTitles(templateId = templateId)
            .joinToString()
        return parseProducts
            .execute(titles, separator)
            .getOrThrow()
    }
}
