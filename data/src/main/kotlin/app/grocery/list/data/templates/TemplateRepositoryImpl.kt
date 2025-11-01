package app.grocery.list.data.templates

import app.grocery.list.domain.template.Template
import app.grocery.list.domain.template.TemplateRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class TemplateRepositoryImpl @Inject constructor(
    private val dao: TemplateDao,
) : TemplateRepository {

    override suspend fun all(): List<Template> =
        dao.templates()

    override suspend fun productTitles(templateId: Int): List<String> =
        dao.productTitles(templateId = templateId)
}
