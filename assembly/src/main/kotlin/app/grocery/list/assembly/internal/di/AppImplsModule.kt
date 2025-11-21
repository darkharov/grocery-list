package app.grocery.list.assembly.internal.di

import app.grocery.list.assembly.internal.impls.MainActivityContractImpl
import app.grocery.list.main.activity.ui.MainActivity
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AppImplsModule {

    @Binds
    fun mainActivityContract(impl: MainActivityContractImpl): MainActivity.Contract
}
