package app.grocery.list.domain.template

interface TemplateRepository {
    suspend fun all(): List<Template>
    suspend fun productTitles(templateId: Int): List<String>
}
