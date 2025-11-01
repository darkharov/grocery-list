package app.grocery.list.data.internal.di

import android.content.Context
import androidx.room.Room
import app.grocery.list.data.db.AppDatabase
import app.grocery.list.data.product.CategoryDao
import app.grocery.list.data.product.DEFAULT_CATEGORY_ID
import app.grocery.list.data.product.ProductDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class DbModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).fallbackToDestructiveMigration(dropAllTables = false)
            .build()

    @Provides
    @Singleton
    fun productDao(
        appDatabase: AppDatabase,
    ): ProductDao =
        appDatabase.productDao()

    @Provides
    @Named(DEFAULT_CATEGORY_ID)
    @Singleton
    fun provides(categoryDao: CategoryDao): Int =
        categoryDao.defaultCategoryId
}
