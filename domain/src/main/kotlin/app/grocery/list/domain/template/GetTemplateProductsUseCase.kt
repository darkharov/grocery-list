package app.grocery.list.domain.template

import app.grocery.list.domain.format.ProductListSeparator
import app.grocery.list.domain.sharing.ParseProductListUseCase
import app.grocery.list.domain.sharing.ParsedProducts
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTemplateProductsUseCase @Inject constructor(
    private val templateRepository: TemplateRepository,
    private val parseProducts: ParseProductListUseCase,
) {
    suspend fun execute(
        templateId: Int,
        separator: ProductListSeparator,
    ): ParsedProducts {
        val titles = templateRepository
            .productTitles(templateId = templateId)
            .joinToString()
        return parseProducts
            .execute(titles, separator)
            .getOrThrow()
    }
}
