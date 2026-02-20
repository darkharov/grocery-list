package app.grocery.list.data.internal.di

import android.content.Context
import androidx.room.Room
import app.grocery.list.data.internal.db.AppDatabase
import app.grocery.list.data.internal.di.migrations.MigrationFrom4To5
import app.grocery.list.data.product.ProductDao
import app.grocery.list.data.product.list.CustomProductListDao
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
        ).build()

    @Provides
    @Singleton
    fun productDao(
        appDatabase: AppDatabase,
    ): ProductDao =
        appDatabase.productDao()

    @Provides
    @Singleton
    fun customProductListDao(
        appDatabase: AppDatabase,
    ): CustomProductListDao =
        appDatabase.customProductListDao()
}
