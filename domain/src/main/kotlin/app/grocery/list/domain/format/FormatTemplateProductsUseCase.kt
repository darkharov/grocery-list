package app.grocery.list.domain.format

import app.grocery.list.domain.format.sharing.ParseProductListUseCase
import app.grocery.list.domain.template.TemplateRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FormatTemplateProductsUseCase @Inject constructor(
    private val templateRepository: TemplateRepository,
    private val parseProducts: ParseProductListUseCase,
) {
    suspend fun execute(
        templateId: Int,
        separator: ProductListSeparator,
    ): FormattedProducts {
        val titles = templateRepository
            .productTitles(templateId = templateId)
            .joinToString()
        return parseProducts
            .execute(titles, separator)
            .getOrThrow()
    }
}
