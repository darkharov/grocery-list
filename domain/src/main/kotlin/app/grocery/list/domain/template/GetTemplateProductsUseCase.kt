package app.grocery.list.domain.template

import app.grocery.list.domain.sharing.ParseAndFormatProductsUseCase
import app.grocery.list.domain.sharing.ProductsAndFormattedTitles
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTemplateProductsUseCase @Inject constructor(
    private val templateRepository: TemplateRepository,
    private val parseProducts: ParseAndFormatProductsUseCase,
) {
    suspend fun execute(templateId: Int): ProductsAndFormattedTitles {
        val titles = templateRepository
            .productTitles(templateId = templateId)
            .joinToString()
        return parseProducts
            .execute(text = titles)
            .getOrThrow()
    }
}
