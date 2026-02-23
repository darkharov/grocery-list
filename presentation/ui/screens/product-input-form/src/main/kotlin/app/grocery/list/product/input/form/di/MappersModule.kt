package app.grocery.list.product.input.form.di

import app.grocery.list.commons.compose.elements.dropdown.menu.AppDropdownMenuMapper
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.product.input.form.R
import app.grocery.list.product.input.form.mappers.CategoryDropdownMenuItemMapper
import app.grocery.list.product.input.form.mappers.ProductListDropdownMenuItemMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class MappersModule {

    @Provides
    @Singleton
    fun categoryDropdownMenuMapper(categoryMapper: CategoryDropdownMenuItemMapper) =
        AppDropdownMenuMapper(
            label = StringValue.ResId(R.string.category),
            itemMapper = categoryMapper,
        )

    @Provides
    @Singleton
    fun productListDropdownMenuMapper(productListMapper: ProductListDropdownMenuItemMapper) =
        AppDropdownMenuMapper(
            label = StringValue.ResId(R.string.product_list),
            itemMapper = productListMapper,
        )
}
