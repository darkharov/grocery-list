package app.grocery.list.assembly.internal.di

import app.grocery.list.assembly.impls.NavigationFacade
import app.grocery.list.product.input.form.ProductInputFormNavigation
import app.grocery.list.product.list.actions.ProductListActionsNavigation
import app.grocery.list.product.list.preview.ProductListPreviewNavigation
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
interface ActivityScopedImplsModule {
    @Binds fun productInputFormNavigation(impl: NavigationFacade): ProductInputFormNavigation
    @Binds fun productListPreviewNavigation(impl: NavigationFacade): ProductListPreviewNavigation
    @Binds fun productListActionsNavigation(impl: NavigationFacade): ProductListActionsNavigation
}
