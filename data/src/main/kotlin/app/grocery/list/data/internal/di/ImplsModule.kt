package app.grocery.list.data.internal.di

import app.grocery.list.data.AppRepositoryImpl
import app.grocery.list.data.product.ProductRepositoryImpl
import app.grocery.list.data.templates.TemplateRepositoryImpl
import app.grocery.list.domain.AppRepository
import app.grocery.list.domain.product.ProductRepository
import app.grocery.list.domain.template.TemplateRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface ImplsModule {

    @Binds
    fun appRepository(impl: AppRepositoryImpl): AppRepository

    @Binds
    fun productRepository(impl: ProductRepositoryImpl): ProductRepository

    @Binds
    fun templateRepository(impl: TemplateRepositoryImpl): TemplateRepository
}
