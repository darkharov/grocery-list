package app.grocery.list.assembly.impls

import app.grocery.list.domain.sharing.SharingStringFormatter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ImplsModule {

    @Binds
    fun sharingStringFormatterContract(impl: SharingStringFormatterContractImpl): SharingStringFormatter.Contract
}
