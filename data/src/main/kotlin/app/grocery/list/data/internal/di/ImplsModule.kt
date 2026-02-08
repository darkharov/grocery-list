package app.grocery.list.data.internal.di

import app.grocery.list.data.SettingsRepositoryImpl
import app.grocery.list.data.category.CategoryRepositoryImpl
import app.grocery.list.data.faq.FaqItemRepositoryImpl
import app.grocery.list.data.product.ProductRepositoryImpl
import app.grocery.list.data.templates.TemplateRepositoryImpl
import app.grocery.list.domain.category.CategoryRepository
import app.grocery.list.domain.faq.FaqItemRepository
import app.grocery.list.domain.product.ProductRepository
import app.grocery.list.domain.settings.SettingsRepository
import app.grocery.list.domain.template.TemplateRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface ImplsModule {

    @Binds
    fun settingsRepository(impl: SettingsRepositoryImpl): SettingsRepository

    @Binds
    fun productRepository(impl: ProductRepositoryImpl): ProductRepository

    @Binds
    fun categoryRepository(impl: CategoryRepositoryImpl): CategoryRepository

    @Binds
    fun templateRepository(impl: TemplateRepositoryImpl): TemplateRepository

    @Binds
    fun faqItemRepository(impl: FaqItemRepositoryImpl): FaqItemRepository
}
