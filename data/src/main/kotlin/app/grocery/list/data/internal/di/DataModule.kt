package app.grocery.list.data.internal.di

import android.content.Context
import androidx.room.Room
import app.grocery.list.data.internal.db.AppDatabase
import app.grocery.list.data.product.ProductDao
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
        ).fallbackToDestructiveMigration(dropAllTables = false)
            .build()

    @Provides
    @Singleton
    fun productDao(
        appDatabase: AppDatabase,
    ): ProductDao =
        appDatabase.productDao()
}
