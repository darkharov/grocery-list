package app.grocery.list.domain.list.preview

import app.grocery.list.domain.AppRepository
import app.grocery.list.domain.format.FormattedProducts
import app.grocery.list.domain.format.ParseProductListUseCase
import app.grocery.list.domain.format.ProductListSeparator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetFormattedTemplateProductsUseCase @Inject constructor(
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
