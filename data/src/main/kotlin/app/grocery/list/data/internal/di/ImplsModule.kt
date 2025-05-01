package app.grocery.list.data.internal.di

import app.grocery.list.data.AppRepositoryImpl
import app.grocery.list.domain.AppRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface ImplsModule {

    @Binds
    fun productRepository(impl: AppRepositoryImpl): AppRepository
}
