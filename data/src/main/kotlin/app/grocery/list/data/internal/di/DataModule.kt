package app.grocery.list.data.internal.di

import android.content.Context
import androidx.room.Room
import app.grocery.list.data.R
import app.grocery.list.data.internal.db.AppDatabase
import app.grocery.list.data.internal.db.migrations.MigrationFrom4To5
import app.grocery.list.data.internal.db.migrations.MigrationFrom5To6
import app.grocery.list.data.product.ProductDao
import app.grocery.list.data.product.list.ProductListDao
import app.grocery.list.domain.product.list.ProductList
import app.grocery.list.domain.theming.ColorScheme
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class DataModule {

    @Provides
    @Singleton
    fun database(
        @ApplicationContext context: Context,
    ): AppDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database",
        ).addMigrations(
            MigrationFrom4To5,
            MigrationFrom5To6,
        ).build()

    @Provides
    @Singleton
    fun productDao(
        appDatabase: AppDatabase,
    ): ProductDao =
        appDatabase.productDao()

    @Provides
    @Singleton
    fun productListDao(
        appDatabase: AppDatabase,
    ): ProductListDao =
        appDatabase.productListDao()

    @Provides
    @DefaultProductList
    @Singleton
    fun defaultProductList(
        @ApplicationContext context: Context,
    ): ProductList =
        ProductList(
            id = ProductList.Id.Default,
            title = context.getString(R.string.grocery_list),
            colorScheme = ColorScheme.Yellow,
        )
}
