package app.grocery.list.assembly.impls

import app.grocery.list.commons.format.ProductTitleFormatter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ImplsModule {
    @Binds
    fun productTitleFormatterErrorLogger(impl: ErrorLoggersFacade): ProductTitleFormatter.ErrorLogger
}
